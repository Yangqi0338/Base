package com.newzkl.platform.base.biz.account.model.pack.res;

import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 入会礼包订单出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.rpc.model.vo.packorder.PackOrderVO}。
 * id/createTime/updateTime 由 {@code BaseRes} 提供。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PackOrderRes extends BaseRes {

    /**
     * 下单账号ID
     */
    private Long accountId;

    /**
     * 收货信息
     */
    private ShipAddressRes shipVO;

    /**
     * 订单金额（分）
     */
    private Money amount;

    /**
     * 物流公司
     */
    private String freightCompany;

    /**
     * 物流单号
     */
    private String freightCode;

    /**
     * 订单明细 JSON（结构：List&lt;PackGoodsRes&gt;）
     */
    private String packOrderItemList;

    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;

    /**
     * 订单状态
     */
    private OrderEnum.State state;

    /**
     * 礼包商品ID
     */
    private Long packId;

    /**
     * 礼包类型（角色 ID）
     */
    private Integer packType;

    /**
     * 礼包等级
     */
    private Integer packLevel;

    /**
     * 礼包名称
     */
    private String levelName;

    /**
     * 上次物流API调用时间
     */
    private LocalDateTime lastFreightApiUseTime;
}
