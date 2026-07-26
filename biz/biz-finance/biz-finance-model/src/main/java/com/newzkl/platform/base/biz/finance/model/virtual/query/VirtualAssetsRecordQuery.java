package com.newzkl.platform.base.biz.finance.model.virtual.query;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 虚拟资产变动记录查询入参。
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.domain.virtual.model.req.QueryVirtualAssetsRecordReq}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VirtualAssetsRecordQuery extends PageQuery {

    /**
     * 客户id。
     */
    private Long accountId;

    /**
     * 客户类型。
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 资产类型。
     */
    private Integer assetsType;

    /**
     * 业务类型。
     */
    private Integer businessType;

    /**
     * 变动类型。
     */
    private Integer alterType;
}
