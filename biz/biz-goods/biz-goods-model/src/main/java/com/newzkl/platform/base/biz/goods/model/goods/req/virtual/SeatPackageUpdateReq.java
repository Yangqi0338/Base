package com.newzkl.platform.base.biz.goods.model.goods.req.virtual;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 席位套餐修改请求对象
 */
@Data
public class SeatPackageUpdateReq implements Serializable {
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
     * 状态
     * @ext 取值范围: 0=禁用, 1=启用
     */
    private Integer state;

}
