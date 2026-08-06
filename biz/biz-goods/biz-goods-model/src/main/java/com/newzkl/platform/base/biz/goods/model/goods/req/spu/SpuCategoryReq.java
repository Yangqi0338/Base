package com.newzkl.platform.base.biz.goods.model.goods.req.spu;

import com.newzkl.platform.base.biz.goods.model.biz.req.CategoryReq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分类
 *
 * @author fang
 */
@Data
public class SpuCategoryReq extends CategoryReq {
    /**
     * 父ID
     */
    @NotNull
    private Long pid;
    /**
     * 名称 查询
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;
    /**
     * 已绑定品牌 ID 列表, 逗号拼接
     *
     * <p>仅 {@code bindBrand} 走此字段做整存整取, 无 like 查询; 分类创建/修改主流程不填</p>
     */
    private String brandIdList;

}