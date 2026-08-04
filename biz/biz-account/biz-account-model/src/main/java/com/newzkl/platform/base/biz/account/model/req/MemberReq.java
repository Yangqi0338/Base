package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import com.newzkl.platform.base.biz.account.model.enums.PersonalEnum;
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
     * 昵称
     */
    private String name;
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
     * 统计：成交笔数
     */
    private Integer countDealNumber;
    /**
     * 统计：成交金额 (Money, 落库 BIGINT 分)
     */
    private Money countDealAmount;
    /**
     * 渠道商ID
     */
    private Long channelId;
}
