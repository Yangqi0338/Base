package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsInfo;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsQuery;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PackGoodsSaveReq;
import com.newzkl.platform.base.biz.account.domain.repository.LevelRepository;
import com.newzkl.platform.base.biz.account.domain.service.LevelDomain;
import com.newzkl.platform.base.biz.account.model.assembler.LevelAssembler;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.req.LevelReq;
import com.newzkl.platform.base.biz.account.model.level.res.LevelRes;
import com.newzkl.platform.base.biz.account.model.level.vo.ConditionVO;
import com.newzkl.platform.base.biz.account.model.level.vo.LevelVO;
import com.newzkl.platform.base.biz.account.model.level.vo.PackCondition;
import com.newzkl.platform.base.biz.account.model.level.vo.PermissionVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 等级领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.service.impl.LevelDomainImpl}。
 * 旧实现直连 {@code @DubboReference IPackGoodsFacade}, 中台化后改走出站端口 {@code PackGoodsApi}。</p>
 *
 * @author KC
 */
@Service("accountLevelDomainImpl")
@RequiredArgsConstructor
public class LevelDomainImpl implements LevelDomain {

    /**
     * 入会礼包默认等级值 (旧实现硬编码为 1, 保留该语义)
     */
    private static final int DEFAULT_LEVEL_VALUE = 1;

    private final LevelRepository levelRepository;
    private final LevelAssembler assembler;
    private final PackGoodsApi packGoodsApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(LevelReq req) {
        LevelVO level = assembler.req2VO(req);
        level.setValue(DEFAULT_LEVEL_VALUE);

        // 入会礼包只有这一个修改入口, 升级条件与直推礼包配置改为必填
        ConditionVO condition = level.getCondition();
        PermissionVO permission = level.getPermission();
        if (condition == null || condition.getPack() == null
                || permission == null || permission.getDirectConfig() == null
                || !permission.getDirectConfig().checkActiveDirectPack()) {
            throw new PlatformException(BaseErrorCode.PARAM, "等级升级条件");
        }

        PackCondition pack = condition.getPack();
        PackGoodsSaveReq packGoodsSaveReq = new PackGoodsSaveReq();
        packGoodsSaveReq.setId(pack.getPackGoodsId());
        packGoodsSaveReq.setType(level.getType());
        packGoodsSaveReq.setLevel(level.getValue());
        packGoodsSaveReq.setImg(pack.getPackImg());
        packGoodsSaveReq.setAmount(pack.getAmount());
        packGoodsSaveReq.setName(pack.getPackName());
        pack.setPackGoodsId(packGoodsApi.save(packGoodsSaveReq));

        return levelRepository.save(level);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(LevelReq req) {
        if (req.getId() == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "等级");
        }
        return save(req);
    }

    @Override
    public List<LevelRes> pageList(LevelQuery query) {
        return toResList(levelRepository.pageList(query).getRecords());
    }

    @Override
    public List<LevelRes> list(LevelQuery query) {
        return toResList(levelRepository.list(query));
    }

    @Override
    public LevelRes findByQuery(LevelQuery query) {
        query.setPageSize(1);
        return CollUtil.getFirst(list(query));
    }

    @Override
    public PackGoodsInfo levelPack(Integer roleId) {
        PackGoodsQuery query = new PackGoodsQuery();
        query.setType(roleId);
        query.setLevel(DEFAULT_LEVEL_VALUE);
        return packGoodsApi.findByQuery(query);
    }

    /**
     * 领域视图列表转出参列表 (空集合而非 null)
     *
     * @param levelList 等级领域视图列表
     * @return 等级出参列表
     */
    private List<LevelRes> toResList(List<LevelVO> levelList) {
        if (CollUtil.isEmpty(levelList)) {
            return new ArrayList<>();
        }
        return TransferUtils.transfers(levelList, assembler::vo2Res);
    }
}
