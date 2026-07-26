package com.newzkl.platform.base.biz.account.model.res;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页统计结果。
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.domain.role.model.res.IndexCountRes};
 * 原字段 {@code selectorPermissionVO} 为跨域等级权限结构 {@code PermissionRpcVO},
 * 该结构未落中台且当前无调用方使用, 故不迁移。</p>
 *
 * @author KC
 */
@Data
public class IndexCountRes implements Serializable {

    /**
     * 团队人数 (累加)
     */
    private Integer teamCount;

    /**
     * 团队供应商人数 (累加)
     */
    private Integer teamSupplierCount;

    /**
     * 团队甄选师人数 (累加)
     */
    private Integer teamSelectorCount;

    /**
     * 渠道商人数
     */
    private Integer channelCount;

    /**
     * 平均周订单量
     */
    private Integer weekOrderCount;

    /**
     * 平均月订单量
     */
    private Integer monthOrderCount;
}
