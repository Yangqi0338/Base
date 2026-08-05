package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * @author 
 * 市场绑定
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class MarketBindDO extends BaseDO {
    /**
     * 市场id
     */
    @Index
    private Long marketId;
    /**
     * 绑定类型  1：运营商  2：交易师  3:渠道商
     */
    @Index
    private RoleEnum.CompanyRole bindType;
    /**
     * 客户id
     */
    @Index
    private Long userId;
    /**
     * 客户名称
     */
    private String userName;
    /**
     * 状态  0：删除  1：正常
     */
    private CommonEnum.YesOrNo state;
    /**
     * 解除绑定时间
     */
    private LocalDateTime debindTime;
}