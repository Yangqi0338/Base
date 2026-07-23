package com.newzkl.platform.base.biz.goods.model.goods.query.brand;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 品牌
* @author fang
*/
@Data
public class BrandPageQuery extends PageQuery {
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 品牌名称
     */
    private String name;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 状态
     * @see com.newzkl.platform.base.biz.goods.model.enums.AuditEnum.ApprovalStatus
     */
    private String state;
}
