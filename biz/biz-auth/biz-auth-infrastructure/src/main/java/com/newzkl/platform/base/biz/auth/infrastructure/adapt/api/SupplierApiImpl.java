package com.newzkl.platform.base.biz.auth.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.SupplierFacade;
import com.newzkl.platform.base.biz.auth.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * {@link SupplierApi} 出站实现, 经 {@link SupplierFacade} 跨域取供应商记录
 *
 * @author KC
 */
@Slf4j
@Service
public class SupplierApiImpl implements SupplierApi {

    @RpcReference
    private SupplierFacade supplierFacade;

    @Override
    public SupplierOutVO supplier(Long accountId) {
        return supplierFacade.getSupplierVO(accountId);
    }
}
