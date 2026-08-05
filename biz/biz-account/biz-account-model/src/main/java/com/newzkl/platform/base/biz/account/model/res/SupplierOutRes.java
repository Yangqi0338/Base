package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商
 * @author fang
 */
@Data
public class SupplierOutRes extends BaseRes {
     /**
     * 状态 (查询)
     */
     private SupplierEnum.State state;
     /**
     * 企业信息
     */
     private String companyInfo;
     /**
     * 是否缴纳保证金 (查询)
     */
     private CommonEnum.YesOrNo promisePayState;
     /**
     * 保证金金额 (Money, 落库 BIGINT 分)
     */
     private Money promisePayAmount;
     /**
     * 保证金审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
     private AuditEnum.State promisePayAuditState;
     /**
     * 是否设置账期 (查询)
     */
     private CommonEnum.YesOrNo periodSetState;
     /**
     * 账期配置JSON
     */
     private String periodSetConfig;
     /**
     * 应付保证金金额 (Money, 落库 BIGINT 分)
     */
     private Money shouldPromisePayAmount;
     /**
      * 保证金缴纳配置 promise_pay_config
      * 0 即时 1 延迟
      */
     private Integer promisePayConfig;
     /**
     * 主体类型 (查询)
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
     * 商品总数
     */
     private Integer goodsTotalCount;
     /**
     * 售卖中的商品
     */
     private Integer goodsOnSaleCount;
     /**
     * 总金额 (Money, 落库 BIGINT 分)
     */
     private Money goodsSaleAmount;
     /**
     * 总销量
     */
     private Integer goodsSaleCount;
     /**
     * 总成交销量
     */
     private Integer goodsDealCount;
     /**
     * 待售卖的商品
     */
     private Integer goodsNotSaleCount;
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
}