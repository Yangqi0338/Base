package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.alibaba.fastjson.JSONObject;
import com.zkl.scm.sale.domain.refund.model.vo.FreightExt;
import com.zkl.scm.sale.domain.refund.model.vo.RefundItemVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 售后单
* @author fang
*/
@Component
public class RefundConvert {

    public List<RefundItemVO> refundItemVOConvert(String json){
        return JSONObject.parseArray(json, RefundItemVO.class);
    }
    public String refundItemVOConvert(List<RefundItemVO> list){
        return JSONObject.toJSONString(list);
    }

    public FreightExt freightExtConvert(String string){
        return JSONObject.parseObject(string, FreightExt.class);
    }

    public String freightExtConvert(FreightExt freightExt){
        if (freightExt == null){
            return null;
        }
        return JSONObject.toJSONString(freightExt);
    }
}
