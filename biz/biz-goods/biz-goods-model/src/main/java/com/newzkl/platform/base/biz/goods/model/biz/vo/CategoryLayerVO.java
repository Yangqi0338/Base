package com.newzkl.platform.base.biz.goods.model.biz.vo;

import com.newzkl.platform.base.common.ddd.model.res.PlatformTreeNode;
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