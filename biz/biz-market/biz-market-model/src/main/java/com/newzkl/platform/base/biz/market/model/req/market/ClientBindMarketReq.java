package com.newzkl.platform.base.biz.market.model.req.market;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 客户绑定市场
 * @date 2023/12/6 17:19
 */
@Data
public class ClientBindMarketReq {

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 客户id
     */
    private Long userId;

    /**
     * 客户名称
     */
    private String userName;

    /**
     * 绑定类型
     */
    private AccountEnum.Identity bindType;

}
