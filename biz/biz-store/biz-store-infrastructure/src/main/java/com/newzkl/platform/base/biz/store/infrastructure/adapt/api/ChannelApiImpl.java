package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.facade.ChannelFacade;
import com.newzkl.platform.base.biz.account.facade.model.ChannelOutVO;
import com.newzkl.platform.base.biz.account.facade.model.ChannelRpcQuery;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import com.newzkl.platform.base.biz.store.model.store.entity.ChannelVO;
import com.newzkl.platform.base.biz.sys.facade.IDictFacade;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@code ChannelApi} 默认兜底实现
 *
 * <p>TODO[cross-service]: 渠道域(user)跨域链延迟, 入口 starter 侧远程 consumer 覆盖。</p>
 *
 * @author KC
 */
@Component("storeChannelApi")
public class ChannelApiImpl implements ChannelApi {

    @RpcReference
    private ChannelFacade channelFacade;
    @RpcReference
    private IDictFacade dictFacade;

    @Override
    public void editContact(List<ChannelContactReq> reqList) {
        // TODO[cross-service]: 远程 user 渠道联系人更新, 默认空操作
    }

    @Override
    public ChannelVO detail(Long accountId) {
        ChannelRpcQuery query = new ChannelRpcQuery();
        query.setIdList(CollUtil.newArrayList(accountId));
        List<ChannelOutVO> channelList = channelFacade.channelList(query);
        ChannelOutVO channel = CollUtil.getFirst(channelList);
        return TransferUtils.transfer(channel, ChannelVO.class);
    }

    @Override
    public ChannelConfigVO getConfig() {
        String value = dictFacade.get(DictEnum.Key.CHANNEL_CONFIG.getCode());
        ChannelConfigVO channelConfigVO = JSONUtil.toBean(value, ChannelConfigVO.class);
        if (channelConfigVO == null) {
            // 字典未配置时给出 0 兜底: 调用方对金额阈值做拆箱比较, null 会直接 NPE
            channelConfigVO = new ChannelConfigVO();
            channelConfigVO.setMinimumRechargeAmount(Money.ZERO);
            channelConfigVO.setMinimumWithdrawalAmount(Money.ZERO);
            channelConfigVO.setMaximumDailyWithdrawalAmount(Money.ZERO);
            channelConfigVO.setWithdrawalFee(0);
        }
        return channelConfigVO;
    }
}
