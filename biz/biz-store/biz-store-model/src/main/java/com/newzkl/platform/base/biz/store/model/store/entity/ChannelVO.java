package com.newzkl.platform.base.biz.store.model.store.entity;


import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 渠道商
 * @author fang
 */
@Data
public class ChannelVO extends BaseRes {
     /**
     * ID
     */
     private Long id;
     /**
     * 角色ID
     */
     private AccountEnum.Identity identity;
     /**
     * 状态 (查询)
     */
     private Integer state;
     /**
     * 登录名称(手机号) (查询)
     */
     private String username;
     /**
     * 角色名称
     */
     private String roleName;
     /**
     * 渠道商名称 (查询) channel_name
     */
     private String name;
     /**
     * 头像
     */
     private String headImg;
     /**
     * 企业信息
     */
     private String companyInfo;
     /**
     * 实名认证信息
     */
     private String nameAuthVO;
     /**
     * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
     private Integer auditState;
     /**
     * 审批拒绝原因
     */
     private String auditRefuseReason;
     /**
     * 服务费配置
     */
     private String serviceFeeConfigVO;
     /**
     * 交易师收益
     */
     private Integer dealerEarnings;
     /**
      * 适配
      */
     private String channelName;
     private CommonEnum.YesOrNo storePermission;

     public String getChannelName(){
          return this.name;
     }
}