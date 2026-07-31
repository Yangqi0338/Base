package com.newzkl.platform.base.biz.order.domain.service.flow.model;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/159:28
 */
@Data
public class NextLine {
    private Node toNode;
    private String condition;
}
