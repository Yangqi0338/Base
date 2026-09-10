package com.newzkl.platform.base.biz.market.facade.model;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpDownReq implements Serializable {

    /**
     * 1 上架 0 下架
     */
    private SpuEnum.State enable;

    /**
     * spu id列表
     */
    private List<Long> spuIdList;

}
