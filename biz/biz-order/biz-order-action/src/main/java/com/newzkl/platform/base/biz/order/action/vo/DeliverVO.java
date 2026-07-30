package com.newzkl.platform.base.biz.order.action.vo;

import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import lombok.Data;

/**
 * 发货单出参 (对齐旧 {@code scm-sale} 的 {@code DeliverVO} 字段名)
 *
 * <p>存在理由: Base 发货 DTO {@code OrderDelivery} 把物流三字段改成了
 * {@code logisticsName} / {@code logisticsNo} / {@code expressPhone},
 * 而 3 个前端仍按旧名 {@code expressCompanyName} / {@code expressNo} / {@code expressMobile} 取值:
 *
 * <ul>
 *   <li>{@code channel-admin/src/views/order/order-list/order-detail/index.vue:76,87}</li>
 *   <li>{@code gys-admin/src/views/gys/orders/order/detail.vue:88,91}</li>
 *   <li>{@code platform-admin/src/views/orders/order/detail.vue:70,73}</li>
 * </ul>
 *
 * <p>直接返回 {@code OrderDelivery} 会让这三处物流栏静默空白 —— 端点 200、无报错、值取不到,
 * 属最难排查的一类故障。故在 action 层做出参改名回落, 不动 model 层字段名
 * (model 的新名是 Base 库表口径, 与 {@code order_delivery} 列名一致, 不应为前端历史包袱回退)
 *
 * <p>入参侧无需处理: {@code DeliverCmd} 已按旧名 {@code expressCompanyName} 等收参
 *
 * @author KC
 */
@Data
public class DeliverVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * SPU 订单号 (旧口径为主键 Long, Base 改为业务单号 String)
     */
    private String spuOrderId;

    /**
     * 发货人账号 (对应 Base {@code operatorName})
     */
    private String deliverUsername;

    /**
     * 物流公司名称 (对应 Base {@code logisticsName})
     */
    private String expressCompanyName;

    /**
     * 物流单号 (对应 Base {@code logisticsNo})
     */
    private String expressNo;

    /**
     * 发货手机号 (对应 Base {@code expressPhone})
     */
    private String expressMobile;

    /**
     * 渠道商 ID
     */
    private Long channelId;

    /**
     * 发货单号 (Base 新增, 旧无对等字段)
     */
    private String deliveryNo;

    /**
     * 发货状态 1-待发货 2-已发货 3-已签收 4-拒收 (Base 新增)
     */
    private Integer deliveryStatus;

    /**
     * 由 Base 发货 DTO 转旧字段名出参
     *
     * @param delivery Base 发货主单 DTO
     * @return 旧字段名出参, 入参为 null 时返回 null
     */
    public static DeliverVO of(OrderDelivery delivery) {
        if (delivery == null) {
            return null;
        }
        DeliverVO vo = new DeliverVO();
        vo.setId(delivery.getId());
        vo.setSpuOrderId(delivery.getSpuOrderNo());
        vo.setDeliverUsername(delivery.getOperatorName());
        vo.setExpressCompanyName(delivery.getLogisticsName());
        vo.setExpressNo(delivery.getLogisticsNo());
        vo.setExpressMobile(delivery.getExpressPhone());
        vo.setChannelId(delivery.getChannelId());
        vo.setDeliveryNo(delivery.getDeliveryNo());
        vo.setDeliveryStatus(delivery.getDeliveryStatus());
        return vo;
    }
}
