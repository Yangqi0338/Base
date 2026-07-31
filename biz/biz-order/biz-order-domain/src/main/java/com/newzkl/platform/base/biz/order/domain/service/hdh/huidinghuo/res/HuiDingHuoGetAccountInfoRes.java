package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 获取账户信息响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetAccountInfoRes extends HuiDingHuoBaseRes<HuiDingHuoGetAccountInfoRes.AccountData> {

    /**
     * 账户数据类
     */
    @Data
    public static class AccountData {
        /**
         * 余额（必填）
         */
        private BigDecimal balance;
    }
}