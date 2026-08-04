package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.PackGoodsRepository;
import com.newzkl.platform.base.biz.user.domain.service.PackGoodsDomain;
import com.newzkl.platform.base.biz.user.model.pack.query.PackGoodsQuery;
import com.newzkl.platform.base.biz.user.model.pack.req.PackGoodsCommand;
import com.newzkl.platform.base.biz.user.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 入会礼包商品领域服务实现
 *
 * <p>迁移自旧 {@code PackGoodsDomainServiceImpl}。</p>
 *
 * <p>迁移说明：</p>
 * <ul>
 *   <li>旧 {@code packGoodsSaveOrUpdate} 用 {@code saveOrUpdate}，中台按 {@code id} 空否显式分流
 *       {@code save}/{@code updateById}。</li>
 *   <li>{@code TODO[auth-defer]}：旧 {@code packGoodsVOList} 借 {@code RoleEnum.getNextEnumList}/
 *       {@code findClientRoleList} 按当前登录角色分流并回填 {@code canBuy}，Base {@code RoleEnum} 暂无
 *       等价方法，故 {@code canBuy} 暂置 null，待入口 starter 鉴权基建接入后补齐。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service("packGoodsDomainImpl")
@RequiredArgsConstructor
public class PackGoodsDomainImpl implements PackGoodsDomain {

    private final PackGoodsRepository packGoodsRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long packGoodsSave(PackGoodsCommand command) {
        PackGoodsRes res = TransferUtils.transfer(command, PackGoodsRes::new);
        if (command.getId() == null) {
            if (res.getState() == null) {
                res.setState(0);
            }
            PackGoodsRes saved = packGoodsRepository.save(res);
            log.info("新增入会礼包商品成功，ID：{}", saved.getId());
            return saved.getId();
        }
        if (packGoodsRepository.getById(command.getId()) == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "入会礼包商品");
        }
        PackGoodsRes updated = packGoodsRepository.updateById(res);
        log.info("修改入会礼包商品成功，ID：{}", updated.getId());
        return updated.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void packGoodsDelete(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM, "删除时ID集合不能为空");
        }
        packGoodsRepository.deleteBatch(idList);
        log.info("删除入会礼包商品成功，ID：{}", idList);
    }

    @Override
    public PackGoodsRes packGoodsVO(Long id) {
        PackGoodsRes res = packGoodsRepository.getById(id);
        if (res == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "入会礼包商品");
        }
        return res;
    }

    @Override
    public Page<PackGoodsRes> packGoodsVOList(PackGoodsQuery query) {
        // TODO[auth-defer]: 旧实现按登录角色分流并回填 canBuy，Base RoleEnum 暂无等价方法，canBuy 暂置 null
        return packGoodsRepository.pageQuery(query);
    }

    @Override
    public PackGoodsRes findByTypeAndLevel(Integer type, Integer level) {
        PackGoodsQuery query = new PackGoodsQuery();
        query.setType(type);
        query.setLevel(level);
        query.resetQuerySingle();
        List<PackGoodsRes> records = packGoodsRepository.pageQuery(query).getRecords();
        return records.isEmpty() ? null : records.get(0);
    }
}
