package com.newzkl.platform.base.biz.account.model.vo.tencent;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sijiwang
 */
@Data
@Accessors(chain = true)
public class ImCreateUserAccountObj implements Serializable {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户签名
     */
    private String userSign;
}
