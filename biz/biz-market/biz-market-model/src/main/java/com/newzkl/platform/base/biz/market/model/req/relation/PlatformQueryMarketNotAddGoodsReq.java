package com.newzkl.platform.base.biz.market.model.req.relation;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 平台市场添加商品查询商品列表
 * @date 2024/1/8 15:16
 */
@Data
public class PlatformQueryMarketNotAddGoodsReq extends PageQuery {
    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 市场id
     */
    private List<Long> marketIdList;
    /**
     * 销售价左
     */
    private Integer salePriceL;
    /**
     * 销售价右
     */
    private Integer salePriceR;

    /** 账户ID */
    private Long accountId;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 市场类型: 市场类型：GENERAL-普通市场，SPECIAL-专区
     * {link com.newzkl.platform.base.biz.market.model.enums.MarketTypeEnum}
     */
    private String marketType;
    /**
     * 零售价左
     */
    private Integer unitPriceL;
    /**
     * 零售价右
     */
    private Integer unitPriceR;
    /**
     * 预估利润左
     */
    private Integer maxProfitL;
    /**
     * 预估利润右
     */
    private Integer maxProfitR;
    /**
     * 销量左
     */
    private Integer sellNumL;
    /**
     * 销量右
     */
    private Integer sellNumR;
    /** 是否选中 */
    private Integer choose;

    /**
     * 时间排序 1 升序 2 降序
     */
    public void setTimeSort(int timeSort) {
        if (timeSort == 1) {
            addSortField("id");
        } else if (timeSort == 2) {
            addDescSortField("id");
        }
    }

    /**
     * 价格排序 1 升序 2 降序
     */
    public void setPriceSort(int priceSort) {
        if (priceSort == 1) {
            addSortField("salePriceBegan");
        } else if (priceSort == 2) {
            addDescSortField("salePriceBegan");
        }
    }

    /**
     * 利润排序 1 升序 2 降序
     */
    public void setProfitSort(int profitSort) {
        if (profitSort == 1) {
            addSortField("maxProfit");
        } else if (profitSort == 2) {
            addDescSortField("maxProfit");
        }
    }

    /**
     * 销量排序 1 升序 2 降序
     */
    public void setSellNumSort(int sellNumSort) {
        if (sellNumSort == 1) {
            addSortField("sellNum");
        } else if (sellNumSort == 2) {
            addDescSortField("sellNum");
        }
    }

    public void setDescs(List<String> descs) {
        descs.forEach(this::addDescSortField);
    }

    public void setAscs(List<String> ascs) {
        ascs.forEach(this::addSortField);
    }
}
