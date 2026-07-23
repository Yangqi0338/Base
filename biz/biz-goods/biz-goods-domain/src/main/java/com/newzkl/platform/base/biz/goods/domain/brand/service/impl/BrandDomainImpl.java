package com.newzkl.platform.base.biz.goods.domain.brand.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.brand.repository.BrandRepository;
import com.newzkl.platform.base.biz.goods.domain.brand.service.BrandDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.BrandPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.BrandReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品牌
 *
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class BrandDomainImpl implements BrandDomain {

    private final BrandRepository brandRepository;

    @Override
    public Long brandSave(BrandReq brandReq) {
        if (brandReq.getId() == null) {
            brandRepository.brandSave(brandReq);
        } else {
            brandRepository.brandEdit(brandReq);
        }
        return brandReq.getId();
    }

    @Override
    public Integer brandCount(BrandPageQuery brandQuery) {
        return brandRepository.brandCount(brandQuery);
    }

    @Override
    public void brandDelete(List<Long> idList) {
        brandRepository.brandDelete(idList);
    }

    @Override
    public BrandVO brandById(Long id) {
        return brandRepository.brandById(id);
    }

    @Override
    public Page<BrandVO> brandPage(BrandPageQuery brandQuery) {
        return brandRepository.brandPage(brandQuery);
    }
}
