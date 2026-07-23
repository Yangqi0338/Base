package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.brand.repository.BrandRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.BrandDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.BrandDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.BrandPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.BrandReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 品牌
* @author fang
*/
@Repository
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandDAO brandDAO;

    @Override
    public void brandSave(BrandReq brand) {
    	brandDAO.insert(TransferUtils.transfer(brand, BrandDO::new));
    }

    @Override
    public void brandDelete(List<Long> idList) {
    	brandDAO.delete(new BaseLambdaQueryWrapper<BrandDO>().in(BrandDO::getId, idList));
    }

    @Override
    public void brandEdit(BrandReq brand) {
    	brandDAO.updateById(TransferUtils.transfer(brand, BrandDO::new));
    }

    @Override
    public BrandVO brandById(Long id) {
    	return TransferUtils.transfer(brandDAO.selectById(id), BrandVO::new);
    }

    @Override
    public Integer brandCount(BrandPageQuery brandQuery) {
        return brandDAO.selectCount(new BaseLambdaQueryWrapper<BrandDO>()
                .notEmptyIn(BrandDO::getId, brandQuery.getIdList())
                .notEmptyEq(BrandDO::getId, brandQuery.getId())
                .notEmptyLike(BrandDO::getName, brandQuery.getName())
        ).intValue();
    }

    @Override
    public Page<BrandVO> brandPage(BrandPageQuery brandQuery) {
        return brandDAO.queryPage(RepositorySupport.page(brandQuery), brandQuery);
    }
}
