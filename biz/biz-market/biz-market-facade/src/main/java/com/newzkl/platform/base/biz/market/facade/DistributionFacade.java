package com.newzkl.platform.base.biz.market.facade;

import com.newzkl.platform.base.biz.market.facade.model.DistributionsRpcBatchUpdateReq;
import com.newzkl.platform.base.biz.market.facade.model.UpDownReq;

public interface DistributionFacade {
    void upDownEvent(UpDownReq event);
}
