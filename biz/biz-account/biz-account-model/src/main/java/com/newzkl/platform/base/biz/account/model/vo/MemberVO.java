package com.newzkl.platform.base.biz.account.model.vo;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.PersonalEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
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
     * 用户名称
     */
    private String name;
    /**
     * 昵称
     *
     * @ext 副数据 account 侧列, member 表无该列
     */
    private String nickname;
    /**
     * 头像
     *
     * @ext 副数据 account 侧列, member 表无该列
     */
    private String head;

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
     * unionId
     */
    private String unionId;
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
            this.id = SnowflakeGenerator.getSnowflakeId();
        }
    }

}
