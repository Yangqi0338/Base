package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.account.facade.SupplierFacade;
import com.newzkl.platform.base.biz.account.facade.model.SupplierRpcQuery;
import com.newzkl.platform.base.biz.order.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.biz.order.model.support.api.ReceiveAddressOutVO;
import com.newzkl.platform.base.biz.order.model.support.api.SupplierRefundVO;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;
import com.newzkl.platform.base.common.ddd.model.enums.SettleType;
import lombok.extern.slf4j.Slf4j;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("orderSupplierApi")
public class SupplierApiImpl implements SupplierApi {

    @RpcReference
    private SupplierFacade supplierFacade;

    @Override
    public SupplierOutVO getSupplierVO(Long accountId) {
        return supplierFacade.getSupplierVO(accountId);
    }

    @Override
    public List<SettlementConfigOutVO> settlementConfigBatch(List<Long> supplierIdList) {
        SupplierRpcQuery query = new SupplierRpcQuery();
        query.setIdList(supplierIdList);
        return supplierFacade.settlementConfigList(query);
    }

    @Override
    public SupplierRefundVO supplierRefundVO(Long supplierId) {
        SupplierRefundVO supplierRefundVO = new SupplierRefundVO();
        supplierRefundVO.setId(supplierId);
        SupplierOutVO supplierVO = getSupplierVO(supplierId);
        if(StrUtil.isNotEmpty(supplierVO.getReceiveAddress())){
            supplierRefundVO.setReceiveAddressVO(JSONObject.parseObject(supplierVO.getReceiveAddress(), ReceiveAddressOutVO.class));
        }
        return supplierRefundVO;
    }

    @Override
    public SettleType settleOrderType(Long supplierId) {
        SettlementConfigOutVO settlementConfigOutVO = CollUtil.getFirst(settlementConfigBatch(CollUtil.newArrayList(supplierId)));
        return settlementConfigOutVO == null ? null : settlementConfigOutVO.getOrderType();
    }

}
