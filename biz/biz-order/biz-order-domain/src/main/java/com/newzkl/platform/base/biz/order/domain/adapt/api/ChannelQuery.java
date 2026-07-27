package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 渠道商查询入参
 *
 * @author KC
 */
@Data
public class ChannelQuery implements Serializable {

    /**
     * 渠道商ID列表
     */
    private List<Long> idList;

    /**
     * 渠道商名称
     *
     * @ext 模糊匹配
     */
    private String channelName;

    /**
     * 渠道商状态
     */
    private Integer state;
}
