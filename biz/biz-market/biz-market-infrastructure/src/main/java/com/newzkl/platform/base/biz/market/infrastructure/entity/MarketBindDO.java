package com.newzkl.platform.base.biz.market.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
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
     * 绑定类型
     */
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
     * 状态
     */
    private CommonEnum.YesOrNo state;
    /**
     * 解除绑定时间
     */
    private LocalDateTime debindTime;
}