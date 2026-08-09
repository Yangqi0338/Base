package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户关注讲师数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class UserLecturerFollowDO extends BaseDO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 讲师ID
     * @ext 关联讲师表主键
     */
    private Long lecturerId;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;
}
