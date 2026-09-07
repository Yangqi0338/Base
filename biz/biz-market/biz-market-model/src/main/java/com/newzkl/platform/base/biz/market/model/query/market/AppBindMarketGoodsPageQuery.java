package com.newzkl.platform.base.biz.market.model.query.market;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * App 端已绑定市场商品分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppBindMarketGoodsPageQuery extends PageQuery {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 商品ID集合
     */
    private List<Long> goodsIdList;
    /**
     * 关系类型
     *
     * @ext 2：市场商品 3：市场选品商品
     */
    private Integer relationType;

    /** 绑定身份类型 */
    private AccountEnum.Identity bindType;
    /**
     * 商品上下架状态
     *
     * @ext 0：仓库中 2：上架中 3：待上架
     */
    private String spuState;

    /** 降序排序字段列表 */
    private List<String> descs;
    /** 升序排序字段列表 */
    private List<String> ascs;

}
