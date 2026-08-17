package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 门店专区领域对象
 */
@Data
@TableName
public class StoreZoneDO extends BaseDO {
    /**
     * 专区code
     */
    @Index
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
     * 描述
     */
    private String zoneDescribe;
    
    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;
}