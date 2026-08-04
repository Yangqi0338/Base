package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 渠道商分页结果（数字门店版）
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.model.relation.res.ChannelPageRes};
 * 旧工程内已无该类源码, 字段按 {@code UserQueryService#queryChannelPage} 实际读写还原。</p>
 *
 * <p>⚠️ 注意: {@code queryChannelPage} 的数据库查询链路
 * ({@code iChannelRepository.queryChannelPage}) 当前仍处于注释状态,
 * 下列字段已完整声明, 但运行时将恒返回空分页。待仓储链路打通后自动生效。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelPageRes extends BaseRes {

    /**
     * 渠道商状态
     */
    private Integer state;

    /**
     * 登录账号名称（手机号）
     */
    private String username;

    /**
     * 渠道商名称
     */
    private String name;

    /**
     * 联系人名称
     */
    private String contactsName;

    /**
     * 联系方式
     */
    private String contactsWay;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 主体类型
     */
    private Integer bodyType;

    /**
     * 渠道商类型
     *
     * @see ChannelEnum.ChannelType
     */
    private String channelType;

    /**
     * 客户总数
     */
    private Integer customCount;

    /**
     * 采购金余额 (分)
     */
    private Integer earnings;

    /**
     * 商品位总数
     */
    private Integer productSeatCount;

    /**
     * 已用商品位
     */
    private Integer usedProductSeat;
}
