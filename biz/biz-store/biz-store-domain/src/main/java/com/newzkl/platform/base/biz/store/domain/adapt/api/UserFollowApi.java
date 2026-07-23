package com.newzkl.platform.base.biz.store.domain.adapt.api;

/**
 * 用户关注域跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IUserFollowFacade}。</p>
 *
 * @author KC
 */
public interface UserFollowApi {

    /**
     * 统计指定渠道(店铺)的粉丝数。
     *
     * @param channelId 渠道ID
     * @return 粉丝数, 无则 0
     */
    Integer countFollower(Long channelId);
}
