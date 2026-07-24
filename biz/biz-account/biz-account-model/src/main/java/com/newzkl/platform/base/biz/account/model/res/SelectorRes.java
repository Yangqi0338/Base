package com.newzkl.platform.base.biz.account.model.res;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

/**
 * 甄选师
 *
 * @author fang
 */
@Data
public class SelectorRes extends BaseRes {

    private Integer state;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 团队人数:累加
     */
    private Integer teamCount;
    /**
     * 昨日邀请人数:定时写
     */
    private Integer yesterdayInvite;
    /**
     * 今日邀请人数:定时写
     */
    private Integer todayInvite;
    /**
     * 7日邀请人数:定时写
     */
    private Integer weekInvite;
    /**
     * 30日邀请人数:定时写
     */
    private Integer monthInvite;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 团队供应商人数:累加
     */
    private Integer teamSupplierCount;
    /**
     * 团队甄选师人数:累加
     */
    private Integer teamSelectorCount;
    /**
     * 月邀请人数:定时写
     */
    private Integer toMonthInvite;
    /**
     * 自身的订单流水
     */
    private Integer orderAmount;
    /**
     * 总订单流水
     */
    private Integer orderTotalAmount;
    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;

    public void init() {


        this.level = 1;
        this.state = RoleEnum.State.IN.getCode();

        this.goodsPoints = 0;
        this.levelUpProgress = 0.0;
        this.orderTotalAmount = 0;
        this.orderAmount = 0;
        this.teamCount = 0;
        this.yesterdayInvite = 0;
        this.teamSupplierCount = 0;
        this.teamSelectorCount = 0;
        this.toMonthInvite = 0;
        this.weekInvite = 0;
        this.yesterdayInvite = 0;
        this.todayInvite = 0;
        this.monthInvite = 0;

        if (StrUtil.isBlank(this.phone)) {
            this.phone = this.username;
        }
    }
}