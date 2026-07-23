package com.newzkl.platform.base.biz.goods.rpc.model.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 渠道商商品关系信息
 * @date 2024/1/216:06
 */
@Data
public class ApiChannelSpuRelationVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * spu_Id
     */
    @NotNull
    private Long spuId;
    /**
     * 名称
     */
    @NotNull
    private String name;
    /**
     * 图片
     */
    @NotNull
    private String img;
    /**
     * 商品上下架状态 : 2 上架 3 下架
     */
    @NotNull
    private String spuState;
    /**
     * 销售价起始: 单位分
     */
    private Integer salePriceBegan;
    /**
     * 销售价结束
     */
    private Integer salePriceEnd;


    private String createTime;

    /**
     * 让利比例
     */
    private Integer discountRate;

    /**
     * 市场类型：GENERAL-普通市场，SPECIAL-专区市场
     */
    private String marketType;


   /**
     * 冗余: 建议零售价(to c)
     */
    private Integer unitPrice;

    /**
     * 最小计价数
     */
    private Double minPricingNum;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 商品信息
     */
    private MarketGoodsInfoVO marketGoodsInfoVO;

    /**
     * 金价更新时间
     */
    private String goldPriceUpdateTime;

    /**
     * 市场商品关系表中的商品信息
     */
    @Data
    public static class MarketGoodsInfoVO implements Serializable{
        private static final long serialVersionUID = 2L;

        /**
         * 赠送LT积分
         */
        private Integer giftLTPoints;

        /**
         * 商品标签
         */
        private String label;

        /**
         * 任务红包比例
         */
        private Integer taskRedPacketRatio;

    }
    
}
