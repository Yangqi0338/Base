package com.newzkl.platform.base.biz.finance.model.virtual.res;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 虚拟资产出参
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.domain.virtual.model.vo.VirtualAssetsVO}。
 * {@code id}/{@code createTime} 由 {@code BaseRes} 提供, 不再重复声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VirtualAssetsRes extends BaseRes {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 资产类型
     */
    private Integer assetsType;

    /**
     * 资产值
     */
    private Integer assetsValue;

    /**
     * 累计资产
     */
    private Integer totalAssets;
}
