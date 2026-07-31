package com.newzkl.platform.base.biz.activity.model.event.req;

import lombok.Data;

/**
 * @Description: 奖金池详情查询请求对象
 * @Author: niu
 * @Date: 2024/1/16 15:13
 */
@Data
public class QueryBonusDetailsReq  {

    /**
     * 奖金池id
     */
    private Long bonusPoolId;
}
