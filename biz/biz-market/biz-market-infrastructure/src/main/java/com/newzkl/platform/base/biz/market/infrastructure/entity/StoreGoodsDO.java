package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.DistributionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import java.time.LocalDateTime;

/**
 * @author 
 * 铺货列表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class StoreGoodsDO extends BaseDO {
    /**
     * 商品id
     */
    @Index
    private Long goodsId;
    /**
     * skuId
     */
    @Index
    private Long skuId;
    /**
     * 来源
     * @see DistributionEnum.Source
     */
    @Index
    private Long marketId;
    /**
     * 数据类型
     *
     * @ext 0：商品  1：sku
     */
    private Integer dataType;
    /**
     * 销售价
     *
     * @ext Money，落库 BIGINT 分
     */
    private Money sellPrice;
    /**
     * 销量
     */
    private Integer sellNum;
    /**
     * 门店id
     */
    @Index
    private Long storeId;
    /**
     * 商品状态
     */
    @Index
    private DistributionEnum.State goodsState;
    /**
     * 渠道商id
     */
    @Index
    private Long channelId;
    /**
     * 零售价
     *
     * @ext Money，落库 BIGINT 分
     */
    private Money unitPrice;
    /**
     * 供货价
     *
     * @ext Money，落库 BIGINT 分
     */
    private Money supplierPrice;
    /**
     * 商品信息
     */
    private String goodsInfo;
    /**
     * 是否需要更新
     *
     * @ext 0不需要，1需要
     */
    private Integer needUpdate;
    /**
     * 推荐时间
     */
    private LocalDateTime recommendationTime;
    /**
     * 上架时间
     */
    private LocalDateTime upTime;
}