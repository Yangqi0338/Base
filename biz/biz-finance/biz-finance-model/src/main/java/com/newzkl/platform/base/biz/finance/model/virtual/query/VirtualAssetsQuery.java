package com.newzkl.platform.base.biz.finance.model.virtual.query;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 虚拟资产查询入参
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.domain.virtual.model.req.QueryVirtualAssetsReq}。
 * 旧入参不分页, 新入参统一继承 {@code PageQuery} 以复用仓储层分页降级 (可 {@code setNonPaged(true)} 取全量)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VirtualAssetsQuery extends PageQuery {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 资产类型
     */
    private Integer assetsType;
}
