package com.newzkl.platform.base.biz.store.model.template.req;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 查询样板店req
 *
 * @author niu
 * @date 2024/4/8 15:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModelShopQuery extends BizPageQuery {

    /**
     * 审核状态
     */
    private AuditEnum.State auditState;

    /**
     * 状态
     * @ext 取值范围: 0=禁用, 1=启用
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
    private Long creatorId;
    /**
     * 渠道商id
     */
    private Long channelId;
    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 创建人名
     */
    private String createName;

}
