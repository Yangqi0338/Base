package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.GroupCountRes;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 首页统计结果
 * @date 2023/12/2517:03
 */
@Data
public class UserCountRes {
    /**
     * 用户人数
     */
    private Integer  accountCount;
    /**
     * 渠道商人数
     */
    private Integer  channelCount;
    /**
     * 供应商人数
     */
    private Integer  supplierCount;
    /**
     * 甄选师数量
     */
    private Integer selectorCount;
    /**
     * 交易师数量
     */
    private Integer dealerCount;
    /**
     * 运营商数量
     */
    private Integer operatorCount;
    /**
     * 分组统计
     */
    private List<GroupCountRes> groupCountRes;
}
