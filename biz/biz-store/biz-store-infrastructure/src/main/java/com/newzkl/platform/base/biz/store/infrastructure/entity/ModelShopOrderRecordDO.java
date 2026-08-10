package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.ModeShopOrderType;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 样板店订单记录
 * @ext DB 表 model_shop_order_record
 */
@Data
@TableName
public class ModelShopOrderRecordDO extends BaseDO {
    
    /**
     * 门店id
     */
    @Index
    private Long storeId;

    /**
     * 样板店ID
     */
    @Index
    private Long modelShopId;
    
    /**
     * 订单类型
     */
    private ModeShopOrderType type;

    /**
     * 金额
     */
    private Money amount;

}