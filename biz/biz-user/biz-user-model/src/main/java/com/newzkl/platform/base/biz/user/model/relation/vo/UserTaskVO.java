package com.newzkl.platform.base.biz.user.model.relation.vo;

import com.newzkl.platform.base.biz.user.model.enums.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户任务(UserTask)展示类。
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserTaskVO extends BaseRes {

    /**
     * 用户id
     */
    private Long accountId;

    /**
     * 任务类型
     */
    private EarningsEnum.ConsumeType type;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 比例
     */
    private Integer ratio;

    /**
     * 完成次数
     */
    private Integer count;

    /**
     * 外键ID
     */
    private Long foreignId;

    /**
     * 状态
     */
    private String status;

}
