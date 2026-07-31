package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.alibaba.fastjson.JSONObject;
import com.zkl.scm.sale.domain.order.model.vo.OrderSnapVO;
import com.zkl.scm.sale.domain.order.model.vo.ShipVO;
import org.springframework.stereotype.Component;

/**
* 订单
* @author fang
*/
@Component
public class OrderConvert {
    public ShipVO shipVOConvert(String string){
        return JSONObject.parseObject(string, ShipVO.class);
    }
    public String shipVOConvert(ShipVO object){
        if(object == null){
            return null;
        }
        return JSONObject.toJSONString(object);
    }
    public OrderSnapVO orderSnapVOConvert(String string){
        return JSONObject.parseObject(string, OrderSnapVO.class);
    }
    public String orderSnapVOConvert(OrderSnapVO object){
        if(object == null){
            return null;
        }
        return JSONObject.toJSONString(object);
    }
}
