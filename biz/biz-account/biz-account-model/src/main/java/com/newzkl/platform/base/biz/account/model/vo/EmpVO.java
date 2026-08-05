package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工身份视图
 *
 * <p>与 {@code emp} 表对齐。旧 {@code emp} 表是自带 username/password/account_id 的独立登录表,
 * Base 已把登录凭证与父子关系收敛到 {@code account} 表, {@code emp} 只留员工侧的类型与岗位</p>
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmpVO extends BaseRes {

    /**
     * 岗位ID
     *
     * <p>历史遗留单值字段, 与 并存: 列表列才是落库列</p>
     */
    private Long jobId;

    /**
     * 员工类型
     */
    private AuthEnum.EmpType type;

    /**
     * 岗位ID集合, 逗号隔开
     */
    private String jobIdList;
}
