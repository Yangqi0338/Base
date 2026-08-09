package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动
 *
 * @author niu
 */
@Data
@TableName
public class ActivityDO extends BaseDO {
    /**
     * 活动id
     */
    private String activityId;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 客户端类型
     * @ext 1 平台, 2 渠道商
     */
    private Integer clientType;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 门槛类型
     * @ext 0 贡献值, 1 会员等级, 2 会员卡等级
     */
    private Integer conditionType;

    /**
     * 门槛值
     */
    private Integer conditionValue;

    /**
     * 复类型
     * @ext 0 单次, 1 周期
     */
    private Integer repeatType;

    /**
     * 时间点或周期值
     */
    private Long repeatValue;

    /**
     * 状态
     * @ext 0 关闭, 1 开启
     */
    private String state;

    /**
     * 其他配置
     */
    private Long otherConfig;

}
