package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 虚拟资产。
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.infrastructure.entity.VirtualAssets}。
 * 旧实体无 {@code @TableName} (表名写在 mapper xml), 新实体显式声明。
 * {@code id}/{@code createTime} 由 {@link BaseDO} 提供, 不再重复声明。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("virtual_assets")
public class VirtualAssetsDO extends BaseDO {

    /**
     * 客户id。
     */
    @Index
    private Long accountId;

    /**
     * 客户名称。
     */
    private String accountName;

    /**
     * 客户类型。
     */
    @Index
    private PurseEnum.FinanceUser accountType;

    /**
     * 资产类型。
     */
    @Index
    private Integer assetsType;

    /**
     * 资产值。
     */
    private Integer assetsValue;

    /**
     * 累计资产。
     */
    private Integer totalAssets;
}
