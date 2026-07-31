package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.alibaba.fastjson.JSONObject;
import com.zkl.scm.sale.domain.order.model.vo.ShipVO;
import com.zkl.scm.sale.domain.order.model.vo.SpuOrderExt;
import org.springframework.stereotype.Component;
/**
* SPU订单
* @author fang
*/
@Component
public class SpuOrderConvert {
    public ShipVO shipVOConvert(String string){
        return JSONObject.parseObject(string, ShipVO.class);
    }
    public String shipVOConvert(ShipVO object){
        if(object == null){
            return null;
        }
        return JSONObject.toJSONString(object);
    }

    public SpuOrderExt spuOrderExtConvert(String string){
        return JSONObject.parseObject(string, SpuOrderExt.class);
    }
    public String spuOrderExtConvert(SpuOrderExt object){
        if(object == null){
            return null;
        }
        return JSONObject.toJSONString(object);
    }
}
