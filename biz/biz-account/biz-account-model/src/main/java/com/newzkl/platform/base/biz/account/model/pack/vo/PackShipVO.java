package com.newzkl.platform.base.biz.account.model.pack.vo;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入会礼包订单收货信息
 *
 * <p>收货地址主体随 {@code ShipAddressController} 整体并入订单域(biz-order), 而 biz-account 不依赖
 * biz-order, 故礼包侧另立本类承接 {@code pack_order.ship} JSON 列与 {@code PackOrderRes.shipVO} 出参。
 * 字段名与订单域 {@code ShipAddressVO} 逐一对齐, 已落库 JSON 与前端出参结构不变</p>
 *
 * <p>礼包下单链路 {@code PackOrderServiceImpl} 沿旧逻辑恒传空地址, 本类实际取值恒为 null,
 * 仅为保持列/出参结构存在</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PackShipVO extends BaseRes {

    /**
     * 收货地区, 例如: 辽宁省,沈阳市,铁西区,XXX镇
     */
    private String shipArea;

    /**
     * 收货联系人姓名
     */
    private String shipName;

    /**
     * 收货地址, 如创业路东
     */
    private String shipAddress;

    /**
     * 联系方式
     */
    private Long shipPhone;

    /**
     * 收货邮编
     */
    private String shipZipCode;

    /**
     * 收货地址编码, 省 CODE, 6 位
     */
    private Integer shipProvinceCode;

    /**
     * 收货地址编码, 市 CODE, 6 位
     */
    private Integer shipCityCode;

    /**
     * 收货地址编码, 区 CODE, 6 位
     */
    private Integer shipAreaCode;

    /**
     * 是否默认: 0 否 1 是
     */
    private Integer isDefault;

    /**
     * 角色类型 ID
     */
    private AccountEnum.Identity identity;

    /**
     * 账号 ID
     */
    private Long accountId;
}
