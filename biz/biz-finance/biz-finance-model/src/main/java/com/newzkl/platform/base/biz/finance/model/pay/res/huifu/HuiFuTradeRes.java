package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

/**
 * @author niu
 * @description:
 * @date 2025-08-25 15:22:17
 */
@Data
public class HuiFuTradeRes extends HuiFuBaseRes implements PayBaseResult {

    /**
     * 请求日期 String(8) Y 格式为yyyyMMdd，示例值：20220925
     */
    private String req_date;

    /**
     * 请求流水号 String(128) Y 交易时传入，原样返回；示例值：rQ2021121311173944134649875651
     */
    private String req_seq_id;

    /**
     * 全局流水号 String(128) N 示例值：00470topo1A221019132207P068ac1362af00000
     */
    private String hf_seq_id;

    @Override
    public Long getTradeNo() {
        return NumberUtil.parseLong(this.getReq_seq_id());
    }

    @Override
    public String getThirdTradeNo() {
        return this.getHf_seq_id();
    }
}
