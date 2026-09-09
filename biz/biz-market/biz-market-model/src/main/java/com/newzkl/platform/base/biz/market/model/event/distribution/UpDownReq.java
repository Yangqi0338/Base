package com.newzkl.platform.base.biz.market.model.event.distribution;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkTableUpDownEventMq implements Serializable {

    /**
     * 1 上架 0 下架
     */
    private SpuEnum.State enable;

    /**
     * 需要推送的事件详情
     */
    private List<Long> spuIdList;

    /**
     * 需要推送的事件详情
     */
    private CommonEnum.YesOrNo needUpdate;

}
