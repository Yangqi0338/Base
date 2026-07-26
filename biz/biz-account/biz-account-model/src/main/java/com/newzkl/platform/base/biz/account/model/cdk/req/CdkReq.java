package com.newzkl.platform.base.biz.account.model.cdk.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开通码写入入参。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.CdkCommand}
 * (旧 {@code operator} 为 String 类型的运营商 ID, 本仓收敛为 Long)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CdkReq extends BaseReq {

    /**
     * 开通码值
     */
    private String value;

    /**
     * 运营商 ID
     */
    private Long operatorId;

    /**
     * 系统类型
     */
    private Integer systemType;

    /**
     * 归属人角色 ID
     */
    private Long belowRole;
}
