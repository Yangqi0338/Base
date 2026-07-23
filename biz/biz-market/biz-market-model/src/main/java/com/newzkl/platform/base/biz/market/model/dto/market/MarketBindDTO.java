package com.newzkl.platform.base.biz.market.model.dto.market;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 市场绑定
 */
@Data
public class MarketBindDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 绑定类型  1：运营商  2：交易师  3:渠道商
     */
    private Integer bindType;
    /**
     * 客户id
     */
    private Long userId;
    /**
     * 客户名称
     */
    private String userName;
    /**
     * 状态  0：删除  1：正常
     */
    private Integer state;
    /**
     * 解除绑定时间
     */
    private LocalDateTime debindTime;
    /**
     * 绑定时间
     */
    private LocalDateTime createTime;
}