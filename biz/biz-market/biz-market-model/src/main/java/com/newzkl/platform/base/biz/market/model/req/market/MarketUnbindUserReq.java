package com.newzkl.platform.base.biz.market.model.req.market;

import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 查询市场用户请求对象
 * @date 2023/12/7 16:28
 */
@Data
public class MarketUnbindUserReq {

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 绑定类型
     *
     * @ext 1：运营商 2：交易师 3：渠道商
     */
    private Integer bindType;


    /**
     * 当前页
     */
    private Integer pageNo = 0;

    /**
     * 每页的数量
     */
    private Integer pageSize = 0;

    /** 渠道商ID */
    private List<Long> channelId;


    /**
     * 状态 (查询)
     */
    private Integer state;
    /**
     * 状态 (查询)
     */
    private List<Integer> stateList;

    /**
     *  渠道商模糊搜索名称/账号
     */
    private String username;

}
