package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 本期奖金池参与记录
 *
 * <p>源为无独立 DAO/XML 引用的结果对象, 表名按 bonus_pool_now_partake 推定, 存疑
 *
 * @author niu
 */
@Data
@TableName("bonus_pool_now_partake")
public class BonusPoolNowPartakeDO {
    private Long id;
    private String serialId;
    private Long channelId;
    private String activityId;
    private String activityName;
    private String settlementId;
    private String settlementName;
    private String nickName;
    private Long accountId;
    private String accountName;
    private Long role;
    private String roleName;
    private Integer personPercent;
    private Integer dividendAmount;
    private String dividendCycle;
    private Long bonusPoolId;
    private Long memberId;
    private String orderSn;
    private Long skuCode;
    private Integer buyNum;
    private Integer bonus;
    private LocalDateTime createTime;
}
