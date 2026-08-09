package com.newzkl.platform.base.biz.market.model.query.market;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 渠道商市场分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelMarketPageQuery extends PageQuery {

    /**
     * 客户id
     */
    private Long clientId;

    /**
     * 市场等级
     */
    private Integer marketLevel;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 绑定类型
     *
     * @ext 1：运营商 3：渠道商
     */
    private Integer bindType;

    /** 降序排序字段列表 */
    private List<String> descs;
    /** 升序排序字段列表 */
    private List<String> ascs;
}
