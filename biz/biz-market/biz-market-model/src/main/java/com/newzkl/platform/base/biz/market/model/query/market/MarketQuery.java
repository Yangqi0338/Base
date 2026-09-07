package com.newzkl.platform.base.biz.market.model.query.market;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author niu
 * @description: 查询市场请求对象
 * @date 2023/12/5 15:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketQuery extends BizPageQuery {

    /**
     * 绑定账户id
     */
    private Long bindAccountId;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 绑定类型
     */
    private AccountEnum.Identity bindIdentity;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 市场类型
     */
    private MarketEnum.MarketTypeEnum marketType;

}
