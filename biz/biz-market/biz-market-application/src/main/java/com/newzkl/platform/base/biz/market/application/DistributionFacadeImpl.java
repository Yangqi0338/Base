package com.newzkl.platform.base.biz.market.application;

import com.newzkl.platform.base.biz.market.domain.distribution.DistributionDomain;
import com.newzkl.platform.base.biz.market.facade.DistributionFacade;
import com.newzkl.platform.base.biz.market.facade.model.DistributionsRpcBatchUpdateReq;
import com.newzkl.platform.base.biz.market.facade.model.UpDownReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DistributionFacadeImpl implements DistributionFacade {

    private final DistributionDomain distributionDomain;

    @Override
    public void upDownEvent(UpDownReq req) {
        distributionDomain.upDownEvent(req);
    }
}
