package com.newzkl.platform.base.biz.activity.model.bonus.req;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

/**
 * @Description: 修改自定义奖金请求对象
 * @Author: niu
 * @Date: 2024/1/22 11:17
 */
@Data
public class AlterCustomBonusReq  {

    /**
     * 自定义奖金 (Money, 入参元, 落库 BIGINT 分)
     */
    private Money customBonus;
}
