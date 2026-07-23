package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;


import lombok.Data;

/**
 * @author niu
 * @description: 汇付接口返回结果
 * @date 2025-08-23 11:40:51
 */
@Data
public class HuiFuAsyncRes extends HuiFuBaseRes {

    private String sign;

    private String data;
}
