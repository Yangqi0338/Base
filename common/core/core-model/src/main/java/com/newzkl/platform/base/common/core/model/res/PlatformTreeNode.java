package com.newzkl.platform.base.common.core.model.res;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/10/2013:20
 */
public interface PlatformTreeNode<T> {
    Long getNodeId();

    Long getNodePid();

    void putChildren(T t);

    List<T> getChildren();
}
