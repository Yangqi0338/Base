package com.newzkl.platform.base.biz.order.model.vo;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.json.JSONUtil;

import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/811:46
 */
@Data
public class SpuOrderAggVO {
    /**
     * SPU订单
     */
    private SpuOrderVO spuOrderVO;
    /**
     * sku订单
     */
    private List<SkuOrderVO> skuOrderList;

    public void doDesensitized() {
        String phone = DesensitizedUtil.mobilePhone(spuOrderVO.getShipPhone());
        spuOrderVO.setShipPhone(phone);
        ShipVO shipVO = JSONUtil.toBean(spuOrderVO.getShipVO(), ShipVO.class);
        if (shipVO != null) {
            shipVO.setShipPhone(phone);
            spuOrderVO.setShipVO(JSONUtil.toJsonStr(shipVO));
        }
        spuOrderVO.setChannelName(PatternUtil.desensitized(spuOrderVO.getChannelName(), 3, 2));
    }
}
