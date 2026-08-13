package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.PackOrderApi;

import com.newzkl.platform.base.biz.account.facade.PackOrderFacade;
import com.newzkl.platform.base.biz.account.facade.model.PackOrderFacadeDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.PackOrderRpcVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackOrderApiImpl implements PackOrderApi {

    @Autowired
    private PackOrderFacade packOrderFacade;

    @Override
    public PackOrderRpcVO packOrderVO(Long orderNo) {
        PackOrderFacadeDTO packOrderFacadeDTO = packOrderFacade.packOrderVO(orderNo);
        return TransferUtils.transfer(packOrderFacadeDTO,PackOrderRpcVO.class);
    }

    @Override
    public void paySuccess(Long orderNo) {
        packOrderFacade.paySuccess(orderNo);
    }
}
