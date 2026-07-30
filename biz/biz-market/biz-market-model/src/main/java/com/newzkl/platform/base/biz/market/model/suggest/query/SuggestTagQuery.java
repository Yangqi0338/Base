package com.newzkl.platform.base.biz.market.model.suggest.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 建议标签查询
 *
 * <p>迁移: 替代旧 {@code MarketSuggestTagDAO.queryByAccountId(accountId, operator, mobile)}
 * 手写 XML 的 operator 分支 SQL, 改由 MyBatis-Plus 条件组装。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SuggestTagQuery extends BizPageQuery {

    /**
     * 是否运营商视角
     *
     * <p>{@code true} 按 {@code opeartor_id} 过滤 (运营商查旗下所有提交);
     * {@code false} 按 {@code account_id} 过滤 (客户查自己的提交)。</p>
     */
    private Boolean operator;

    /**
     * 提交人手机号 (仅 operator=true 时生效)
     */
    private String mobile;
}
