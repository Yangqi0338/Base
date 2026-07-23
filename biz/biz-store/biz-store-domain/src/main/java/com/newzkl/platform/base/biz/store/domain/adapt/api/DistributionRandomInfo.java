package com.newzkl.platform.base.biz.store.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 随机分销商品信息 (跨域 market DistributionRandomRPCVO 降级为 store 本地最小 DTO)。
 *
 * @author KC
 */
@Data
public class DistributionRandomInfo implements Serializable {

    /**
     * 分销ID
     */
    private Long id;
}
