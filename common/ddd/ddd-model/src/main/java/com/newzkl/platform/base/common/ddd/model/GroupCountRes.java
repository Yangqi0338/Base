package com.newzkl.platform.base.common.ddd.model;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/117:46
 */
@Data
public class GroupCountRes {
    /**
     * 分组天数
     */
    private String transDay;
    /**
     * 数量
     */
    private Integer transNum = 0;
    /**
     * 金额
     */
    private Integer transAmount = 0;
}
