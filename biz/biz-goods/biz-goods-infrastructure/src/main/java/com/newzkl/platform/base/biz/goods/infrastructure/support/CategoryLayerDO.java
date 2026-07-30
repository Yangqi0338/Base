package com.newzkl.platform.base.biz.goods.infrastructure.support;

import lombok.Data;

/**
 * 基础数据实体类
 *
 * @author god
 */
@Data
public abstract class CategoryLayerDO extends CategoryBaseDO {
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 祖先路径+自身ID，逗号分隔，逗号结尾
     * 根节点为 "id,"，如 "11,"
     * 子节点为 "父pidList + 自身id + ,"，如 "11,1101,"
     */
    private String pidList;
    /**
     * 层级深度，根节点为1
     */
    private Integer level;
}
