package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.PersonalEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * c端客户
 *
 * @author fang
 */
@Data
@TableName
public class MemberDO extends BaseDO {
    /**
     * 用户名称
     */
    private String name;
    /**
     * 背景图
     */
    private String backgroundImg;
    /**
     * 性别
     */
    private PersonalEnum.Gender gender;
    /**
     * 生日
     */
    private LocalDate birthday;
    /**
     * 常住地
     * @ext 省份, 城市, 区县
     */
    private String residence;

    /**
     * 微信ID
     */
    private String wxId;
    /**
     * openId
     */
    private String openId;
    /**
     * unionId
     */
    private String unionId;
    /**
     * 渠道商ID
     */
    private Long channelId;
}