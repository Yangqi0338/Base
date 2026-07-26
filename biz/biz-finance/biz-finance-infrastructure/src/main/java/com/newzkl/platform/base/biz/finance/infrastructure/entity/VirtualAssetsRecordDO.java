package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 虚拟资产变动记录。
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.infrastructure.entity.VirtualAssetsRecord}。
 * 旧实体无 {@code @TableName} (表名写在 mapper xml), 新实体显式声明。
 * 旧 {@code createTime} 为 {@code java.util.Date}, 新统一由 {@link BaseDO} 的
 * {@code LocalDateTime createTime} 承担。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("virtual_assets_record")
public class VirtualAssetsRecordDO extends BaseDO {

    /**
     * 客户id。
     */
    @Index
    private Long accountId;

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
     * 变动类型。
     */
    @Index
    private Integer alterType;

    /**
     * 变动值。
     */
    private Integer alterValue;

    /**
     * 业务类型。
     */
    @Index
    private Integer businessType;

    /**
     * 变动信息。
     */
    private String alterInfo;
}
