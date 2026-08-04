package com.newzkl.platform.base.biz.finance.model.pay.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品席位购买记录导出
 *
 * <p>迁移自 new-scm {@code finance.domain.pay.model.vo.GoodsSeatPurchaseRecordExportVO}, Excel 表头逐字保留</p>
 *
 * @author KC
 */
@Data
public class GoodsSeatPurchaseRecordExportVO {

    /**
     * 订单号
     */
    @ExcelProperty("订单号")
    private Long orderNo;

    /**
     * 支付金额
     */
    @ExcelProperty("支付金额")
    private String payAmount;

    /**
     * 购买数量
     */
    @ExcelProperty("购买数量")
    private Integer purchaseNum;

    /**
     * 支付方式
     *
     * @see OrderEnum.PayType
     */
    @ExcelProperty("支付方式")
    private String payType;

    /**
     * 支付状态
     *
     * @see OrderEnum.State
     */
    @ExcelProperty("支付状态")
    private String payState;

    /**
     * 支付时间
     */
    @ExcelProperty("支付时间")
    private LocalDateTime createTime;

}
