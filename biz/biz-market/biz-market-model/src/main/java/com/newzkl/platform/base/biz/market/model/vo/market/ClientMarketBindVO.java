package com.newzkl.platform.base.biz.market.model.vo.market;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description: 客户市场绑定
 * @date 2023/12/6 16:50
 */
@Data
public class ClientMarketBindVO {

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 绑定类型
     */
    private AccountEnum.Identity bindType;

    /**
     * 客户id
     */
    private Long userId;

    /**
     * 客户名称
     */
    private String userName;

    /**
     * 绑定时间
     */
    private LocalDateTime time;
}
