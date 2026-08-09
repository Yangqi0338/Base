package com.newzkl.platform.base.biz.finance.model.earnings.req;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.GoodsInfoVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

/**
 * 带交易的分润执行请求
 *
 * @author niu
 * @date 2023/12/18 16:55
 */
@Data
public class EarningsPaymentExecReq extends EarningsExecReq {

    /**
     * 邀请人
     */
    private Long invitedId;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 订单分润来源
     */
    private PurseEnum.FinanceUser source;
    /**
     * 渠道商总支付金额
     */
    private Money totalAmount;
    /**
     * 服务费
     */
    private Money serviceAmount;
    /**
     * 商品金额
     */
    private Money goodsAmount;
    /**
     * 铺货价格
     */
    private Money storeAmount;
    /**
     * 货款金额
     */
    private Money supplierAmount;

    /**
     * 商品信息
     */
    private GoodsInfoVO goodsInfoVO;

    /**
     * 最大分润金额
     */
    private Integer maxEarningsPrecent;

    /**
     * 待分润金额
     */
    @Override
    public Money getAmount() {
        if (source == PurseEnum.FinanceUser.SUPPLIER) {
            return this.getSupplierAmount();
        } else {
            return this.getStoreAmount().subtract(this.getGoodsAmount());
        }
    }

    /**
     * 贡献人
     */
    private String contributeName;

    /**
     * 贡献人id
     */
    @JsonIgnore
    public Long getContributeId() {
        if (source == PurseEnum.FinanceUser.SUPPLIER) {
            return this.getSupplierId();
        } else {
            return this.getChannelId();
        }
    }

    @Override
    public Long getId() {
        return orderNo;
    }
}
