package com.newzkl.platform.base.biz.order.model.dto;


import com.newzkl.platform.base.biz.order.model.req.DeliverCommand;
import com.newzkl.platform.base.biz.order.model.req.DeliverItemCommand;
import com.newzkl.platform.base.biz.order.model.vo.DeliverItemVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 发货实体
 * @date 2023/12/713:46
 */
@Data
public class Deliver {
    /**
     * 主键
     */
    private Long id;
    /**
     * SPU订单号
     */
    private Long spuOrderId;
    /**
     * 发货人账号
     */
    private String deliverUsername;
    /**
     * 物流公司名称
     */
    private String expressCompanyName;
    /**
     * 物流单号
     */
    private String expressNo;
    private String expressMobile;
    /**
     * 发货明细
     */
    private List<DeliverItemVO> item;

    public void init(DeliverCommand deliverCommand, SpuOrderVO spuOrderVO) {
        this.setId(SnowflakeIdAble.getSnowflakeId());
        this.setExpressCompanyName(deliverCommand.getExpressCompanyName());
        this.setExpressNo(deliverCommand.getExpressNo());
        this.setSpuOrderId(spuOrderVO.getId());
        this.setDeliverUsername(SecurityUtils.getUsername());
        this.item = new ArrayList<>();
        for (DeliverItemCommand deliverItemCommand : deliverCommand.getDeliverItemCommandList()) {
            DeliverItemVO deliverItem = new DeliverItemVO();
            deliverItem.setId(SnowflakeIdAble.getSnowflakeId());
            deliverItem.setSpuOrderId(spuOrderVO.getId());
            deliverItem.setDeliverId(this.getId());
            deliverItem.setSkuId(deliverItemCommand.getSkuId());
            deliverItem.setCount(deliverItemCommand.getCount());
            this.item.add(deliverItem);
        }
    }
}
