package com.newzkl.platform.base.biz.store.infrastructure.adapt.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.account.facade.AccountFacade;
import com.newzkl.platform.base.biz.account.facade.ChannelFacade;
import com.newzkl.platform.base.biz.account.facade.model.ChannelOutVO;
import com.newzkl.platform.base.biz.account.facade.model.ChannelRpcQuery;
import com.newzkl.platform.base.biz.finance.facade.PayFacade;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.ChannelContactReq;
import com.newzkl.platform.base.biz.store.domain.adapt.api.PayApi;
import com.newzkl.platform.base.biz.store.model.store.entity.ChannelVO;
import com.newzkl.platform.base.biz.store.model.store.req.StoreOrderPayRes;
import com.newzkl.platform.base.biz.sys.facade.IDictFacade;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
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
@Component("storePayApi")
public class PayApiImpl implements PayApi {

    @RpcReference
    private PayFacade payFacade;
    @RpcReference
    private AccountFacade accountFacade;

    @Override
    public StoreOrderPayRes orderPay(OrderPayReq orderPayReq) {
        String wxOpenId = accountFacade.queryWxOpenId(orderPayReq.getAccountId(), orderPayReq.getIdentity());
        orderPayReq.setWxOpenId(wxOpenId);

        PayBaseResult payBaseResult = payFacade.orderPay(orderPayReq);
        return TransferUtils.transfer(payBaseResult, StoreOrderPayRes.class);
    }
}
