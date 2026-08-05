package com.newzkl.platform.base.biz.store.model.template.req;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author niu
 * @description:
 * @date 2024/4/8 15:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryModelShopReq extends BizPageQuery {

    /**
     * 审核状态
     * @see AuditEnum.State
     */
    private Integer auditState;

    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;

    /**
     * 样式code
     */
    private String styleCode;

    /**
     * 样板店名称
     */
    private String modelShopName;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建人名
     */
    private String createName;

    /**
     * 创建时间左
     */
    private LocalDateTime createTimeL;

    /**
     * 创建时间右
     */
    private LocalDateTime createTimeR;

    /**
     * 是否删除
     */
    private Integer isDelete;

}
