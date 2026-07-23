package com.newzkl.platform.base.biz.account.model.res;

import com.fasterxml.jackson.annotation.JsonFormat;

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
     * 背景图
     */
    private String backgroundImg;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 生日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;

    /**
     * 常住地-省份
     */
    private String residenceProvince;

    /**
     * 常住地-城市
     */
    private String residenceCity;

    /**
     * 常住地-区县
     */
    private String residenceDistrict;

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
     * 统计：成交金额
     */
    private Integer countDealAmount;
    /**
     * 商户ID
     */
    private Long channelId;
}
