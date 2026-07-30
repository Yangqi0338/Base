package com.newzkl.platform.base.biz.finance.model.virtual.res;

import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 虚拟资产变动记录出参
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.domain.virtual.model.vo.VirtualAssetsRecordVO}。
 * 旧 mapper xml 将 {@code create_time} 映射为 {@code alterTime}, 新实现在仓储层显式回填以保持前端契约。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VirtualAssetsRecordRes extends BaseRes {

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

    /**
     * 变动类型
     */
    private Integer alterType;

    /**
     * 变动值
     */
    private Integer alterValue;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 变动信息
     */
    private String alterInfo;

    /**
     * 变动时间 (取自记录创建时间)
     */
    private LocalDateTime alterTime;
}
