package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 门店专区背景图领域对象
 */
@Data
@TableName
public class StoreZoneBackgroundImageDO extends BaseDO {
    /**
     * 专区code
     */
    @Index
    private String zoneCode;

    /**
     * 图片链接
     */
    private String backgroundImage;
}