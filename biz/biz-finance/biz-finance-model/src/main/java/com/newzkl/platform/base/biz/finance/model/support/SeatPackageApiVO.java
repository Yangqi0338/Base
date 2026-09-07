package com.newzkl.platform.base.biz.finance.model.support;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 席位套餐跨域投影 (finance 本地)
 *
 * <p>biz-finance 渠道商购买席位时, 经 {@code SeatPackageApi} 从 biz-goods 取套餐数量与价格。
 * 领域层只依赖本地类型, 不直连 biz-goods-facade, 由 infra 适配器完成 facade VO → 本 VO 的转换</p>
 *
 * @author KC
 */
@Data
public class SeatPackageApiVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 席位套餐名称
     */
    private String seatPackageName;

    /**
     * 商品个数
     */
    private Integer seatNum;

    /**
     * 套餐价格
     */
    private Money packagePrice;
}
