package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 */
@Data
public class TeamUserCountCommand implements Serializable {
    /**
     * 角色等级
     *
     */
    private Integer level;
    /**
     * 角色id
     *
     */
    private Integer type;
    /**
     * 数量
     *
     */
    private Integer count;
}