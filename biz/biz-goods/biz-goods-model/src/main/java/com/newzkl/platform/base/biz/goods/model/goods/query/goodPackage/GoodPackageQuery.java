package com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品-套餐分页查询。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodPackageQuery extends BizPageQuery {

    /**
     * 套餐业务编码。
     */
    private String packageId;

    /**
     * 套餐名称 (模糊)。
     */
    private String packageName;

    /**
     * 状态: 1 启用, 0 停用。
     */
    private Integer state;
}
