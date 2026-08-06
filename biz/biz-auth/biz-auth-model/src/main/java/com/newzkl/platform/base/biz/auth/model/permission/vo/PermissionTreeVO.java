package com.newzkl.platform.base.biz.auth.model.permission.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限树节点视图
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionTreeVO extends PermissionVO {

    /** 子节点 */
    private List<PermissionTreeVO> children = new ArrayList<>();
}
