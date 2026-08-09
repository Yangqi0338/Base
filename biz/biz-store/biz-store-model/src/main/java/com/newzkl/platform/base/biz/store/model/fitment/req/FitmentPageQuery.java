package com.newzkl.platform.base.biz.store.model.fitment.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 装修分页查询
 *
 * @author niu
 * @date 2024/3/29 15:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FitmentPageQuery extends PageQuery {

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 样板店id
     */
    private Long modelShopId;
}
