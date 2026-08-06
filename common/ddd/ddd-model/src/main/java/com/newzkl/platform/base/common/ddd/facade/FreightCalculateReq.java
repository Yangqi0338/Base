package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author niu
 * @description: 运费计算请求对象
 * @date 2023/4/28 14:08
 */
@Data
public class FreightCalculateReq implements Serializable {

    /**
     * 商品信息列表 key：货源，0表示自营，value：商品信息集合
     */
    private Map<Long, List<FreightCalculateGoodsVO>> goodsInfoList;

    /**
     * 区域编码
     */
    private RegionEasyVO regionEasyVO;
}
