package com.newzkl.platform.base.biz.goods.model.goods.query.brand;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 行业
* @author fang
*/
@Data
public class IndustryPageQuery extends PageQuery {

    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
    * 名称 查询
    */
    private String name;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * not行业ID
     */
    private Long notIndustryId;
    /**
     * not行业ID list
     */
    private List<Long> notIndustryIdList;

    /* 过滤渠道商 */
    private Integer operatorFilter;
}
