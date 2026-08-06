package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1914:11
 */
@Data
public class AccountInfo implements Serializable {
    /**
     * 账号ID
     */
    private Long id;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 账号名称
     */
    private String username;
    /**
     * 账号角色ID集合
     */
    private String roleIdList;
    /**
     * 邀请人ID
     */
    private Long inviteAccountId;
    /**
     * 父id集合
     */
    private String pidList;
    /*** 创建时间*/
    private LocalDateTime createTime;
    /*** 所属端*/
    private CommonEnum.Client client;
}
