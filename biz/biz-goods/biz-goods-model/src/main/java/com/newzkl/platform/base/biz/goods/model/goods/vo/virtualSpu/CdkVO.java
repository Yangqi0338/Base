package com.newzkl.platform.base.biz.goods.model.goods.vo.virtualSpu;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 兑换码
 * @author fang
 */
@Data
public class CdkVO extends BaseVO {
     /**
      * 兑换码密钥
      */
     private String value;
     /**
      * 兑换状态 0 未兑换 1 已兑换
      */
     private Integer useState;
     /**
      * 获取方式 0 发放 1 购买
      */
     private Integer getType;
     /**
      * 分配状态 0 未分配 1 运营商已分配 2 交易师已分配  3 平台已分配
      */
     private Integer toState;
     /**
      * 兑换时间
      */
     private LocalDateTime useTime;
     /**
      * 使用者ID
      */
     private Long useId;
     /**
      * 归属人角色
      */
     private Long belowRole;
     /**
      * 运营商ID
      */
     private Long operatorId;
     /**
      * 交易师ID
      */
     private Long dealerId;
     /**
      * 渠道商ID
      */
     private Long channelId;
     /**
      * 发放给渠道商的时间
      */
     private LocalDateTime toOperatorTime;
     /**
      * 发放给交易师的时间
      */
     private LocalDateTime toDealerTime;
     /**
      * 发放给渠道商的时间
      */
     private LocalDateTime toChannelTime;
     /**
      * 渠道商描述
      */
     private String useDesc;
     /**
      * 系统类型
      *         COMPOSE(-1,"组合"),
      *         STORE(0,"数字门店"),
      *         XM(2,"厦门"),
      *         MK(3,"美康"),
      */
     private Integer systemType;
     /**
      * 使用类型 0 用户使用 1 平台使用
      */
     private Integer useType;
     /**
      * 订单ID
      */
     private Long orderId;
     /**
      * 关联应用ID
      */
     private Integer refAppId;
}