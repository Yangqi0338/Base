package com.newzkl.platform.base.biz.user.model.relation.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户收藏创建请求
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.collection.model.req.UserCollectionCreateReq}。
 * 商品字段为收藏时刻快照, 由调用方带入, 不实时回查商品域。</p>
 *
 * @author KC
 */
@Data
public class UserCollectionCreateReq {

    /**
     * 用户ID
     * @ext 由控制器填充当前登录账号
     */
    private Long userId;

    /**
     * 用户名称
     * @ext 由控制器填充当前登录账号
     */
    private String userName;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 铺货表ID
     */
    @NotNull(message = "铺货表ID不能为空")
    private Long storeGoodsId;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * SPU 名称
     * @ext 快照
     */
    @NotEmpty(message = "SPU名称不能为空")
    private String spuName;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * SKU 名称
     * @ext 快照
     */
    private String skuName;

    /**
     * 商品价格快照
     * @ext Money, 入参 JSON 元 → Money, 落库 BIGINT 分
     */
    private Money price;

    /**
     * 商品主图URL
     * @ext 快照
     */
    @NotEmpty(message = "商品主图URL不能为空")
    private String mainImage;
}
