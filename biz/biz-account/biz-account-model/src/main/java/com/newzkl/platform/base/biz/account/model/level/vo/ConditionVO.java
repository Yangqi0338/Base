package com.newzkl.platform.base.biz.account.model.level.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 等级升级条件值对象 (DB 以 JSON 列存储)
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.model.vo.ConditionVO}。
 * 旧类的 amount / goods / team / teamDirect / teamNoDirect 五类条件属等级升级计算引擎,
 * 其调用方在分润/升级链 (slug 11), 本切片仅迁 controller 三端点所需的 pack 条件。</p>
 *
 * @author KC
 */
@Data
public class ConditionVO implements Serializable {

    /**
     * 条件有效性类型: 0 满足任意一项 1 全部满足
     */
    private Integer conditionJudgeType;

    /**
     * 入会礼包条件
     */
    private PackCondition pack;
}
