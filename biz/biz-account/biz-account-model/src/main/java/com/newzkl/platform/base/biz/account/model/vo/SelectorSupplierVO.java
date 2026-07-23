package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;

import lombok.Data;

/**
 * 甄选师查看供应商视图
 * @author fang
 */
@Data
public class SelectorSupplierVO extends BaseRes {
     /**
     * ID
     */
     private Long id;
     /**
     * 角色ID
     */
     private RoleEnum.CompanyRole role;
     /**
     * 角色名称
     */
     private String roleName;
     /**
      * 主体类型
      *         COMPANY(0,"企业"),
      *         PERSON(1,"个人"),
      */
     private Integer bodyType;
     /**
      * 账号名称 (查询)
      */
     private String username;
     /**
      * 名称 (查询)
      */
     private String name;
     /**
      * 企业名称
      */
     private String companyName;
     /**
      * 状态 (查询)
      */
     private Integer state;
     /**
      * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
      */
     private Integer auditState;
     /**
      * 是否缴纳保证金 (查询)
      */
     private Integer promisePayState;
     /**
      * 保证金审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过",4,"终止")
      */
     private Integer promisePayAuditState;
     /**
      * 是否设置账期 (查询)
      */
     private Integer periodSetState;
     /**
      *商品总数
      */
     private Integer goodsTotalCount;
     /**
      *售卖中的商品
      */
     private Integer goodsOnSaleCount;
     /**
      *总金额
      */
     private Integer goodsSaleAmount;
     /**
      *总销量
      */
     private Integer goodsSaleCount;
     /**
      *总成交销量
      */
     private Integer goodsDealCount;
     /**
      * 实名认证
      */
     private String nameAuthInfo;
     /**
      * 联表:真实姓名
      */
     private String realName;
     /**
      * 角色ID集合
      */
     private String roleIdList;

     public String getCompanyName(){
          return this.name;
     }
}