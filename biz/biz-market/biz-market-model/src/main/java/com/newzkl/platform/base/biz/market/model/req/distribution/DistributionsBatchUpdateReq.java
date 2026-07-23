package com.newzkl.platform.base.biz.market.model.req.distribution;

import lombok.Data;

import java.util.List;

/**
 * 批量修改商品数据
 */
@Data
public class DistributionsBatchUpdateReq {

    private Long channelId;

    private List<DistributionsUpdateReq> updateReqs;

}
