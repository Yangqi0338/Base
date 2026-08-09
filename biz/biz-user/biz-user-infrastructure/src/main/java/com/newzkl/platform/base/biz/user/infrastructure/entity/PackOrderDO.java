package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.pack.enums.PackOrderStateEnum;
import com.newzkl.platform.base.biz.user.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.biz.user.model.pack.vo.PackGoodsVO;
import com.newzkl.platform.base.biz.user.model.relation.res.ShipAddressRes;
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
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.PackOrderDO}（表 {@code pack_order}）。</p>
 *
 * <p>迁移说明：{@code @TableName} 不写显式表名，中台 {@code DynamicTableNameInnerInterceptor}
 * 按类名去 {@code DO} 后缀推表名（{@code PackOrderDO} → {@code pack_order}）；
 * 旧手写 id/actable 注释去除，主键雪花与 createTime/updateTime/delFlag/executor 由 {@code BaseDO} 承接；
 * {@code shipVO} 靠 MP 下划线策略生成列 {@code ship_v_o}，autotable 按字段名重建列，无需 {@code @TableField}。</p>
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
