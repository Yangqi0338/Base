package com.newzkl.platform.base.biz.account.application.provider;

import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.facade.SupplierFacade;
import com.newzkl.platform.base.biz.account.facade.model.SupplierRpcQuery;
import com.newzkl.platform.base.biz.account.infrastructure.dao.SupplierDAO;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;
import lombok.Setter;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/2615:03
 */
@Component
@DubboService
@Setter(onMethod_ = @Autowired)
public class SupplierFacadeImpl implements SupplierFacade {

    @Autowired
    private SupplierClientDomain supplierDomain;
    @Autowired
    private UserQueryService supplierQueryAppService;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierDAO supplierDAO;

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
        List<SettlementConfigOutVO> settlementConfigOutVOList = TransferUtils.transfers(supplierVOList, supplierVO -> {
            SettlementConfigOutVO settlementConfigOutVO = JSONObject.parseObject(supplierVO.getPeriodSetConfig(), SettlementConfigOutVO.class);
            settlementConfigOutVO.setId(supplierVO.getId());
            return settlementConfigOutVO;
        });
        return settlementConfigOutVOList;
    }
}
