package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.model.support.api.DistributionDetailVO;

import java.util.List;

/**
 * 商品铺货出站端口
 *
 * @author KC
 */
public interface GoodsDistributionApi {

    /**
     * 按铺货ID批量查询铺货详情
     *
     * @param distributionIds 铺货ID列表
     * @return 铺货详情列表, 恒非 null
     */
    List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> distributionIds);
}
