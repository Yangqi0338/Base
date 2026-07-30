package com.newzkl.platform.base.biz.store.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道联系人更新入参 (跨域 user ChannelContactReq 降级为 store 本地 DTO)
 *
 * @author KC
 */
@Data
public class ChannelContactReq implements Serializable {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 联系人电话
     */
    private String phone;
}
