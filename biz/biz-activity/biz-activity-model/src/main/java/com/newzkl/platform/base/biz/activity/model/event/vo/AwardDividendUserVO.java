package com.newzkl.platform.base.biz.activity.model.event.vo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AwardDividendUserVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 添加时间
     */
    private LocalDateTime time;

    /**
     * 角色名称
     */
    private String roleName;
}
