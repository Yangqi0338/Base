package com.newzkl.platform.base.biz.market.model.biz.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户分类同步请求。
 *
 * <p>迁移自 {@code com.zkl.scm.rpc.model.CategorySyncReq}, 仅保留平台根分类ID
 * (由 {@link BaseReq} 的 {@code id} 承载)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategorySyncReq extends BaseReq {
}
