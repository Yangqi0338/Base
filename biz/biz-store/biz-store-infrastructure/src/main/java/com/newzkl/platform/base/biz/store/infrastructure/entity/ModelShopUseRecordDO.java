package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 样板店使用记录实体类
 * 对应数据库表：model_shop_use_record
 */
@Data
@TableName("model_shop_use_record")
public class ModelShopUseRecordDO {
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 样板店ID
     */
    private Long modelShopId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}