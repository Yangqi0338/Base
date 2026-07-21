package com.newzkl.platform.base.biz.account.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1714:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelRegisterEvent implements Serializable {
    /**
     * 账号IDs
     */
    private Long accountId;
    /**
     * 账号名称: 手机号
     */
    private String username;
    /**
     * 明文密码
     */
    private String password;
}
