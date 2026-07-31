package com.newzkl.platform.base.biz.order.infrastructure.assembler;

import com.alibaba.fastjson.JSONObject;

import com.newzkl.platform.base.biz.order.model.vo.DeliverItemVO;
import org.springframework.stereotype.Component;
import java.util.List;

/**
* 发货单
* @author fang
*/
@Component
public class DeliverConvert {
    public List<DeliverItemVO> itemConvert(String json){
        return JSONObject.parseArray(json, DeliverItemVO.class);
    }
    public String itemConvert(List<DeliverItemVO> list){
        return JSONObject.toJSONString(list);
    }
}
