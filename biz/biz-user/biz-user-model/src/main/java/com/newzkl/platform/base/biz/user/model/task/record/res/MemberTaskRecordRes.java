package com.newzkl.platform.base.biz.user.model.task.record.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员任务进度出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.memberTaskRecord.model.vo.MemberTaskRecordVO}。
 * id/createTime/updateTime 由 {@code BaseRes} 提供。</p>
 *
 * <p>迁移说明：旧 VO 的 {@code redPacketRewardYuan} 由实体充血方法
 * {@code AmountConvertUtil.storageToYuan} 换算，中台贫血模型下由本类 setter 换算
 * （存储值单位 0.0001 元，1 元 = 10000）。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberTaskRecordRes extends BaseRes {

    /**
     * 存储值换算比例（1元 = 10000）
     */
    private static final BigDecimal REWARD_SCALE = new BigDecimal("10000");

    /**
     * 会员ID
     */
    private String memberId;

    /**
     * 会员昵称
     */
    private String memberNickname;

    /**
     * 任务编号
     */
    private String taskNum;

    /**
     * 任务配置ID
     */
    private Long taskConfigId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型编码
     */
    private Integer taskType;

    /**
     * 任务类型描述
     */
    private String taskTypeDesc;

    /**
     * 任务完成次数或金额
     */
    private Long count;

    /**
     * 任务状态编码
     */
    private Integer taskStatus;

    /**
     * 任务状态描述
     */
    private String taskStatusDesc;

    /**
     * 完成条件
     */
    private String completeCondition;

    /**
     * 任务进度
     */
    private String taskProgress;

    /**
     * 红包奖励（元）
     */
    private BigDecimal redPacketRewardYuan;

    /**
     * 设置红包奖励存储值并同步换算为元
     *
     * @param redPacketReward 红包奖励存储值（单位 0.0001 元）
     */
    public void setRedPacketReward(Long redPacketReward) {
        this.redPacketRewardYuan = redPacketReward == null || redPacketReward == 0L
                ? BigDecimal.ZERO
                : new BigDecimal(redPacketReward).divide(REWARD_SCALE, 2, java.math.RoundingMode.HALF_UP);
    }
}
