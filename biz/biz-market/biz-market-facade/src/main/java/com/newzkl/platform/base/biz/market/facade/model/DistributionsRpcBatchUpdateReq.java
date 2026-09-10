package com.newzkl.platform.base.biz.market.facade.model;

import lombok.Data;

import java.util.List;

/**
 * 批量修改商品数据
 */
@Data
public class DistributionsRpcBatchUpdateReq {

    /** 渠道商ID */
    private Long channelId;

    /** 更新请求列表 */
    private List<DistributionsRpcUpdateReq> updateReqs;

}
