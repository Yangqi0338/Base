package com.newzkl.platform.base.biz.market.model.dto.relation;

import com.newzkl.platform.base.biz.market.model.vo.relation.MarketGoodsInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 市场商品关系
 */
@Data
public class MarketGoodsRelationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 关系类型：  1：一级市场商品  2：二级市场商品  3：市场选品商品
     */
    private Integer relationType;
    /**
     * 用户id   0：为平台   >0:为客户
     */
    private Long userId;
    /**
     * 销量
     */
    private Integer sellNum;
    /**
     * 销售额
     */
    private Integer sellAmount;
    /**
     * 状态  1：正常  0：删除
     */
    private Integer state;
    /**
     * 解绑时间
     */
    private LocalDateTime deBindTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 让利比例
     */
    private Integer discountRate;

    /**
     * 商品信息
     * @see MarketGoodsInfoVO
     */
    private String goodsInfo;

}