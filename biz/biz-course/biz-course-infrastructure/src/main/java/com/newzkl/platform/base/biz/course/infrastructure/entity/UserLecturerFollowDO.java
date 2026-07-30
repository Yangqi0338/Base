package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户关注讲师数据对象
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.entity.UserLecturerFollowDO}
 * (表 {@code user_lecturer_follow})。偏离说明: 源以 {@code is_deleted} 表达关注状态
 * (0-已关注, 1-取消关注), Base 侧由 {@link BaseDO#getDelFlag} 承担
 * ({@code del_flag} 正常 0 表示已关注, NULL 表示已取关), 语义等价。
 * 源 {@code createBy}/{@code updateBy} 由 {@link BaseDO#getExecutor} 承担。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_lecturer_follow")
public class UserLecturerFollowDO extends BaseDO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 讲师ID, 关联讲师表主键
     */
    private Long lecturerId;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;
}
