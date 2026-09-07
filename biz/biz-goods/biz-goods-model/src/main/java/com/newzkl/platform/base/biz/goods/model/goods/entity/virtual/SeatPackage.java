package com.newzkl.platform.base.biz.goods.model.goods.entity.virtual;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 席位套餐领域对象
 *
 * <p>虚拟商品的一种: 渠道商/供应商购买后转为商品位额度 (钱包 {@code GOODS_SEAT} 科目),
 * 不走 SPU 上下架与下单发货链路</p>
 */
@Data
public class SeatPackage {
    /**
     * 主键
     */
    private Long id;

    /**
     * 席位套餐code
     */
    private String seatPackageCode;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

    /**
     * 席位个数
     */
    private Integer seatNum;

    /**
     * 套餐价格 (Money, 落库 BIGINT 分)
     */
    private Money packagePrice;

    /**
     * 描述
     */
    private String packageDescribe;

    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;

    /**
     * 状态：0 正常,1 已删除
     */
    private Integer deleted;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建人名
     */
    private String createName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
