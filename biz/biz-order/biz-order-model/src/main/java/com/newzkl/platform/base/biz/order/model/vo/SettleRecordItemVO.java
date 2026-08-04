package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.dto.Money;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 结算记录明细表
 * @author fang
 */
@Data
public class SettleRecordItemVO extends BaseVO {
     /**
     * ID
     */
     private Long id;
     /**
     * 结算记录ID
     */
     private Long settleRecordId;
     /**
     * SPU_ID
     */
     private Long spuId;
     /**
     * spu名称
     */
     private String spuName;
     /**
     * spu图片
     */
     private String spuImg;
     /**
     * sku订单结算信息, json数组字符串
     * 示例: [{"skuName":"[{\"name\":\"规格项\",\"value\":\"规格值\"}]","skuAmount":400,"skuNum":2}]
     */
     private String skuSettleDetail;
     /**
     * 结算金额
     */
     private Money settleMoney;
     /**
     * 结算商品数量
     */
     private Integer settleGoodsNum;
     /**
      * 结算运费
      */
     private Money spuFreight;
     /**
      * 下单时间
      */
     private LocalDateTime createOrderTime;
}