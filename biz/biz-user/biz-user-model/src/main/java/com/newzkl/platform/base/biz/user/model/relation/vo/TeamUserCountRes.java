package com.newzkl.platform.base.biz.user.model.relation.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 团队用户数量统计结果
 *
 * @author muc_fang
 */
@Data
public class TeamUserCountRes implements Serializable {
    /**
     * 等级
     */
    private int level;
    /**
     * 数量
     */
    private int count;
}
