package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动其他配置
 *
 * @author niu
 */
@Data
@TableName
public class ActivityOtherConfigDO extends BaseDO {
    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 显示配置
     */
    private String showConfig;

    /**
     * 具体配置
     */
    private String configDetails;

}
