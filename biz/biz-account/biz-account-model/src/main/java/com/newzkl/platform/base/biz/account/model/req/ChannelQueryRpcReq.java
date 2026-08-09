package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
 * 渠道商 RPC 查询入参
 */
@Data
public class ChannelQueryRpcReq extends BizPageQuery {

    /**
     * 状态 (查询)
     */
    private Integer state;
    /**
     * 状态集合 (查询)
     */
    private List<Integer> stateList;
    /**
     * 账号名称 (查询)
     */
    private String username;
    /**
     * 渠道商名称
     */
    private String channelName;


    /** 渠道商ID */
    private Long channelId;
}
