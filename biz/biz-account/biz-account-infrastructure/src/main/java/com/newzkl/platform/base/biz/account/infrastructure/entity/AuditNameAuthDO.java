package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import lombok.Data;

/**
 * 实名认证审批数据
 */
@Data
public class AuditNameAuthDO extends AuditBaseDO {

    private String name;
    private String nameAuthInfo;
}
