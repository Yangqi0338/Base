package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 装修模板
 */
@Data
public class FitmentTemplateDO implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板封面
     */
    private String templateLogo;

    /**
     * 默认模板
     */
    private Integer defaultTemplate;

    /**
     * 价格 (Money, 落库 BIGINT 分)
     */
    private Money price;

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 运营商名称
     */
    private String operatorName;

    /**
     * 渠道id 0：平台配置   >1：渠道商装修
     */
    private Long channelId;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 发布状态 0：否  1：是
     */
    private Integer state;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 页面导航
     */
    private String pageNavigation;

    /**
     * 页面风格
     */
    private String pageStyle;

    /**
     * 详情
     */
    private String details;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}