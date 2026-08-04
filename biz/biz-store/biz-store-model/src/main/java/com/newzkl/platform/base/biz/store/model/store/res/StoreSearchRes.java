package com.newzkl.platform.base.biz.store.model.store.res;

import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 门店
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreSearchRes extends BaseRes {
    /**
     * 主键
     */
    private Long id;
    /**
     * 门店名称
     */
    private String name;
    /**
     *
     */
    private String logo;

    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 总客户数
     */
    private Integer totalCustomers;

    /**
     * 门店的随机商品数据
     */
    private List<GoodsVO> goodsVOList;

    /**
     * 粉丝数量（门店账户的粉丝）
     */
    private Integer fanNumber;

    @Data
    public static class GoodsVO {
        /**
         * 商品id
         */
        private Long goodsId;
        /**
         * 铺货id
         */
        private Long distributionId;
        /**
         * 商品图片
         */
        private String img;
        /**
         * 销售价 (Money, 落库 BIGINT 分)
         */
        private Money sellPrice;
    }
}