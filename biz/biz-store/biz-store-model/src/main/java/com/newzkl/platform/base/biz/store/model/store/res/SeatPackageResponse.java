package com.newzkl.platform.base.biz.store.model.store.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 席位套餐领域对象
 */
@Data
public class SeatPackageResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
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
     * 套餐价格
     */
    private Integer packagePrice;
    
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