package com.newzkl.platform.base.biz.market.model.req.market;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 查询绑定市场请求对象
 * @date 2023/12/7 16:05
 */
@Data
public class BindMarketListReq {

    /**
     * 客户id
     */
    private Long bindAccountId;

    /**
     * 绑定身份类型
     */
    private AccountEnum.Identity bindIdentity;

    /**
     * 市场名称
     */
    private String marketName;


    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 时间排序
     *
     * @ext 1：降序 2：升序
     */
    private Integer timeSort;

    /**
     * 数量排序
     *
     * @ext 1：降序 2：升序
     */
    private Integer numSort;

}
