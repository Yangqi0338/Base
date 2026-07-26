package com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品-套餐视图对象。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodPackageVO extends BaseRes {

    /**
     * 套餐业务编码 (唯一)。
     */
    private String packageId;

    /**
     * 套餐名称。
     */
    private String packageName;

    /**
     * 商品席位数。
     */
    private Long goodsNum;

    /**
     * 套餐价格 (分)。
     */
    private Integer packagePrice;

    /**
     * 套餐描述。
     */
    private String packageDesc;

    /**
     * 状态: 1 启用, 0 停用。
     */
    private Integer state;
}
