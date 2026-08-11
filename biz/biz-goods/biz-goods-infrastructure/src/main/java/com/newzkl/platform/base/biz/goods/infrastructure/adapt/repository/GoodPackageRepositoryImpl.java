package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.goodPackage.adapt.repository.GoodPackageRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.GoodPackageDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodPackageDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 商品套餐仓储实现
 *
 * <p>迁移说明: new-scm 同名实现为坏桩 (save 返回 null 且 insert/update 被注释,
 * findAll/findByState 调 mapper 后丢结果返回 null, mapper 字段未 {@code @Autowired}),
 * 本类按 MyBatis-Plus 范式重写并补齐。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class GoodPackageRepositoryImpl implements GoodPackageRepository {

    private final GoodPackageDAO goodPackageDAO;

    @Override
    public Long save(GoodPackageReq req) {
        GoodPackageDO packageDO = TransferUtils.transfer(req, GoodPackageDO::new);
        if (packageDO.getId() == null) {
            goodPackageDAO.insert(packageDO);
        } else {
            goodPackageDAO.updateById(packageDO);
        }
        return packageDO.getId();
    }

    @Override
    public GoodPackageVO findById(Long id) {
        return TransferUtils.transfer(goodPackageDAO.selectById(id), GoodPackageVO::new);
    }

    @Override
    public GoodPackageVO findByPackageId(String packageId) {
        return TransferUtils.transfer(goodPackageDAO.selectOne(goodPackageDAO.getLwByPackageId(packageId)),
                GoodPackageVO::new);
    }

    @Override
    public boolean existsByPackageId(String packageId) {
        return goodPackageDAO.selectCount(goodPackageDAO.getLwByPackageId(packageId)) > 0;
    }

    @Override
    public void updateState(Long id, Integer state) {
        GoodPackageDO packageDO = new GoodPackageDO();
        packageDO.setId(id);
        packageDO.setState(state);
        goodPackageDAO.updateById(packageDO);
    }

    @Override
    public Page<GoodPackageVO> page(GoodPackageQuery query) {
        Page<GoodPackageDO> page = goodPackageDAO.selectPage(RepositorySupport.page(query),
                goodPackageDAO.getLw(query));
        return TransferUtils.transferPage(page, GoodPackageVO.class);
    }
}
