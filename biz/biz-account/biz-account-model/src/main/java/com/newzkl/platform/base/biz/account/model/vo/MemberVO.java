package com.newzkl.platform.base.biz.account.model.vo;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * c端客户
 *
 * @author fang
 */
@Data
public class MemberVO extends BaseRes {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String head;

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
     * 统计：成交金额 (Money, 落库 BIGINT 分)
     */
    private Money countDealAmount;
    /**
     * 商户ID
     */
    private Long channelId;

    public void init() {
        this.countDealAmount = Money.ZERO;
        this.countDealNumber = 0;
        setCreateTime(LocalDateTime.now());
        setUpdateTime(LocalDateTime.now());
        if (this.getId() == null) {
            this.id = SnowflakeIdAble.getSnowflakeId();
        }
    }

}