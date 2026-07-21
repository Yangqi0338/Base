package com.newzkl.platform.base.common.ddd.model;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/2013:20
 */
public interface ScmTreeNode<T> {
    Long getNodeId();

    Long getNodePid();

    void putChildren(T t);

    List<T> getChildren();
}
