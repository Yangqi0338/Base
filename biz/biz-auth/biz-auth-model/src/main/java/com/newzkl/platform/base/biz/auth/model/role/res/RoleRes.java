package com.newzkl.platform.base.biz.auth.model.role.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.vo.RoleVO} 的对外出参角色。
 * 旧出参含 {@code dataGroupIdListJson} (库内原始 JSON 串) 与 {@code dataGroupVOList}
 * (资料组明细, 由 admin 域 {@code IDataGroupFacade} 回填)。资料组能力未在中台落地,
 * 故本出参只保留 {@code dataGroupIdList}; {@code dataGroupNameList} / {@code dataGroupVOList}
 * 由 admin 侧资料组切片补齐 (见迁移报告依赖清单)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleRes extends BaseRes {

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
     * 描述
     */
    private String des;

    /**
     * 资料组 ID 集合
     */
    private List<Long> dataGroupIdList;
}
