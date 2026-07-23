package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.SupplierEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商
 *
 * @author fang
 */
@Data
@TableName
public class SupplierDO extends BaseDO {
    /**
     * 审批状态
     */
    private AuditEnum.State auditState;
    /**
     * 状态  0 未开通 1 已开通 2 已入驻
     */
    private SupplierEnum.State state;
    /**
     * 企业区域编码
     */
    private String companyAreaCode;
    /**
     * 企业信息
     */
    private String companyInfo;
    /**
     * 是否缴纳保证金
     */
    private CommonEnum.YesOrNo promisePayState;
    /**
     * 保证金金额
     */
    private Integer promisePayAmount;
    /**
     * 保证金审批状态
     */
    private AuditEnum.State promisePayAuditState;
    /**
     * 保证金缴纳配置 promise_pay_config
     * 0 即时 1 延迟
     */
    private Integer promisePayConfig;
    /**
     * 是否设置账期
     */
    private Integer periodSetState;
    /**
     * 账期配置JSON
     */
    private String periodSetConfig;
    /**
     * 应付保证金金额
     */
    private Integer shouldPromisePayAmount;
    /**
     * 主体类型
     */
    private AccountEnum.BodyType bodyType;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 行业ID集合
     */
    private String industryIdList;
    /**
     * 结算配置
     */
    private String settlementConfigVO;
    /**
     * 收货地址
     */
    private String receiveAddress;
    /**
     * 入驻时间
     */
    private LocalDateTime inTime;

    /* ------------------------- 冗余 ------------------------- */
    private String name;
}