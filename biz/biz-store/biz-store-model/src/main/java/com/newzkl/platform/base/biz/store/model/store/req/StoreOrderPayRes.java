package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.ddd.facade.WxMiniPayResult;
import lombok.Data;

import java.io.Serializable;

/**
 * 门店订单支付结果
 *
 * <p>迁移: 旧契约返回 {@code com.zkl.scm.finance.rpc.model.res.PayBaseResult} 接口,
 * 其只暴露 {@code tradeNo} 与 {@code thirdTradeNo} 两个默认方法。
 * 中台化后为避免 biz-account 反向依赖 biz-finance 模型, 在端口侧就地定义等价出参。</p>
 *
 * @author KC
 */
@Data
public class StoreOrderPayRes extends WxMiniPayResult {

    /**
     * 平台交易号
     */
    private String tradeNo;

    /**
     * 第三方交易号
     */
    private String thirdTradeNo;
}
