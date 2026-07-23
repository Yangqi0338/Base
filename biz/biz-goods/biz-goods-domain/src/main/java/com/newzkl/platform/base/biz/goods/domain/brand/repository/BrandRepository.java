package com.newzkl.platform.base.biz.goods.domain.brand.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.BrandPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.BrandReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;

import java.util.List;

/**
* 品牌
* @author fang
*/
public interface BrandRepository {

    void brandSave(BrandReq brandReq);

    void brandDelete(List<Long> idList);

    void brandEdit(BrandReq brandReq);

    BrandVO brandById(Long id);

    Integer brandCount(BrandPageQuery brandQuery);

    Page<BrandVO> brandPage(BrandPageQuery brandQuery);

}
