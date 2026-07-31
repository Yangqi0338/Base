package com.newzkl.platform.base.biz.activity.model.event.res;

import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityOtherConfigVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.BonusPoolDataVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.StrategyVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 奖金池详情
 * @Author: niu
 * @Date: 2024/1/16 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonusDetailsRes {

    /**
     * 奖金池数据
     */
    private BonusPoolDataVO bonusPoolDataVO;

    /**
     * 活动其他配置
     */
    private ActivityOtherConfigVO activityOtherConfig;

    /**
     * 策略
     */
    private StrategyVO strategy;
}
