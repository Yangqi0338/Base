package com.newzkl.platform.base.biz.goods.model.biz.vo;

import com.newzkl.platform.base.common.core.model.res.PlatformTreeNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
@Data
public class CategoryLayerVO extends CategoryVO implements PlatformTreeNode<CategoryLayerVO> {
    /**
     * 父ID
     */
    private Long pid;
    /** 父级ID列表 */
    private String pidList;
    /** 级别 */
    private Integer level;
    /**
     * 已绑定的品牌 ID 列表 (逗号拼接, 无尾逗号)
     *
     * <p>对应 spu_category 表 brand_id_list 列, 由 bindBrand 端点 add/cut 维护</p>
     */
    private String brandIdList;

    /**
     * 子分类
     */
    private List<CategoryLayerVO> children;

    @Override
    public Long getNodeId() {
        return id;
    }

    @Override
    public Long getNodePid() {
        return pid;
    }

    @Override
    public void putChildren(CategoryLayerVO categoryVO) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(categoryVO);
    }

}