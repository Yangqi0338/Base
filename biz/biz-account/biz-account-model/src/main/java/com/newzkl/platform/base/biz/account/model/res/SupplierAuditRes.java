package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.biz.account.model.vo.CompanyInfoVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商审核出参
 *
 * <p>供应商审核列表/详情专用瘦出参, 仅透出审核决策必需字段, 去掉 {@code SupplierRes}/{@code SupplierVO}
 * 的销量/结算/收货地址等分析类冗余。id 与 createTime 继承自 {@link BaseRes};
 * companyInfo 为 {@link CompanyInfoVO} 结构对象; 金额字段为 {@link Money},
 * 经全局 Jackson 序列化器输出元为单位两位小数字符串</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierAuditRes extends BaseRes {

    /**
     * 账号名称
     */
    private String username;

    /**
     * 角色身份
     */
    private AccountEnum.Identity identity;

    /**
     * 状态
     */
    private SupplierEnum.State state;

    /**
     * 审批状态 (0待用户提交/1待审核/2通过/3未通过)
     */
    private AuditEnum.State auditState;

    /**
     * 企业资质信息
     */
    private CompanyInfoVO companyInfo;

    /**
     * 是否缴纳保证金
     */
    private CommonEnum.YesOrNo promisePayState;

    /**
     * 保证金实缴金额 (元字符串两位小数)
     */
    private Money promisePayAmount;

    /**
     * 保证金审批状态 (0待用户提交/1待审核/2通过/3未通过)
     */
    private AuditEnum.State promisePayAuditState;

    /**
     * 保证金缴纳配置
     */
    private SupplierEnum.PromisePayConfig promisePayConfig;

    /**
     * 是否设置账期
     */
    private CommonEnum.YesOrNo periodSetState;

    /**
     * 账期配置 JSON
     */
    private SettlementConfigVO periodSetConfig;

    /**
     * 应付保证金金额 (元字符串两位小数)
     */
    private Money shouldPromisePayAmount;

    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;

    /**
     * 行业 ID 集合
     */
    private String industryIdList;

    /**
     * 上级甄选师 ID
     */
    private Long inviteId;
}
