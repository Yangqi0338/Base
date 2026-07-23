package com.newzkl.platform.base.biz.user.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.LevelRepository;
import com.newzkl.platform.base.biz.user.domain.service.LevelDomain;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.req.LevelQuery;
import com.newzkl.platform.base.biz.user.model.relation.req.LevelReq;
import com.newzkl.platform.base.biz.user.model.relation.res.LevelDTO;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.PackCondition;
import com.newzkl.platform.base.biz.user.model.relation.vo.ConditionVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.LevelVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.PermissionVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 等级领域服务实现。
 *
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class LevelDomainImpl implements LevelDomain {

    private final LevelRepository levelRepository;

    @Override
    public Long save(LevelReq levelCommand) {
        LevelDTO level = TransferUtils.transfer(levelCommand, LevelDTO::new);
        level.setValue(1);

        // 保存礼包 只有这一个修改入口, 改为必填
        ConditionVO condition = level.getCondition();
        PermissionVO permission = level.getPermission();
        if (condition == null || condition.getPack() == null || !permission.getDirectConfig().checkActiveDirectPack()) {
            throw new ScmException(BaseErrorCode.PARAM);
        }

        PackCondition pack = condition.getPack();
        // TODO[facade] 源码此处经 packGoodsFacade 保存礼包商品(finance/goods 域)，未迁移，暂略。
        return levelRepository.save(level);
    }

    @Override
    public void update(LevelReq levelCommand) {
        if (levelCommand.getId() == null) {
            throw new ScmException(BaseErrorCode.NODATA);
        }

        save(levelCommand);
    }

    @Override
    public List<LevelVO> pageList(LevelQuery levelQuery) {
        // TODO[page-meta] 源码返回空 Page；降级为查询列表返回。
        return levelRepository.calList(levelQuery);
    }

    @Override
    public Pair<LevelVO, Double> executeLevel(ConditionReq conditionCommand) {
        // 获取所有等级
        LevelQuery levelQuery = new LevelQuery();
        levelQuery.setType(conditionCommand.getRole());
        levelQuery.addDescSortField("value");
        List<LevelVO> levelList = levelRepository.calList(levelQuery);
        // 执行等级判断
        LevelVO levelVO = null;
        double progress = 0.0;
        for (LevelVO level : levelList) {
            progress = level.isMeet(conditionCommand);
            if (progress < 100) {
                break;
            } else {
                levelVO = level;
            }
        }
        return Pair.of(levelVO, progress);
    }

    @Override
    public List<LevelDTO> list(LevelQuery levelQuery) {
        return levelRepository.list(levelQuery);
    }

    @Override
    public LevelDTO findByQuery(LevelQuery levelQuery) {
        levelQuery.setPageSize(1);

        List<LevelDTO> list = levelRepository.list(levelQuery);

        return CollUtil.getFirst(list);
    }
}
