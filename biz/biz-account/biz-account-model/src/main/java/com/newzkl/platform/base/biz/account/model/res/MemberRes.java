package com.newzkl.platform.base.biz.account.model.res;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * c端客户
 *
 * @author fang
 */
@Data
@NoArgsConstructor
public class MemberRes extends BaseRes {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String head;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 生日
     */
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
     * 微信ID uni id
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
    private Long merchantId;
    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 背景图
     */
    private String backgroundImg;
}