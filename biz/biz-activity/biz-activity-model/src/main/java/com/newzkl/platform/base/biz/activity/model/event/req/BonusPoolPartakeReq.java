package com.newzkl.platform.base.biz.activity.model.event.req;

import com.newzkl.platform.base.biz.activity.model.event.vo.BonusPoolPartakeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 奖金池参与请求对象
 * @Author: niu
 * @Date: 2024/1/9 18:56
 */
@Data
public class BonusPoolPartakeReq  {

    /**
     * 奖金池id
     */
    private Long bonusPoolId;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 奖金池参与记录
     */
    private List<BonusPoolPartakeVO> bonusPoolPartake;
}
