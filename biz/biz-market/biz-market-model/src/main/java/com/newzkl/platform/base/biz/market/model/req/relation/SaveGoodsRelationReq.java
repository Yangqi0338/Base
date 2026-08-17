package com.newzkl.platform.base.biz.market.model.req.relation;

import com.newzkl.platform.base.biz.market.model.vo.relation.MarketGoodsInfoVO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 保存商品关系请求对象
 * @date 2023/12/7 16:59
 */
@Data
public class SaveGoodsRelationReq {

    /**
     * 商品id集合
     */
    private List<Long> goodsIds;

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 关联关系
     */
    private GoodsRelationEnum.GoodsRelation relationType;

    /**
     * 业务用户id
     */
    private Long userId;

    /**
     * 让利比例
     */
    private Integer discountRate;

    /**
     * 商品信息
     */
    private MarketGoodsInfoVO goodsInfoVO;
}
