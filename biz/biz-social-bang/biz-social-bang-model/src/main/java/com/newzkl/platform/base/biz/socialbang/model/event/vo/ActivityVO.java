package com.newzkl.platform.base.biz.socialbang.model.event.vo;

import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 活动信息配置
 * @Author: niu
 * @Date: 2024/1/9 17:22
 */
@Data
public class ActivityVO {

    private Long id;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;


    /**
     * 客户端类型 1：平台  2：渠道商
     */
    private Integer clientType;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 门槛类型 0：贡献值  1：会员等级  2：会员卡等级
     */
    private Integer conditionType;

    /**
     * 值
     */
    private Integer conditionValue;

    /**
     * 重复类型 0：单次  1：周期  2：不限时间，单次结束后重新开始
     */
    private Integer repeatType;

    /**
     * 时间点或周期值
     */
    private Long repeatValue;

    /**
     * 其他配置
     */
    private Long otherConfigId;

    /**
     * 状态 {@link ActivityEnum.ExecuteState}
     */
    private String state;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
