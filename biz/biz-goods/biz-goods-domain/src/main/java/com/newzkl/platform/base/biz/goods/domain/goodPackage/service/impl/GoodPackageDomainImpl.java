package com.newzkl.platform.base.biz.goods.domain.goodPackage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.goodPackage.adapt.repository.GoodPackageRepository;
import com.newzkl.platform.base.biz.goods.domain.goodPackage.service.GoodPackageDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品套餐领域服务实现。
 *
 * <p>迁移说明: new-scm 该链路为坏桩 (仓储 save/findAll/findByState 直接返回 null,
 * mapper 未注入), 本实现按 Base 范式重写, 并把旧 {@code RuntimeException} 换为
 * {@link ScmException}。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class GoodPackageDomainImpl implements GoodPackageDomain {

    /**
     * 状态: 启用。
     */
    private static final Integer STATE_ENABLED = 1;

    /**
     * 状态: 停用。
     */
    private static final Integer STATE_DISABLED = 0;

    private final GoodPackageRepository goodPackageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(GoodPackageReq req) {
        if (goodPackageRepository.existsByPackageId(req.getPackageId())) {
            throw new ScmException(400, "套餐ID已存在");
        }
        if (req.getState() == null) {
            req.setState(STATE_ENABLED);
        }
        return goodPackageRepository.save(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GoodPackageReq req) {
        requireExists(req.getId());
        goodPackageRepository.save(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        requireExists(id);
        goodPackageRepository.updateState(id, STATE_ENABLED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        requireExists(id);
        goodPackageRepository.updateState(id, STATE_DISABLED);
    }

    @Override
    public GoodPackageVO detailByPackageId(String packageId) {
        GoodPackageVO vo = goodPackageRepository.findByPackageId(packageId);
        if (vo == null) {
            throw new ScmException(400, "套餐不存在");
        }
        return vo;
    }

    @Override
    public Page<GoodPackageVO> page(GoodPackageQuery query) {
        return goodPackageRepository.page(query);
    }

    /**
     * 校验套餐存在, 不存在抛业务异常。
     *
     * @param id 套餐主键 ID
     */
    private void requireExists(Long id) {
        if (goodPackageRepository.findById(id) == null) {
            throw new ScmException(400, "套餐不存在");
        }
    }
}
