package com.newzkl.platform.base.common.ddd.facade;


import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商
 * @author fang
 */
@Data
public class SupplierOutVO extends BaseRes {

     /**
     * 角色ID
     */
     private Long roleId;
     /**
     * 角色名称
     */
     private String roleName;
     /**
     * 账号名称 (查询)
     */
     private String username;
     /**
     * 名称 (查询)
     */
     private String name;
     /**
     * 状态 (查询)
     */
     private SupplierEnum.State state;
     /**
     * 审批状态 (查询) (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
     private AuditEnum.State auditState;
     /**
     * 企业信息
     */
     private String companyInfo;
     /**
     * 是否缴纳保证金 (查询)
     */
     private Integer promisePayState;
     /**
     * 保证金金额
     */
     private Integer promisePayAmount;
     /**
     * 保证金审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
     private Integer promisePayAuditState;
     /**
     * 是否设置账期 (查询)
     */
     private Integer periodSetState;
     /**
     * 账期配置JSON
     */
     private SettlementConfigVO periodSetConfig;
     /**
     * 应付保证金金额
     */
     private Integer shouldPromisePayAmount;
     /**
      * 保证金缴纳配置 promise_pay_config
      * 0 即时 1 延迟
      */
     private Integer promisePayConfig;
     /**
     * 主体类型 (查询)
     */
     private Integer bodyType;
     /**
     * 审批拒绝原因
     */
     private String auditRefuseReason;
     /**
     * 行业ID集合
     */
     private String industryIdList;
     /**
     * 上级甄选师ID
     */
     private Long inviteId;
     /**
     * 商品总数
     */
     private Integer goodsTotalCount;
     /**
     * 售卖中的商品
     */
     private Integer goodsOnSaleCount;
     /**
     * 总金额
     */
     private Integer goodsSaleAmount;
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
     /**
      * 适配: 企业名称
      */
     private String companyName;

     public String getCompanyName(){
          return this.name;
     }
}