package com.newzkl.platform.base.biz.market.model.biz.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户分类编辑请求
 *
 * <p>迁移自旧 {@code CategoryCmd.Edit} 内部类, 按新规范拆为独立 model。
 * 被编辑分类ID 由 {@code BaseReq} 的 {@code id} 承载。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryEditReq extends BaseReq {

    /**
     * 分类内容
     */
    @NotNull
    @Valid
    private CategoryReq category;
}
