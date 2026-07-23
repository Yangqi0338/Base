package com.newzkl.platform.base.biz.store.model.store.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

import java.util.List;

/**
* 渠道商分页查询
*/
@Data
public class StoreAccountQuery extends PageQuery {

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 昵称 (查询)
     */
    private String nickname;

    /**
     * 绑定时间左
     */
    private String bandTimeL;

    /**
     * 绑定时间右
     */
    private String bandTimeR;

    /**
     * 消费金额左
     */
    private Integer payAmountL;

    /**
     * 消费金额右
     */
    private Integer payAmountR;

    /**
     * 进店时间左
     */
    private String viewTimeL;

    /**
     * 进店时间右
     */
    private String viewTimeR;

    /**
     * 0:未拉黑，1已拉黑
     */
    private Integer relationType;

    /**
     * 客户id集合
     */
    private List<Long> accountIdList;
}
