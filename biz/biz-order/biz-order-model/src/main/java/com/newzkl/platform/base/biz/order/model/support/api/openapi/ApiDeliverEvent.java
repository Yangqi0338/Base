package com.newzkl.platform.base.biz.order.model.support.api.openapi;

import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 发货通知事件
 *
 * <p>迁移自 new-scm scm-message-rpc {@code ApiDeliverEvent}, 序列化进 NotifyEventCommand.eventInfo;
 * SkuCountDTO 用本地 biz-order-model dto。</p>
 *
 * @author muc_fang
 */
@Data
@NoArgsConstructor
public class ApiDeliverEvent implements Serializable {

    /**
     * 外部订单号
     */
    private String outOrderNo;

    /**
     * SKU发货列表
     */
    private List<SkuCountDTO> skuDeliverList;

    /**
     * 物流公司名称
     */
    private String expressName;

    /**
     * 物流单号
     */
    private String expressNo;

    /**
     * 全参构造
     *
     * @param outOrderNo     外部订单号
     * @param skuDeliverList SKU发货列表
     * @param expressName    物流公司名称
     * @param expressNo      物流单号
     */
    public ApiDeliverEvent(String outOrderNo, List<SkuCountDTO> skuDeliverList, String expressName, String expressNo) {
        this.outOrderNo = outOrderNo;
        this.skuDeliverList = skuDeliverList;
        this.expressName = expressName;
        this.expressNo = expressNo;
    }
}
