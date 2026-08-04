package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import lombok.Data;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
public class ChannelOutRes extends BaseRes {
    /**
     * 主体类型 (查询)
     */
    private AccountEnum.BodyType bodyType;
    /**
     * 状态 (查询)
     */
    private ChannelEnum.State state;
    /**
     * 企业信息
     */
    private String companyInfo;
    /**
     * 实名认证信息
     */
    private String nameAuthVO;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 市场数量
     */
    private Integer marketCount;
}