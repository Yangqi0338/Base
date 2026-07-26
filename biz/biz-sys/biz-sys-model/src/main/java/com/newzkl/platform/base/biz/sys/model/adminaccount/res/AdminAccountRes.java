package com.newzkl.platform.base.biz.sys.model.adminaccount.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 平台账号视图对象。
 *
 * <p>迁移说明: 旧 {@code AdminAccountPageVO.aroleIdList} 上 {@code @IdParse} 脱敏解析
 * 属入口关注 (id→名称回填), 已剥离, 详见 findings 鉴权/脱敏下沉。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminAccountRes extends BaseRes {

    /**
     * 昵称。
     */
    private String nickname;

    /**
     * 头像。
     */
    private String face;

    /**
     * 手机号。
     */
    private String phone;

    /**
     * 登录名称。
     */
    private String username;

    /**
     * 密码密文 (不对外序列化, 仅供登录校验内部使用)。
     */
    @JsonIgnore
    private String password;

    /**
     * 帐号状态 (0 正常 1 冻结)。
     */
    private Integer state;

    /**
     * 后台角色 id 列表 (JSON/逗号串)。
     */
    private String aroleIdList;
}
