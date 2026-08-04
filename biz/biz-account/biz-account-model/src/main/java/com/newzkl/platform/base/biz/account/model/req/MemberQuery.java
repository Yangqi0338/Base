package com.newzkl.platform.base.biz.account.model.req;


import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * c端客户
 *
 * @author fang
 */
@Data
public class MemberQuery extends PageQuery {
    /**
     *
     */
    private Long id;
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
    private Long merchantId;
    /**
     * 渠道商ID
     */
    private Long channelId;

    /** ID列表 */
    private List<Long> idList;
    /** 用户账号列表 */
    private List<String> userAccountList;
}
