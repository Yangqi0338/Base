package com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.impl;

import com.zkl.scm.goods.rpc.model.freight.FreightTemplateRPCVO;
import com.zkl.scm.goods.rpc.model.freight.RegionRPCVO;
import com.newzkl.platform.base.biz.order.model.enums.RedisEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.FreightCalculateStrategy;
import com.newzkl.platform.base.biz.order.model.order.req.FreightCalculateReq;
import com.newzkl.platform.base.biz.order.model.order.res.FreightCalculateRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 自定义运费模板策略（通用模板）
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomFreightStrategy implements FreightCalculateStrategy {

//    @DubboReference
//    private IFreightFacade facadeFacade;

    @Override
    public FreightCalculateRes calculate(FreightCalculateReq req) {
        log.info("开始计算自定义模板运费，商品ID：{}，模板ID：{}", req.getGoodsId(), req.getFreightTemplateId());
        
        // 1. 获取运费模板（优先从Redis获取）
        String redisKey = RedisEnum.Key.FREIGHT_TEMPLATE.getCode(req.getFreightTemplateId().toString());
//        FreightTemplateRPCVO freightTemplate = RedisUtil.get(redisKey);
        FreightTemplateRPCVO freightTemplate = null;
//        if (Objects.isNull(freightTemplate)) {
//            freightTemplate = facadeFacade.freightTemplate(req.getFreightTemplateId());
//            RedisUtil.set(redisKey, freightTemplate);
//        }
        if (Objects.isNull(freightTemplate)) {
            throw new ScmException(BaseErrorCode.PARAM, "运费模板异常，模板ID：" + req.getFreightTemplateId());
        }

        // 2. 包邮处理
        if (freightTemplate.getFreePost() == 1) {
            FreightCalculateRes res = new FreightCalculateRes();
            res.setGoodsId(req.getGoodsId());
            res.setFreightAmount(0);
            log.info("商品ID：{} 包邮，运费为0", req.getGoodsId());
            return res;
        }

        // 3. 匹配配送区域
        RegionRPCVO regionVO = matchRegion(freightTemplate.getRegionSpec(), req);
        if (Objects.isNull(regionVO) || regionVO.getType() == 2) {
            throw new ScmException(BaseErrorCode.PARAM.getCode(), "商品不支持配送，SPU ID：" + req.getGoodsId());
        }

        // 4. 计算运费
        Integer freightAmount = calculate(freightTemplate.getPricingManner(), regionVO, req);
        FreightCalculateRes res = new FreightCalculateRes();
        res.setGoodsId(req.getGoodsId());
        res.setFreightAmount(freightAmount);
        log.info("自定义模板运费计算完成，商品ID：{}，运费：{}分", req.getGoodsId(), freightAmount);
        return res;
    }

    @Override
    public Long getTemplateId() {
        // 通用模板匹配所有非1/2的模板ID，由工厂类统一分发
        return -1L;
    }

    // 区域匹配（复用原有逻辑）
    private RegionRPCVO matchRegion(java.util.List<RegionRPCVO> regionSpec, FreightCalculateReq req) {
        for (RegionRPCVO regionVO : regionSpec) {
            if (Objects.nonNull(regionVO.getRegion()) && regionVO.getRegion().contains(req.getShipAreaCode())) {
                return regionVO;
            }
            if (Objects.nonNull(regionVO.getCity()) && regionVO.getCity().contains(req.getShipCityCode())) {
                return regionVO;
            }
            if (Objects.nonNull(regionVO.getProvince()) && regionVO.getProvince().contains(req.getShipProvinceCode())) {
                return regionVO;
            }
        }
        return null;
    }

    // 运费计算（复用原有逻辑）
    private int calculate(Integer pricingManner, RegionRPCVO regionVO, FreightCalculateReq req) {
        if (Objects.isNull(regionVO)) {
            return 0;
        }
        switch (pricingManner) {
            // 按件数
            case 1:
                return unifiedCalculate(regionVO, new BigDecimal(req.getNum()));
            // 按重量
            case 2:
                return unifiedCalculate(regionVO, req.getTotalWeight());
            // 按体积
            case 3:
                return unifiedCalculate(regionVO, req.getTotalVolume());
            // 数据异常
            default:
                throw new RuntimeException("计价方式异常，方式：" + pricingManner);
        }
    }

    // 统一计算逻辑（复用原有逻辑）
    private int unifiedCalculate(RegionRPCVO regionVO, BigDecimal count) {
        if (regionVO.getFirstPiece().compareTo(count) > -1) {
            return regionVO.getFirstAmount().intValue();
        } else {
            return regionVO.getFirstAmount().add(
                    (count.subtract(regionVO.getFirstPiece())).divide(regionVO.getSecondPiece(), 0, RoundingMode.UP)
                            .multiply(regionVO.getSecondAmount())
            ).intValue();
        }
    }
}