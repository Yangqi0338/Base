package com.newzkl.platform.base.biz.sys.model.adminaccount.query;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 平台账号分页查询
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminAccountQuery extends BizPageQuery {

    /**
     * 昵称 (模糊)
     */
    private String nickname;

    /**
     * 手机号 (模糊)
     */
    private String phone;

    /**
     * 登录名称 (模糊)
     */
    private String username;

    /**
     * 帐号状态 (0 正常 1 冻结)
     */
    private Integer state;

    /**
     * 角色 id (在 aroleIdList 串内模糊匹配)
     */
    private Long roleId;
}
