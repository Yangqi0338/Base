package com.newzkl.platform.base.common.ddd.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 账户基础信息 (跨域 user AccountVO 降级为 finance 本地最小 DTO)
 *
 * @author KC
 */
@Data
public class AccountInfoVO implements Serializable {

    /**
     * 头像
     */
    private String head;

    /**
     * 昵称
     */
    private String nickname;
}
