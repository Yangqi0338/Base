package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.store.domain.adapt.api.UserFollowApi;
import org.springframework.stereotype.Component;

/**
 * {@code UserFollowApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 用户关注域(user)跨域链延迟, 入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
@Component
public class UserFollowApiDefaultImpl implements UserFollowApi {

    @Override
    public Integer countFollower(Long channelId) {
        // TODO[cross-service]: 远程 user 粉丝统计, 默认 0
        return 0;
    }
}
