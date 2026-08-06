package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 发货单
 * @author fang
 */
@Data
public class DeliverVO extends BaseRes {
     /**
     * 主键
     */
     private Long id;
     /**
     * SPU订单号
     */
     private Long spuOrderId;
     /**
     * 发货人账号
     */
     private String deliverUsername;
     /**
      * 物流公司名称
      */
     private String expressCompanyName;
     /**
     * 物流单号
     */
     private String expressNo;
     private String expressMobile;
     /**
      * 发货明细
      */
     private String item;
     private String outOrderNo;
     private Long channelId;
}