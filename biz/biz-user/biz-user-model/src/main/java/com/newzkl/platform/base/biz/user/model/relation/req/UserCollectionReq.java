package com.newzkl.platform.base.biz.user.model.relation.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户收藏查询请求（check 用）
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.collection.model.req.UserCollectionReq}。</p>
 *
 * @author KC
 */
@Data
public class UserCollectionReq {

    /**
     * 用户ID（由控制器填充当前登录账号）
     */
    private Long userId;

    /**
     * 铺货商品ID
     */
    @NotNull(message = "铺货商品ID不能为空")
    private Long storeDistributionId;
}
