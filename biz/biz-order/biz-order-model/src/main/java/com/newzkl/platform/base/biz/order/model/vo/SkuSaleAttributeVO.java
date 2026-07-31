package com.newzkl.platform.base.biz.order.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * SKU 销售属性
 *
 * <p>迁移: 原跨域 {@code com.zkl.scm.goods.domain.spu.model.vo.SkuSaleAttributeVO} 降级为
 * order 本地 VO, 订单明细导出时解析 SKU 属性 JSON 用
 *
 * @author KC
 */
@Data
public class SkuSaleAttributeVO implements Serializable {

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性值
     */
    private String value;
}
