package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 门店专区商品关系领域对象
 */
@Data
@TableName
public class StoreZoneGoodsRelationDO extends BaseDO {
    /**
     * 专区code
     */
    @Index
    private String zoneCode;
    
    /**
     * 商品id
     */
    @Index
    private Long goodsId;

    /**
     * 门店id
     */
    @Index
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;
}