package com.newzkl.platform.base.biz.order.model.support.api;

import com.newzkl.platform.base.biz.order.model.enums.user.AccountEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 渠道商。
 *
 * <p>迁移: 跨域 user 结构 {@code com.zkl.scm.user.model.account.res.ChannelOutRes}
 * 降级为 order 本地 DTO。</p>
 *
 * @author KC
 */
@Data
public class ChannelOutRes extends BaseVO {
    /** 主体类型 (查询)。 */
    private AccountEnum.BodyType bodyType;
    /** 状态 (查询)。 */
    private ChannelEnum.State state;
    /** 企业信息。 */
    private String companyInfo;
    /** 实名认证信息。 */
    private String nameAuthVO;
    /** 审批拒绝原因。 */
    private String auditRefuseReason;
    /** 服务费配置。 */
    private String serviceFeeConfigVO;
    /** 市场数量。 */
    private Integer marketCount;
}
