package com.newzkl.platform.base.biz.account.model.dto;

import com.newzkl.platform.base.biz.account.model.vo.CompanyInfoVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商纯净视图
 *
 * <p>字段与 {@code supplier} 表一一对应, 不含任何副数据。用于只需供应商自有列的场景
 * (校验入驻/审核状态、取账期与保证金配置等), 相比
 * {@link com.newzkl.platform.base.biz.account.model.res.SupplierRes} 少一次 account 查询</p>
 *
 * <p>枚举口径以 {@code SupplierDO} 为准。旧 {@code SupplierRes} 曾把 {@code state} /
 * {@code auditState} / {@code promisePayAuditState} 声明为 {@code Integer}, MapStruct 用
 * {@code ordinal()} 强转, 与枚举 code 不保证一致, 已随本类统一为枚举</p>
 *
 * @author KC
 * @ext 主数据 supplier (无副数据)
 */
@Data
public class SupplierDTO extends BaseRes {

    /**
     * 企业名称
     */
    private String name;
    /**
     * 入驻状态 (INIT 待完善 / AUDITING 审核中 / NORMAL 已入驻)
     */
    private SupplierEnum.State state;
    /**
     * 审批状态 (CUSTOM 待用户提交 / AUDITING 待审核 / SUCCESS 通过 / FAIL 未通过)
     */
    private AuditEnum.State auditState;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 企业区域
     */
    private String companyAreaCode;
    /**
     * 企业信息
     */
    private CompanyInfoVO companyInfo;
    /**
     * 是否缴纳保证金
     */
    private CommonEnum.YesOrNo promisePayState;
    /**
     * 实缴保证金金额
     */
    private Money promisePayAmount;
    /**
     * 保证金审批状态
     */
    private AuditEnum.State promisePayAuditState;
    /**
     * 保证金缴纳配置 (IMMEDIATE 即时 / DELAY 延迟)
     */
    private SupplierEnum.PromisePayConfig promisePayConfig;
    /**
     * 应付保证金金额
     */
    private Money shouldPromisePayAmount;
    /**
     * 是否设置账期
     */
    private CommonEnum.YesOrNo periodSetState;
    /**
     * 账期配置
     */
    private SettlementConfigVO periodSetConfig;
    /**
     * 行业ID集合, 逗号分隔
     */
    private String industryIdList;
    /**
     * 收货地址
     */
    private String receiveAddress;
    /**
     * 入驻时间
     */
    private LocalDateTime inTime;
}
