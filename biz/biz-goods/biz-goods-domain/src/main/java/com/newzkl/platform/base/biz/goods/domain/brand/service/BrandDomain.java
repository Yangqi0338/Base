package com.newzkl.platform.base.biz.goods.domain.brand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.BrandPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.BrandReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;

import java.util.List;

/**
 * 品牌
 *
 * @author fang
 */
public interface BrandDomain {
    /**
     * 品牌保存
     *
     * @param brandReq
     * @return
     */
    Long brandSave(BrandReq brandReq);

    /**
     * 品牌统计
     *
     * @param brandQuery
     * @return
     */
    Integer brandCount(BrandPageQuery brandQuery);

    /**
     * 品牌删除
     *
     * @param idList
     */
    void brandDelete(List<Long> idList);

    /**
     * 品牌详情
     *
     * @param id
     * @return
     */
    BrandVO brandById(Long id);

    /**
     * 分页
     *
     * @param brandQuery
     * @return
     */
    Page<BrandVO> brandPage(BrandPageQuery brandQuery);

}
