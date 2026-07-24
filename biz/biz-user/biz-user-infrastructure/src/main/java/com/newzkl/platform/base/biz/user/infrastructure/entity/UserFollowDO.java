package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.TableIndex;
import org.dromara.autotable.annotation.TableIndexes;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

/**
 * 用户关注持久化对象。
 *
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
@TableIndexes({
        @TableIndex(name = "idx_key", type = IndexTypeEnum.UNIQUE, fields = {"follower_id", "following_id"}),
})
public class UserFollowDO extends BaseDO {

    /**
     * 关注者
     */
    private Long followerId;

    /**
     * 被关注者
     */
    private Long followingId;

}
