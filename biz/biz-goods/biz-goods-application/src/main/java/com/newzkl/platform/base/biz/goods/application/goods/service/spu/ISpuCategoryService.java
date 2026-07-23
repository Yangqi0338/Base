package com.newzkl.platform.base.biz.goods.application.goods.service.spu;


import com.newzkl.platform.base.biz.goods.model.goods.query.brand.PalletCategoryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;

import java.util.List;

/**
 * @author muc_fang
 * @date 2023/11/317:50
 */
public interface ISpuCategoryService {

    /**
     * 分类绑定行业
     *
     * @param industryId
     * @param categoryId
     * @param bind
     */
    void bindIndustry(Long industryId, Long categoryId, boolean bind);

    /**
     * 查询行业下分类
     *
     * @param industryIdList
     * @return
     */
    List<Long> idListByIndustryId(List<Long> industryIdList);

    /**
     * 绑定品牌
     *
     * @param categoryId
     * @param brandId
     * @param isBind
     */
    void bindBrand(Long categoryId, Long brandId, boolean isBind);

    /**
     * Category分页
     */
    List<SpuCategoryVO> categoryList(SpuCategoryQuery categoryQuery);

    /**
     * app市场分类查询列表
     */
    List<SpuCategoryVO> appCategoryList(SpuCategoryQuery categoryQuery);

    /**
     * 货盘分类分页
     */
    List<SpuCategoryVO> palletCategoryList(PalletCategoryPageQuery categoryQuery);

}
