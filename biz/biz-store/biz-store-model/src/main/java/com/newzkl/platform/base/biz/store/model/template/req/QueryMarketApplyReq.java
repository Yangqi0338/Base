package com.newzkl.platform.base.biz.store.model.template.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author niu
 * @description: 查询市场申请req
 * @date 2024/4/8 16:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryMarketApplyReq extends PageQuery {

    /**
     * 处理节点 0：待交易师处理  1：待运营商处理  2：处理结束  3:取消
     */
    private Integer handleNode;

    /**
     * 运营商id 不用传
     */
    private Long operatorId;

    /**
     * 交易师id 不用传
     */
    private Long tradersId;

    /**
     * 渠道商id 不用传
     */
    private Long channelId;
}
