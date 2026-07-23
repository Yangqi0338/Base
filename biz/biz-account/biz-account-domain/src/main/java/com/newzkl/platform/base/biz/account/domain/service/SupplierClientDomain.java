package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;

import java.util.List;

/**
 * 供应商
 *
 * @author fang
 */
public interface SupplierClientDomain {

    Long supplierCustomSave(SupplierCustomSaveReq req);

    int supplierEdit(Long id, SupplierReq supplierEditReq);

    int supplierDelete(List<Long> supplierIdList);

    void supplierEdit(List<EditColumnVO> editColumnList, Long id);

    SupplierVO supplier(Long supplierId);

    void auditPass(AccountVO accountVO, String companyInfo);

    void auditFail(Long accountId, String lastRefuseReason);

    void promiseFlowSubmitAuditSuccess(Long accountId);

//    void promisePayAuditSuccess(PromiseFlowVO promiseFlowVO);

    void promisePayAuditFail(Long accountId, String lastRefuseReason);

    Integer limitAmount(Long accountId);

    Page<SupplierRes> supplierPage(SupplierQuery supplierQuery);
}
