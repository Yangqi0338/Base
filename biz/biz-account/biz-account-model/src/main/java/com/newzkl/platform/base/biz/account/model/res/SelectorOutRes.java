package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 甄选师
 *
 * @author fang
 */
@Data
public class SelectorOutRes extends BaseVO {
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
}