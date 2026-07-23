package com.newzkl.platform.base.biz.store.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道门店联系信息 (跨域 user ChannelStoreVO 降级为 store 本地 DTO)。
 *
 * @author KC
 */
@Data
public class ChannelStoreVO implements Serializable {

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 联系人姓名
     */
    private String contactsName;

    /**
     * 联系人电话
     */
    private String contactsPhone;
}
