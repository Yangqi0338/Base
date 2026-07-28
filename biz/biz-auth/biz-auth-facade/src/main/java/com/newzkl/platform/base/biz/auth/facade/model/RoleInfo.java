package com.newzkl.platform.base.biz.auth.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色基本信息 (facade 传输对象)。
 *
 * <p>{@link com.newzkl.platform.base.biz.auth.facade.AuthApi} 的对外返回体。facade 层自带模型,
 * 不复用 biz-auth-model 的 {@code RoleRes}, 以保证对外契约与内部模型解耦; 字段为内部出参的子集,
 * 仅保留跨域调用方真正需要的角色标识与描述。</p>
 *
 * @author KC
 */
@Data
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色 ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 当前角色用户量
     */
    private Integer totalUserNum;

    /**
     * 描述
     */
    private String des;
}
