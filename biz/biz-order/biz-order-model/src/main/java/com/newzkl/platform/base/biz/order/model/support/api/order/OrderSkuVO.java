package com.newzkl.platform.base.biz.order.model.support.api.order;

import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author niu
 * @description:
 * @date 2024/5/9 9:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSkuVO implements Serializable {

    /** 本地SPU ID */
    private Long localSpuId;

    /** 三方商品id */
    private String outSpuId;

    /** 本地ID */
    private Long localId;

    /** 外部ID */
    private String outId;

    /** 购买数量 */
    private Integer count;

    /**
     * 供应商ID  1 亚运通 2 会订货
     */
    private Long supplierId;

    /**
     * 外部平台来源(三方单) HUI_DING_HUO/LE_TAI 非外部为 null
     */
    private PlatformTypeEnum platformType;
}
