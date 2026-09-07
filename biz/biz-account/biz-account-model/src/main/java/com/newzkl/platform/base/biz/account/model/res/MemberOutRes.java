package com.newzkl.platform.base.biz.account.model.res;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.PersonalEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberOutRes extends BaseRes {
    /**
     * 用户名称
     */
    private String name;
    /**
     * 背景图
     */
    private String backgroundImg;

    /**
     * 性别 (MALE 男 / FEMALE 女), JSON 出参为 code 数值
     */
    private PersonalEnum.Gender gender;

    /**
     * 生日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;

    /**
     * 常住地
     *
     * @ext 省份, 城市, 区县, 逗号分隔单列存储
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
     * 统计：成交笔数
     */
    private Integer countDealNumber;
    /**
     * 统计：成交金额 (Money, 落库 BIGINT 分)
     */
    private Money countDealAmount;
    /**
     * 商户ID
     */
    private Long channelId;
}
