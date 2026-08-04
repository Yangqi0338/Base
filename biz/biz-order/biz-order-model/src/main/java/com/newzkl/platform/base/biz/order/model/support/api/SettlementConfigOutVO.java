package com.newzkl.platform.base.biz.order.model.support.api;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 结算配置跨域 ACL 视图
 *
 * <p>迁移: 跨域 supplier 结构 {@code com.newzkl.platform.base.biz.account.model.vo.SettlementConfigOutVO}
 * 降级为 order 本地 ACL DTO。经 {@code IOrderRepository#settlementConfigBatch} 出站获取,
 * 供结算编排读取供应商结算周期配置, 避免 domain 直依赖 biz-account model</p>
 *
 * @author KC
 */
@Data
public class SettlementConfigOutVO implements Serializable {

    /** 主键ID */
    private Long id;

    /** 可结算节点 */
    private RoleEnum.OrderType orderType;

    /** 结算周期类型 */
    private Integer dataType;

    /** 结算周期天数 dataType = 0 */
    private String dataOne;

    /** 结算周期天数 dataType = 1 */
    private String dataTow;

    /** 订单结算类型2时, 此值表示订单完成后N天结算 */
    private Integer orderTypeDay;
}
