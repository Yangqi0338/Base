package com.newzkl.platform.base.biz.order.domain.service.flow.model;



import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/159:28
 */
public interface Node {

    List<NextLine> getNext();
}
