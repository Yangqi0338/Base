package com.newzkl.platform.base.biz.store.model.store.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店专区背景图领域对象
 */
@Data
public class StoreZoneBackgroundImage {
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 专区code
     */
    private String zoneCode;
    
    /**
     * 图片链接
     */
    private String backgroundImage;
    
    /**
     * 状态：0 正常,1 已删除
     */
    private Integer deleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}