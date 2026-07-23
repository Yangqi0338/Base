package com.newzkl.platform.base.biz.market.model.query.distribution;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author niu
 * @description: 查询铺货列表
 * @date 2024/4/2 11:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DistributionsQuery extends PageQuery {

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 类型  0： 自营  1：供应链
     */
    private Integer goodsType;

    /**
     * 状态 0：下架  1：上架
     */
    private Integer state;

    /**
     * 状态 0：下架  1：上架
     */
    private Integer stateNot;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品id
     */
    private List<Long> goodsIds;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 删除状态 1：删除
     */
    private Integer delState;

    /**
     * 零售价左
     */
    private Integer sellPriceL;

    /**
     * 零售价右
     */
    private Integer sellPriceR;

    /**
     * 采购价左
     */
    private Integer supplierPriceL;

    /**
     * 采购价右
     */
    private Integer supplierPriceR;

    /**
     * 是否需要更新：0不需要，1需要
     */
    private Integer needUpdate;

    @AllArgsConstructor
    @Getter
    public enum SortType implements SortField {
        DEFAULT(0, "sd.id"),
        UP_TIME(1, "sd.up_time IS NULL, sd.up_time"),
        SELL_AMOUNT(2, "(sd.sell_num * sell_price)"),
        SELL_NUM(3, "sd.sell_num"),
        ;

        private final Integer code;
        private final String field;

        @Override
        public String getField(Integer code) {
            return Arrays.stream(SortType.values()).filter(item -> item.getCode().equals(code)).findFirst()
                    .map(SortType::getField).orElse(null);
        }
    }
}
