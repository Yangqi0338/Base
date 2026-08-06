package com.newzkl.platform.base.biz.store.domain.adapt.api;
import com.newzkl.platform.base.common.ddd.facade.ChannelStoreVO;

import java.util.List;

/**
 * 账户域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IAccountFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface AccountApi {

    /**
     * 按昵称模糊查询会员ID列表
     *
     * @param nickname 昵称
     * @return 会员ID列表, 无则空集合
     */
    List<Long> queryMember(String nickname);

    /**
     * 按会员ID列表批量查询会员分组信息
     *
     * @param accountIdList 会员ID列表
     * @return 会员分组信息列表, 无则空集合
     */
    List<AccountGroupInfo> queryMemberByAccountIdList(List<Long> accountIdList);

    /**
     * 查询单个账户基础信息
     *
     * @param accountId 账户ID
     * @return 账户基础信息, 无则 null
     */
    AccountBaseInfo accountInfo(Long accountId);

    /**
     * 查询渠道商门店联系信息
     *
     * @param accountId 账户ID
     * @return 渠道门店信息, 无则 null
     */
    ChannelStoreVO channelStoreInfo(Long accountId);
}
