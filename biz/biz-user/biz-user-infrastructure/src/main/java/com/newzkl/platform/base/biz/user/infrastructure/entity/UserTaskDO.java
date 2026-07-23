package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.enums.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 用户任务(user_task)持久化对象。
 *
 * <p>迁移说明：源 scm-user 被 import 但无实体文件，据 UserTaskRes 字段补建以保编译。
 * TODO[future-common] 待确认真实表结构后校准字段。</p>
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class UserTaskDO extends BaseDO {

    /**
     * 用户id
     */
    @Index
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
    private Double ratio;

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
