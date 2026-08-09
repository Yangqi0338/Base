package com.newzkl.platform.base.biz.store.model.template.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询市场申请req
 *
 * @author niu
 * @date 2024/4/8 16:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketApplyQuery extends PageQuery {

    /**
     * 处理节点
     * @ext 取值范围: 0=待交易师处理, 1=待运营商处理, 2=处理结束, 3=取消
     */
    private Integer handleNode;

    /**
     * 运营商id
     * @ext 不用传
     */
    private Long operatorId;

    /**
     * 交易师id
     * @ext 不用传
     */
    private Long tradersId;

    /**
     * 渠道商id
     * @ext 不用传
     */
    private Long channelId;
}
