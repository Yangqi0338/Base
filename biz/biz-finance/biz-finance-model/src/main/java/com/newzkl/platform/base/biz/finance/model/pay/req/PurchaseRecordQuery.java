package com.newzkl.platform.base.biz.finance.model.pay.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 购买记录 #pay(PurchaseRecord)查询类
 *
 * @author kc
 * @since 2025-11-25 17:24:23
 */
@Data
public class PurchaseRecordQuery extends BizPageQuery {
    private static final long serialVersionUID = 208229929331831171L;

    /**
     * 购买单号
     */
    private String purchaseNo;

    /**
     * 购买类型
     */
    @NotNull(message = "类型不能为空")
    private PurseEnum.PurchaseRecordType type;

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 支付金额
     */
    private Money payAmount;

    /**
     * 支付方式
     */
    private Integer payMode;

    /**
     * 支付状态
     */
    private Integer payState;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 商品价格
     */
    private String[] goodsAmountRange;

    /**
     * 购买方式
     */
    private Integer buyMode;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

}

