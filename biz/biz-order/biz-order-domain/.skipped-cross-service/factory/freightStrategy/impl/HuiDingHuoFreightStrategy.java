package com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.zkl.scm.model.biz.req.huidinghuo.HuiDingHuoGetExpressFeeReq;
import com.zkl.scm.model.biz.res.huidinghuo.HuiDingHuoGetExpressFeeRes;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.FreightCalculateStrategy;
import com.newzkl.platform.base.biz.order.model.order.req.FreightCalculateReq;
import com.newzkl.platform.base.biz.order.model.order.res.FreightCalculateRes;
import java.math.BigDecimal;
import java.util.Collections;
import com.zkl.scm.util.biz.HuiDingHuoApiUtils;
/**
 * 会订货运费计算策略（模板ID=2）
 * @author sijiwang
 */
@Slf4j
@Component
public class HuiDingHuoFreightStrategy implements FreightCalculateStrategy {

    @Override
    public FreightCalculateRes calculate(FreightCalculateReq req) {
        log.info("开始计算会订货运费，商品ID：{}，收货地址：{}-{}-{}", 
                req.getGoodsId(), req.getShipProvinceCode(), req.getShipCityCode(), req.getShipAreaCode());
        
        HuiDingHuoGetExpressFeeReq feeReq = new HuiDingHuoGetExpressFeeReq();
        // 设置地址信息
        setAreaInfo(req, feeReq);
        
        HuiDingHuoGetExpressFeeReq.SkuItem skuItem = new HuiDingHuoGetExpressFeeReq.SkuItem(
                req.getOutSpuId(), req.getOutSkuId(), req.getChannelType(), req.getItemCode(), req.getNum()
        );
        feeReq.setSkuList(Collections.singletonList(skuItem));
        
        // 调用会订货API
        HuiDingHuoGetExpressFeeRes feeRes = HuiDingHuoApiUtils.getExpressFee(feeReq);
        Integer freightAmount = feeRes.getData().getExpAmount().multiply(new BigDecimal(100)).intValue();
        
        FreightCalculateRes res = new FreightCalculateRes();
        res.setGoodsId(req.getGoodsId());
        res.setFreightAmount(freightAmount);
        log.info("会订货运费计算完成，商品ID：{}，运费：{}分", req.getGoodsId(), freightAmount);
        return res;
    }

    @Override
    public Long getTemplateId() {
        return 2L;
    }

    // 地址信息设置（复用原有逻辑）
    private void setAreaInfo(FreightCalculateReq req, HuiDingHuoGetExpressFeeReq feeReq) {

       feeReq.setProvince(req.getShipProvinceName());
       feeReq.setCity(req.getShipCityName());
       feeReq.setDistrict(req.getShipAreaName());
       feeReq.setAddress(req.getShipDetailAddress());

    }
}