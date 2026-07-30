package com.newzkl.platform.base.biz.store.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员分组信息 (跨域 user AccountGroupVO 降级为 store 本地最小 DTO)
 *
 * @author KC
 */
@Data
public class AccountGroupInfo implements Serializable {

    /**
     * 会员ID
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String head;
}
