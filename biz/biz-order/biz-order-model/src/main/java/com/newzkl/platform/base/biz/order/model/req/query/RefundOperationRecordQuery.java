package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

/**
 * 售后操作记录-分页查询请求对象
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Data
public class RefundOperationRecordQuery extends BizPageQuery {

    /**
     * spu订单id
     */
    private Long spuOrderId;

    /**
     * 售后单refund表的主键
     */
    private Long refundId;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作方角色编码
     * @ext 前端传数字 code, Jackson 经 RoleEnum.CompanyRole 的 @JsonValue 反序列化为枚举
     */
    private RoleEnum.CompanyRole operatorRoleCode;
}
