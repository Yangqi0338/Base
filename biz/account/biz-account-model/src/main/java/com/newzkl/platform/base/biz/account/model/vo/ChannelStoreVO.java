package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChannelStoreVO implements Serializable {

    /**
     * 渠道商id
     */
    private Long channelId;
    /**
     * 联系人名称
     */
    private String contactsName;
    /**
     * 联系人电话
     */
    private String contactsPhone;

}
