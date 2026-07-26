package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 收货地址持久化对象。
 *
 * <p>对应旧表 {@code ship_address} (旧 {@code com.zkl.scm.user.infrastructure.entity.ShipAddressDO})。
 * id / createTime / updateTime / delFlag / executor 由 {@link BaseDO} 提供。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ship_address")
public class ShipAddressDO extends BaseDO {

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
    @Index
    private Long roleId;

    /**
     * 账号 ID
     */
    @Index
    private Long accountId;
}
