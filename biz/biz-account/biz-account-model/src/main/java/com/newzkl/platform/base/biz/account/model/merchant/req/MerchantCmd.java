package com.newzkl.platform.base.biz.account.model.merchant.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 商户端入参集合
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.MerchantCmd}, 内部类逐一对应。
 * 旧 {@code javax.validation} 已按 Jakarta EE 9 迁为 {@code jakarta.validation};
 * 旧 {@code MerchantCmd.ID} 无端点使用, 未迁入。</p>
 *
 * @author KC
 */
public class MerchantCmd {

    /**
     * 商户ID列表入参
     */
    @Data
    public static class IDList implements Serializable {

        /**
         * 商户账号ID列表
         */
        @NotEmpty
        private List<Long> merchantIdList;
    }

    /**
     * 商户修改入参
     */
    @Data
    public static class Edit implements Serializable {

        /**
         * 商户账号ID
         */
        private Long id;

        /**
         * 商户可改字段
         */
        private MerchantReq merchantCommand;
    }

    /**
     * 微信公众号配置修改入参
     */
    @Data
    public static class EditWxMpConfigVO implements Serializable {

        /**
         * 微信公众号配置
         */
        private com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO wxMpConfigVO;
    }

    /**
     * 门店开通码入参
     */
    @Data
    public static class Cdk implements Serializable {

        /**
         * 兑换码
         */
        @NotNull(message = "兑换码Code?")
        private String cdk;

        /**
         * 兑换应用
         */
        @NotNull(message = "兑换应用?")
        private Integer serviceId;

        /**
         * 其他手机号
         */
        private Long openPhone;
    }

    /**
     * 门店订单支付入参
     */
    @Data
    public static class OrderPay implements Serializable {

        /**
         * 兑换码支付方式码值 (旧 {@code OrderEnum.PayType.CDK})
         */
        private static final Integer PAY_TYPE_CDK = 3;

        /**
         * 支付方式 (旧 {@code OrderEnum.PayType} 码值)
         */
        @NotNull(message = "支付方式不能为空")
        private Integer payType;

        /**
         * 兑换码
         */
        private String cdk;

        /**
         * 店铺信息
         */
        @NotNull(message = "店铺信息不能为空")
        @Valid
        private StoreInfo storeInfo;

        /**
         * 兑换码支付时兑换码必填校验
         *
         * <p>逐字沿用旧 {@code @AssertFalse} 校验语义: 返回 true 即校验失败。</p>
         *
         * @return 是否违反「兑换码支付必须带兑换码」
         */
        @AssertFalse(message = "兑换码不能为空")
        public boolean isCheckCdk() {
            return PAY_TYPE_CDK.equals(payType) && StrUtil.isBlank(cdk);
        }
    }

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
        @NotEmpty(message = "门店名称不能为空")
        private String storeName;

        /**
         * 门店分类
         */
        @NotNull(message = "门店分类不能为空")
        private Long storeType;

        /**
         * 门店地址
         */
        @NotEmpty(message = "门店地址不能为空")
        private String address;

        /**
         * 负责人
         */
        @NotEmpty(message = "负责人不能为空")
        private String contactName;

        /**
         * 联系方式
         */
        @NotEmpty(message = "联系方式不能为空")
        private String contactPhone;
    }
}
