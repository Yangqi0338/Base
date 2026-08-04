package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.ChannelFacade;
import com.newzkl.platform.base.biz.account.facade.model.ChannelRpcQuery;
import com.newzkl.platform.base.biz.finance.facade.AccountConfigFacade;
import com.newzkl.platform.base.biz.finance.facade.model.ChannelConfigRes;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.order.model.dto.ChannelDTO;
import com.newzkl.platform.base.common.ddd.facade.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChannelApiImpl implements ChannelApi {

    @DubboReference
    private ChannelFacade channelFacade;

    @DubboReference
    private AccountConfigFacade accountFinanceConfigFacade;

    @Override
    public List<ChannelDTO> channelList(List<Long> idList, String channelName) {
        ChannelRpcQuery channelRpcQuery = new ChannelRpcQuery();
        channelRpcQuery.setChannelName(channelName);
        channelRpcQuery.setIdList(idList);
        channelRpcQuery.setState(ChannelEnum.State.OPEN);
        return TransferUtils.transfers(channelFacade.channelList(channelRpcQuery), ChannelDTO.class);
    }

    @Override
    public EarningsConfigRpcVO channelEarningsConfig(Long channelId) {
        return null;
    }

    @Override
    public ChannelNowServiceFeeRes queryNowServiceFee(Long channelId) {
        ChannelConfigRes channelConfigRes = accountFinanceConfigFacade.queryChannelConfig(channelId);
        return new ChannelNowServiceFeeRes(channelId, channelConfigRes.getPlatformNowValue(), channelConfigRes.getOperatorNowValue());
    }
}
