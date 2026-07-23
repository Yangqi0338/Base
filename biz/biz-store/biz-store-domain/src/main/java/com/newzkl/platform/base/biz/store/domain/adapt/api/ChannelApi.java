package com.newzkl.platform.base.biz.store.domain.adapt.api;

import java.util.List;

/**
 * 渠道域跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IChannelFacade}。</p>
 *
 * @author KC
 */
public interface ChannelApi {

    /**
     * 编辑渠道联系人信息。
     *
     * @param reqList 联系人更新入参列表
     */
    void editContact(List<ChannelContactReq> reqList);
}
