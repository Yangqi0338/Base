package com.newzkl.platform.base.biz.socialbang.model.event.req;

import com.newzkl.platform.base.biz.socialbang.model.event.aggregates.ActivityConfigRich;
import lombok.Data;

/**
 * @Description: 活动配置请求对象
 * @Author: niu
 * @Date: 2024/1/9 16:20
 */
@Data
public class ActivityConfigReq {

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 活动配置聚合对象
     */
    private ActivityConfigRich configRich;

}
