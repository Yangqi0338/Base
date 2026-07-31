package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动其他配置
 *
 * @author niu
 */
@Data
@TableName("activity_other_config")
public class ActivityOtherConfigDO implements Serializable {
    private Long id;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 显示配置
     */
    private String showConfig;

    /**
     * 具体配置
     */
    private String configDetails;

    private static final long serialVersionUID = 1L;
}
