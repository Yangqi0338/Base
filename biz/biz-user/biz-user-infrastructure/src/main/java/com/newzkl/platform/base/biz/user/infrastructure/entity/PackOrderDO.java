package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

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
@TableName
public class PackOrderDO extends BaseDO {

    /**
     * 下单账号ID
     */
    @Index
    private Long accountId;

    /**
     * 收货信息 JSON（结构：ShipAddressVO）
     */
    private String shipVO;

    /**
     * 礼包商品ID
     */
    @Index
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
     * 订单金额（分）
     */
    private Integer amount;

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
     * 上次物流API调用时间
     */
    private LocalDateTime lastFreightApiUseTime;

    /**
     * 订单状态 (0,新订单),(2,待支付),(4,待发货),(6,已发货),(8,已收货),(10,已完成),(-1,已关闭)
     */
    @Index
    private Integer state;
}
