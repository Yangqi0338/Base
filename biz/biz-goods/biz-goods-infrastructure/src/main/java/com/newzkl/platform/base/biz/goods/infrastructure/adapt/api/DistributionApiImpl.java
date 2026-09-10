package com.newzkl.platform.base.biz.goods.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.goods.domain.adapt.api.DistributionApi;
import com.newzkl.platform.base.biz.market.facade.DistributionFacade;
import com.newzkl.platform.base.biz.market.facade.model.DistributionsRpcBatchUpdateReq;
import com.newzkl.platform.base.biz.market.facade.model.UpDownReq;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributionApiImpl implements DistributionApi {

    @RpcReference
    private DistributionFacade distributionFacade;

    @Override
    public void down(List<Long> spuIdList) {
        UpDownReq upDownReq = new UpDownReq();
        upDownReq.setSpuIdList(spuIdList);
        upDownReq.setEnable(SpuEnum.State.PLATFORM_DOWN);
        distributionFacade.upDownEvent(upDownReq);
    }
}
