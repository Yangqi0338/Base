package com.newzkl.platform.base.biz.market.model.vo.market;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 市场用户VO对象
 * @date 2023/12/7 16:26
 */
@Data
public class MarketUserVO {

    /** 主键ID */
    private Long id;

    /**
     * 客户id
     */
    private Long clientId;

    /**
     * 客户名称
     */
    private String userName;

    /**
     * 渠道商名称
     */
    private String name;

    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;

    /** 设置默认时间 */
    private String headImg="https://zztp.zzxyg88.com/app/apptx.png";
}
