package com.newzkl.platform.base.biz.market.model.req.distribution;

import lombok.Data;

import java.util.List;

/**
 * 批量修改商品数据
 */
@Data
public class DistributionsBatchUpdateReq {

    /** 渠道商ID */
    private Long channelId;

    /** 更新请求列表 */
    private List<DistributionsUpdateReq> updateReqs;

}
