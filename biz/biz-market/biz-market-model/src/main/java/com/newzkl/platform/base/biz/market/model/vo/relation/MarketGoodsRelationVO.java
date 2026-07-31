package com.newzkl.platform.base.biz.market.model.vo.relation;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 市场商品关系
 */
@Data
public class MarketGoodsRelationVO implements Serializable {
    /** 主键ID */
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

    /** 营销类型 */
    private String marketType;

    /**
     * 商品信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private MarketGoodsInfoVO goodsInfo;

    private static final long serialVersionUID = 1L;
}