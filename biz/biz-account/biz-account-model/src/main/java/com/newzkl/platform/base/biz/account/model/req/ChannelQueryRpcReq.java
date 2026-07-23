package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import lombok.Data;

import java.util.List;

@Data
public class ChannelQueryRpcReq extends BusinessPageQuery {

    /**
     * 状态 (查询)
     */
    private Integer state;
    /**
     * 状态 (查询)
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


    private Long channelId;
}
