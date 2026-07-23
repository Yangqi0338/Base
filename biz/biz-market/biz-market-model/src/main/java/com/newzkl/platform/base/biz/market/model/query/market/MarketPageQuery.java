package com.newzkl.platform.base.biz.market.model.query.market;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author niu
 * @description: 查询市场请求对象
 * @date 2023/12/5 15:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketPageQuery extends PageQuery {

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
     * 下级绑定用户
     */
    private Long subBindUser;

    /**
     * 绑定类型
     */
    private Integer subBindType;

    /**
     * 商品id
     */
    private Long goodsId;
}
