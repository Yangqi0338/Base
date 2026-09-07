package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;

/**
 * 用户商品收藏持久化对象
 *
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class UserCollectionDO extends BaseDO {

    /**
     * 用户ID
     */
    @Index
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 门店ID
     */
    @Index
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 铺货表ID
     */
    @Index
    @OldColumnName("store_distribution_id")
    private Long storeGoodsId;

    /**
     * SPU ID
     */
    @Index
    private Long spuId;

    /**
     * SPU 名称
     * @ext 快照
     */
    private String spuName;

    /**
     * SKU ID
     */
    @Index
    private Long skuId;

    /**
     * SKU 名称
     * @ext 快照
     */
    private String skuName;

    /**
     * 商品价格
     * @ext 快照
     */
    private Money price;

    /**
     * 商品主图URL
     * @ext 快照
     */
    private String mainImage;

    /**
     * 是否有效
     */
    private CommonEnum.YesOrNo isValid;

}
