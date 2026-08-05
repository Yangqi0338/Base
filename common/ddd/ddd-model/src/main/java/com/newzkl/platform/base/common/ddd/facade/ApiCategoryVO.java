package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.ddd.model.res.PlatformTreeNode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品spu信息
 *
 * @author wqm
 * @since 2023年04月27日 14:58:00
 */
@Data
public class ApiCategoryVO implements Serializable, PlatformTreeNode<ApiCategoryVO> {
    /**
     * ID: 1级分类(2位数字), 2级分类(4位数字)
     */
    @NotNull
    private Long id;
    /**
     * 上级ID
     */
    @NotNull
    private Long pid;
    /**
     * 名称
     */
    @NotNull
    private String name;
    /**
     * 图片
     */
    @NotNull
    private String img;
    /**
     * 描述
     */
    @NotNull
    private String desc;
    /**
     * 子分类
     */
    private List<ApiCategoryVO> children;

    @Override
    public Long getNodeId() {
        return id;
    }

    @Override
    public Long getNodePid() {
        return pid;
    }

    @Override
    public void putChildren(ApiCategoryVO apiCategoryVO) {
        if(children == null){
            children = new ArrayList<>();
        }
        children.add(apiCategoryVO);
    }
}
