package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 奖品发放记录
 *
 * @author niu
 */
@Data
@TableName("award_record")
public class AwardRecordDO implements Serializable {
    /**
     * 记录id
     */
    private Long id;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 渠道商活动id
     */
    private Long channelActivityId;

    /**
     * 奖品状态 0:待发放  1：已发放
     */
    private Integer awardState;

    /**
     * 发放奖品方式「1:即时、2:定时、3:人工
     */
    private Integer grantType;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 奖品id
     */
    private Long awardId;

    /**
     * 奖品类型
     */
    private Integer awardType;

    /**
     * 扩展数据
     */
    private String extInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发放时间
     */
    private Date grantTime;

    private static final long serialVersionUID = 1L;
}
