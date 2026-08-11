package com.newzkl.platform.base.biz.socialbang.model.strategy.res;

import lombok.Data;

/**
 * @Description: 抽取方法返回对象
 * @Author: niu
 * @Date: 2024/1/8 17:42
 */
@Data
public class DrawMethodRes {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 奖品id 0为现金
     */
    private Long awardId = 0L;

    /**
     * 奖品类型 0：现金
     */
    private Integer awardType = 0;

    /**
     * 扩展数据
     */
    private String ext;

    /**
     * 发放类型
     */
    private Integer grantType;
}
