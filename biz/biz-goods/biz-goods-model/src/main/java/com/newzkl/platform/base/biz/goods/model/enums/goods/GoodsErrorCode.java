package com.newzkl.platform.base.biz.goods.model.enums.goods;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品域错误码
 *
 * <p>迁移新增: 镜像 finance/order 域错误码约定 ({@code implements ErrorCode}), 收敛商品域
 * (spu/brand/freight/goodsZone/interaction/report) 通用错误码。域内既有 {@code SpuErrorCode}、
 * {@code AuditErrorCode} 保留其细分职责, 本枚举承载跨子域的通用商品错误。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum GoodsErrorCode implements ErrorCode {

    /** 商品不存在。 */
    GOODS_NOT_EXISTS(3001, "商品不存在"),
    /** SPU 不存在。 */
    SPU_NOT_EXISTS(3002, "SPU不存在"),
    /** SKU 不存在。 */
    SKU_NOT_EXISTS(3003, "SKU不存在"),
    /** 商品状态异常。 */
    GOODS_STATE_ERROR(3004, "商品状态异常"),
    /** 品牌不存在。 */
    BRAND_NOT_EXISTS(3005, "品牌不存在"),
    /** 运费模板不存在。 */
    FREIGHT_TEMPLATE_NOT_EXISTS(3006, "运费模板不存在"),
    /** 商品专区不存在。 */
    GOODS_ZONE_NOT_EXISTS(3007, "商品专区不存在"),
    /** 参数异常。 */
    PARAM_ERROR(3008, "参数异常:{}"),
    /** 操作失败。 */
    OPERATE_FAIL(3009, "操作失败:{}"),
    ;

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态码对应说明文案
     */
    private final String message;
}
