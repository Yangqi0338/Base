package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 席位套餐领域对象
 */
@Data
@TableName("seat_package")
public class SeatPackageDO {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 席位套餐code
     */
    private String seatPackageCode;
    
    /**
     * 席位套餐名称
     */
    private String seatPackageName;
    
    /**
     * 席位个数
     */
    private Integer seatNum;
    
    /**
     * 套餐价格 (Money, 落库 BIGINT 分)
     */
    private Money packagePrice;
    
    /**
     * 描述
     */
    private String packageDescribe;
    
    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;
    
    /**
     * 状态：0 正常,1 已删除
     */
    private Integer deleted;
    
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