package com.newzkl.platform.base.biz.socialbang.model.event.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 活动其他配置
 * @Author: niu
 * @Date: 2024/1/5 16:43
 */
@Data
public class ActivityOtherConfigVO implements Serializable {

    /**
     * 显示配置（页面展示 如：奖金池商品配置《数组[商品id，商品标题，商品图片]》）
     */
    private String showConfig;

    /**
     * 详细配置（业务逻辑）
     */
    private String configDetails;
}
