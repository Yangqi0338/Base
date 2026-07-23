package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 席位套餐新增请求对象
 */
@Data
public class SeatPackageCreateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 席位套餐名称
     */
    @NotNull(message = "席位套餐名称不能为空")
    @Size(max = 20, message = "席位套餐名称长度不能超过20")
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

}