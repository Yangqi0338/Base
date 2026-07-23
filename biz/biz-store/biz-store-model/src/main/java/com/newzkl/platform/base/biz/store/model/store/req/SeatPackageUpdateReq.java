package com.newzkl.platform.base.biz.store.model.store.req;

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
     * 套餐价格
     */
    private Integer packagePrice;

    /**
     * 描述
     */
    private String packageDescribe;

    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;

}