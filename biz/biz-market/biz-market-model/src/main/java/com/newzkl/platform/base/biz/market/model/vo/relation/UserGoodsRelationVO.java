package com.newzkl.platform.base.biz.market.model.vo.relation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 商品市场信息
 * @date 2024/1/216:06
 */
@Data
public class UserGoodsRelationVO implements Serializable {
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 一级市场ID集合
     */
    private List<Long> oneMarketIdList;
    /**
     * 二级市场ID集合
     */
    private List<Long> twoMarketIdList;
    /**
     * 分润配置字符串
     */
    private String earningsConfigJson;
    /**
     * 交易师ID
     */
    private Long dealerId;
    /**
     * 运营商ID
     */
    private Long selectorId;
    /**
     * 用户ID
     */
    private Long userId;
}
