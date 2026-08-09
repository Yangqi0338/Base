package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品-套餐数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class GoodPackageDO extends BaseDO {

    /**
     * 套餐业务编码
     * @ext 唯一
     */
    private String packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 商品席位数
     */
    private Long goodsNum;

    /**
     * 套餐价格
     */
    private Money packagePrice;

    /**
     * 套餐描述
     */
    private String packageDesc;

    /**
     * 状态
     * @ext 1 启用，0 停用
     */
    private Integer state;
}
