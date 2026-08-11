package com.newzkl.platform.base.biz.auth.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

/**
 * 权限持久化对象
 *
 * <p>Function(功能点) + Menu(菜单) 合并为单表 permission, 以 type 区分, pid 构树</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class PermissionDO extends BaseDO {

    /**
     * 父权限ID
     * @ext 0 表示根
     */
    @Index
    private Long pid;

    /**
     * 权限类型
     */
    private PermissionEnum.Type type;

    /**
     * 权限编码
     */
    @Index(type = IndexTypeEnum.UNIQUE)
    private String code;

    /**
     * 权限名称
     */
    @Index
    private String name;

    /**
     * 前端路由
     * @ext MENU 为前端 path, FUNC 为后端完整 URL
     */
    private String route;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @Index
    private Integer sort;
}
