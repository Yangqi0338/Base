package com.newzkl.platform.base.biz.goods.rpc.model.freight;

import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 地区编码对象
 * @date 2023/4/28 14:29
 */
@Data
public class RegionEasyVO implements Serializable {

    /**
     * 省
     */
    private Integer provinceCode;

    /**
     * 市
     */
    private Integer cityCode;

    /**
     * 区/县
     */
    private Integer areaCode;
}
