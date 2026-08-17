package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 门店订单支付入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.order.model.StoreOrderPayReq}。
 * 旧实现由控制器 {@code BeanUtil.copyProperties(MerchantCmd.OrderPay, StoreOrderPayReq.class)}
 * 拷贝后补 {@code accountId}, 此处沿用同名字段。</p>
 *
 * @author KC
 */
@Data
public class StoreOrderPayReq implements Serializable {

    /**
     * 支付方式 (旧 {@code OrderEnum.PayType} 码值)
     */
    private PaymentEnum.PayType payType;

    /**
     * 下单账号ID
     */
    @OauthUserId
    private Long accountId;

    /**
     * 门店信息
     */
    private StoreInfo storeInfo;

    /**
     * 门店信息
     *
     * <p>迁移自旧 {@code com.zkl.scm.user.domain.order.model.StoreInfo}。</p>
     */
    @Data
    public static class StoreInfo implements Serializable {

        /**
         * 门店名称
         */
        private String storeName;

        /**
         * 门店分类
         */
        private Long storeType;

        /**
         * 门店地址
         */
        private String address;

        /**
         * 负责人
         */
        private String contactName;

        /**
         * 联系方式
         */
        private String contactPhone;
    }
}
