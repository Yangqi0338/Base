package com.newzkl.platform.base.biz.market.model.req.market;

import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 查询市场用户请求对象
 * @date 2023/12/7 16:28
 */
@Data
public class MarketUserReq {

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
     * 用户id
     */
    private List<Long> userIds;


    /**
     * 渠道商模糊搜索名称/账号
     */
    private String username;

}
