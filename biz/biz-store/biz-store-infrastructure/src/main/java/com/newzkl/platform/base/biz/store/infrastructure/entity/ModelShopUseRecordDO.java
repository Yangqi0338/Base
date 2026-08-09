package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 样板店使用记录
 * @ext DB 表 model_shop_use_record
 */
@Data
@TableName
public class ModelShopUseRecordDO extends BaseDO {
    
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
}