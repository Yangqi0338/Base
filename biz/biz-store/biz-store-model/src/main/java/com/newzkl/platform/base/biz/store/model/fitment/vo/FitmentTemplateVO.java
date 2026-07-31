package com.newzkl.platform.base.biz.store.model.fitment.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 装修模板
 * @date 2024/3/29 14:34
 */
@Data
public class FitmentTemplateVO {

    /** 主键ID */
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
     * 是否默认 0：否  1：是
     */
    private Integer defaultTemplate;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 运营商名称
     */
    private String operatorName;

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
     * 渠道id 0：平台配置  >1：渠道商装修
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
     * 创建时间
     */
    private LocalDateTime createTime;

}
