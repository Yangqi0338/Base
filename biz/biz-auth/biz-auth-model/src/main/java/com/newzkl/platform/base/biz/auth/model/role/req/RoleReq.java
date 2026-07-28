package com.newzkl.platform.base.biz.auth.model.role.req;

import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色写入入参。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.RoleCommand}。
 * 旧 {@code id} 字段由父类 {@link BaseReq} 提供, 此处不重复声明。
 * 旧入参另有 {@code dataGroupIdListJson} / {@code dataGroupNameList} / {@code dataGroupVOList}
 * 三个只出不入的展示字段 (写路径从不读取), 本仓不迁入写入入参。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleReq extends BaseReq {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 申请条件
     */
    private String applyCondition;

    /**
     * 提供服务
     */
    private String provideServices;

    /**
     * 当前角色用户量
     */
    private Integer totalUserNum;

    /**
     * 资料组 ID 集合
     */
    private List<Long> dataGroupIdList;
}
