package com.newzkl.platform.base.biz.order.model.enums.finance;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author niu
 * @description: 财务枚举
 * @date 2023/12/18 10:04
 */
public class PayEnum implements Serializable {

    @AllArgsConstructor
    @Getter
    public enum HuiFuTradeType {
        /**
         * 微信公众号
         */
        T_JSAPI("微信公众号"),
        /** 微信小程序 */
        T_MINIAPP("微信小程序"),
        /** 支付宝 */
        A_JSAPI("支付宝"),
        /** 支付宝正扫 */
        A_NATIVE("支付宝正扫"),
        /** 银联二维码正扫 */
        U_NATIVE("银联二维码正扫"),
        /** 银联二维码 */
        U_JSAPI("银联二维码"),
        /** 微信直连H5支付 */
        T_H5("微信直连H5支付"),
        /** 微信APP支付 */
        T_APP("微信APP支付"),
        /** 微信正扫交易 */
        T_NATIVE("微信正扫交易"),
        /** 数字人民币正扫 */
        D_NATIVE("数字人民币正扫"),
        ;

        private final String desc;
    }

    @AllArgsConstructor
    @Getter
    public enum HuiFuResState {
        /** 成功 */
        S("成功"),
        /** 处理中 */
        P("处理中"),
        /** 失败 */
        F("失败"),
        ;

        private final String desc;
    }
}
