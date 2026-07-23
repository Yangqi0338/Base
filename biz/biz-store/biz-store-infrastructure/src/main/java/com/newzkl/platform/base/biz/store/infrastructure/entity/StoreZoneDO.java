package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店专区领域对象
 */
@Data
@TableName("store_zone")
public class StoreZoneDO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 专区code
     */
    private String zoneCode;
    
    /**
     * 专区名称
     */
    private String zoneName;
    
    /**
     * 专区副标题
     */
    private String zoneSubtitle;
    
    /**
     * 商品个数
     */
    private Integer goodsNum;
    
    /**
     * 订单个数
     */
    private Integer orderNum;
    
    /**
     * 描述
     */
    private String zoneDescribe;
    
    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;
    
    /**
     * 创建人id
     */
    private Long createId;
    
    /**
     * 创建人名
     */
    private String createName;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}