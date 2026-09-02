package com.newzkl.platform.base.biz.account.application.provider;

import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.facade.SupplierFacade;
import com.newzkl.platform.base.biz.account.facade.model.SupplierRpcQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/2615:03
 */
@Component
public class SupplierFacadeImpl implements SupplierFacade {

    @Autowired
    private UserQueryService supplierQueryAppService;

    @Override
    public SupplierOutVO getSupplierVO(Long accountId) {
        SupplierVO supplierVO = supplierQueryAppService.supplierVO(accountId);
        return TransferUtils.transfer(supplierVO, SupplierOutVO.class);
    }

    @Override
    public List<SettlementConfigOutVO> settlementConfigList(SupplierRpcQuery query) {
        SupplierQuery supplierQuery = TransferUtils.transfer(query, SupplierQuery.class);
        supplierQuery.resetQueryList();
        List<SupplierRes> supplierVOList = supplierQueryAppService.supplierPage(supplierQuery).getRecords();
        return TransferUtils.transfers(supplierVOList, supplierVO -> {
            SettlementConfigOutVO settlementConfigOutVO = TransferUtils.transfer(supplierVO.getPeriodSetConfig(), SettlementConfigOutVO.class);
            settlementConfigOutVO.setId(supplierVO.getId());
            return settlementConfigOutVO;
        });
    }
}
