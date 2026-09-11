package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.PersonalEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * c端客户
 *
 * @author fang
 */
@Data
public class MemberReq extends BaseReq {
    /**
     * 用户名称
     */
    private String name;
    /**
     * 昵称
     *
     * @ext 落 account 表, 非 member 自有列
     */
    private String nickname;
    /**
     * 头像
     *
     * @ext 落 account 表, 非 member 自有列
     */
    private String head;
    /**
     * 性别
     */
    private PersonalEnum.Gender gender;
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
     * unionId
     */
    private String unionId;
    /**
     * 渠道商ID
     */
    private Long channelId;
}
