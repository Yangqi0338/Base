package com.newzkl.platform.base.biz.account.model.role.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色领域视图对象。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.entity.Role} (表 {@code role})。
 * 语义为"可申请的企业角色配置"(角色名 / 申请条件 / 提供服务 / 资料组), 与鉴权角色
 * ({@code auth_role}) 不是同一概念。旧实体回塞 {@code repository} 引用的充血写法已去除;
 * 旧 {@code dataGroupIdList} 以 {@code JSONObject.toJSONString} 手工序列化为 String,
 * 本仓改为强类型 {@code List<Long>}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends BaseRes {

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
     * 描述 (旧 DO 列 {@code des})
     */
    private String des;

    /**
     * 资料组 ID 集合
     */
    private List<Long> dataGroupIdList;
}
