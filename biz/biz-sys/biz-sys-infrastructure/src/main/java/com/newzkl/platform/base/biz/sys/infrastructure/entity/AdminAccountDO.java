package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 平台账号数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("admin_account")
public class AdminAccountDO extends BaseDO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String face;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 登录名称
     */
    private String username;

    /**
     * 密码密文
     */
    private String password;

    /**
     * 帐号状态 (0 正常 1 冻结)
     */
    private Integer state;

    /**
     * 后台角色 id 列表 (JSON/逗号串)
     */
    private String aroleIdList;
}
