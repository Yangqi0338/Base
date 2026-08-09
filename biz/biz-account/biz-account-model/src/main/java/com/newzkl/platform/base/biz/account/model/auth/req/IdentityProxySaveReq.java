package com.newzkl.platform.base.biz.account.model.auth.req;

import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import lombok.Data;

/**
 * 身份代理注册请求参数
 *
 * @author muc_fang
 * @date 2024/2/22 11:22
 */
@Data
public class IdentityProxySaveReq extends IdentitySaveReq {

    /**
     * 注册域名
     */
    private String registerDomain;

    /**
     * 渠道商类型
     *
     * @see ChannelEnum.ChannelType
     */
    private ChannelEnum.ChannelType channelType;

}
