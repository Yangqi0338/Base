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

    /**
     * 设置供应商账期
     *
     * <p>迁移自旧聚合方法 {@code Supplier#periodSet}: 置 {@code periodSetState=ON} 并写入账期配置,
     * 随后触发入驻判定 (账期已设置 + 保证金已缴或配置为延迟缴纳 ⇒ 状态置入驻)。</p>
     *
     * @param id              供应商账号ID
     * @param periodSetConfig 账期配置 JSON
     */
    void periodSet(Long id, String periodSetConfig);

    /**
     * 设置供应商应付保证金金额
     *
     * <p>迁移自旧聚合方法 {@code Supplier#shouldPromisePayAmountSet}: 写入应付保证金与缴纳配置,
     * 随后触发入驻判定, 判定条件与 {@link SupplierClientDomain#periodSet} 一致。</p>
     *
     * @param id                     供应商账号ID
     * @param shouldPromisePayAmount 应付保证金金额
     * @param promisePayConfig       保证金缴纳配置, 0 即时 1 延迟
     */
    void shouldPromisePayAmountSet(Long id, Integer shouldPromisePayAmount, Integer promisePayConfig);

    /**
     * 追加供应商经营行业
     *
     * <p>迁移自旧聚合方法 {@code Supplier#addIndustry}: 与已有行业并集去重后整列回写。
     * 保留旧角色分支 —— 供应商本人操作时行业数不得超过
     * {@code SupplierEnum.MAX_INDUSTRY_NUM}, 平台角色不限。</p>
     *
     * @param id             供应商账号ID
     * @param industryIdList 待追加的行业ID列表
     */
    void addIndustry(Long id, List<Long> industryIdList);
}
