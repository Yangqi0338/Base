package com.newzkl.platform.base.biz.order.model.vo;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.json.JSONUtil;

import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import lombok.Data;

import java.util.List;

/**
 * 交易单聚合(替 SpuOrderAggVO, SpuOrder 层折叠后)
 * @author KC
 */
@Data
public class OrderAggVO {
    /**
     * 交易单
     */
    private OrderVO orderVO;
    /**
     * sku 订单
     */
    private List<SkuOrderVO> skuOrderList;

    /**
     * 收货信息脱敏
     */
    public void doDesensitized() {
        String phone = DesensitizedUtil.mobilePhone(orderVO.getShipPhone());
        orderVO.setShipPhone(phone);
        ShipVO shipVO = JSONUtil.toBean(orderVO.getShipVO(), ShipVO.class);
        if (shipVO != null) {
            shipVO.setShipPhone(phone);
            orderVO.setShipVO(JSONUtil.toJsonStr(shipVO));
        }
        orderVO.setChannelName(PatternUtil.desensitized(orderVO.getChannelName(), 3, 2));
    }
}
