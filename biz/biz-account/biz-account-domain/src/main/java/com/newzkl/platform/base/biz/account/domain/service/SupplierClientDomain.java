package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.req.SupplierCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierAuditRes;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.CompanyInfoVO;
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

    /**
     * 提交供应商审核
     *
     * <p>供应商注册后补充企业资料提交审核: 写入企业信息 JSON 并置审核状态为待审核 (AUDITING)。
     * 内联替代原 roleApply 审批流的快照发起, 审核数据直接落在 supplier 主数据。</p>
     *
     * @param accountId   供应商账号ID (与 supplier 主键同值)
     * @param companyInfo 企业资质信息
     */
    void supplierSubmitAudit(Long accountId, CompanyInfoVO companyInfo);

    /**
     * 供应商审核通过
     *
     * <p>从 supplier 主数据读取已提交的企业信息 JSON, 解析经营行业与企业名, 置状态为已入驻 (NORMAL)、
     * 审核状态为通过 (SUCCESS) 并写入入驻时间。内联替代原审批流终态 MQ 回调。</p>
     *
     * @param accountId 供应商账号ID
     */
    void supplierAuditPass(Long accountId);

    void auditFail(Long accountId, String lastRefuseReason);

    void promiseFlowSubmitAuditSuccess(Long accountId);

    /**
     * 保证金审核通过后回写供应商主数据
     *
     * <p>内联替代原审批流终态 MQ 回调 {@code promisePayAuditSuccess}: 幂等保护
     * (已缴纳则直接返回), 置供应商为已入驻、保证金已缴、保证金审核通过并写入实缴金额。
     * 保证金流水与余额充值由 finance 域负责, 本方法只改 account 侧主数据</p>
     *
     * @param accountId        供应商账号ID
     * @param promisePayAmount 审核通过的实缴保证金金额
     */
    void promisePayAuditSuccess(Long accountId, Money promisePayAmount);

    void promisePayAuditFail(Long accountId, String lastRefuseReason);

    Integer limitAmount(Long accountId);

    Page<SupplierRes> supplierPage(SupplierQuery supplierQuery);

    /**
     * 供应商审核分页
     *
     * <p>审核列表专用瘦出参: 仅透出审核决策必需字段, companyInfo 为
     * {@link CompanyInfoVO} 结构对象, 去掉销量/结算等分析类冗余</p>
     *
     * @param supplierQuery 供应商查询条件
     * @return 审核出参分页
     */
    Page<SupplierAuditRes> supplierAuditPage(SupplierQuery supplierQuery);

    /**
     * 供应商审核详情
     *
     * <p>审核详情专用瘦出参, 字段口径同 {@link #supplierAuditPage}</p>
     *
     * @param supplierId 供应商账号主键
     * @return 审核出参
     */
    SupplierAuditRes supplierAuditDetail(Long supplierId);

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
