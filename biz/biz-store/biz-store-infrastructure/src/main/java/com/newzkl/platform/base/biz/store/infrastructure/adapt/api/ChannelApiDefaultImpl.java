package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link ChannelApi} 默认兜底实现。
 *
 * <p>TODO[cross-service]: 渠道域(user)跨域链延迟, 入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
@Component
public class ChannelApiDefaultImpl implements ChannelApi {

    @Override
    public void editContact(List<ChannelContactReq> reqList) {
        // TODO[cross-service]: 远程 user 渠道联系人更新, 默认空操作
    }
}
