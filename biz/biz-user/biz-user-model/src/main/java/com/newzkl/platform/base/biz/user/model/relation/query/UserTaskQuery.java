package com.newzkl.platform.base.biz.user.model.relation.query;

import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

/**
 * 用户任务(UserTask)查询类
 *
 * @author kc
 */
@Data
public class UserTaskQuery extends BizPageQuery {
    /**
     * 任务类型
     */
    private EarningsEnum.ConsumeType type;

    /**
     * 序号
     */
    private Integer seq;

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
