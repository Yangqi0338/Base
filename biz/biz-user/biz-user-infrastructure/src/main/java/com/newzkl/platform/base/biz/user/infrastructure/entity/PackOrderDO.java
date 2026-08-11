package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.enums.account.PackOrderStateEnum;
import com.newzkl.platform.base.biz.user.model.pack.vo.PackGoodsVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.ShipAddressVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 入会礼包订单(pack_order)持久化对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class PackOrderDO extends BaseDO {

    /**
     * 下单账号ID
     */
    @Index
    private Long accountId;

    /**
     * 收货信息
     */
    @OldColumnName("shipVO")
    @JsonSerializable
    private ShipAddressVO ship;

    /**
     * 礼包商品ID
     */
    @Index
    private Long packId;

    /**
     * 礼包类型
     */
    private RoleEnum.CompanyRole packType;

    /**
     * 礼包等级
     */
    private Integer packLevel;

    /**
     * 礼包名称
     */
    private String levelName;

    /**
     * 订单金额
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
     * 订单明细
     */
    @JsonSerializable
    private List<PackGoodsVO> packOrderItemList;

    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;

    /**
     * 上次物流API调用时间
     */
    private LocalDateTime lastFreightApiUseTime;

    /**
     * 订单状态
     */
    @Index
    private PackOrderStateEnum state;
}
