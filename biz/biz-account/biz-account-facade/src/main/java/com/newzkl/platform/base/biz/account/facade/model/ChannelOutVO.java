package com.newzkl.platform.base.biz.account.facade.model;


import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 渠道商
 * @author fang
 */
@Data
public class ChannelOutVO extends BaseVO implements java.io.Serializable{
     /**
     * ID
     */
     private Long id;
     /**
     * 上级交易师ID
     */
     private Long upDealerId;
     /**
     * 上级运营商ID
     */
     private Long upOperatorId;
     /**
     * 角色ID
     */
     private Long roleId;
     /**
     * 主体类型 (查询)
     */
     private Integer bodyType;
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
     * 市场数量
     */
     private Integer marketCount;
     /**
      * 适配
      */
     private String channelName;

     public String getChannelName(){
          return this.name;
     }
}