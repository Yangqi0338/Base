package com.newzkl.platform.base.biz.account.model.merchant.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户查询入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.MerchantQuery}。
 * 旧 {@code id} / {@code idList} 字段由父类 {@code BizPageQuery} 提供, 此处不重复声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantQuery extends BizPageQuery {

    /**
     * 名称 (精确匹配, 与旧 mapper 一致)
     */
    private String name;

    /**
     * 账号名称 (精确匹配, 与旧 mapper 一致)
     */
    private String username;
}
