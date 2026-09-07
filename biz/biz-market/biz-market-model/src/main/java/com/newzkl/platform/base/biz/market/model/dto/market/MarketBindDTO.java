package com.newzkl.platform.base.biz.market.model.dto.market;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 市场绑定
 */
@Data
public class MarketBindDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 绑定身份类型
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
     * 状态
     */
    private CommonEnum.YesOrNo state;
    /**
     * 解除绑定时间
     */
    private LocalDateTime debindTime;
    /**
     * 绑定时间
     */
    private LocalDateTime createTime;
}