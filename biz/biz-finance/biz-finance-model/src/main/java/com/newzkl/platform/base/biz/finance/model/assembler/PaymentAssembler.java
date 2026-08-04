package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", uses = BaseConvert.class)
public interface PaymentAssembler extends BaseAssembler<OrderPayReq, PaymentVO> {

    @Override
    @Mappings({
            @Mapping(target = "goodsAmount", source = "orderAmount")
    })
    PaymentVO req2VO(OrderPayReq req);

    TradeOrderInfoRes vo2TradeRes(PaymentVO paymentDO);

    List<TradeOrderInfoRes> do2TradeRes(List<PaymentVO> list);

    default Integer getByCode(EarningsEnum.ConsumeType consumeType) {
        if (consumeType == null) return null;
        return consumeType.getType();
    }
}