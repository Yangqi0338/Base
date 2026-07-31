package com.newzkl.platform.base.biz.order.domain.adapt.api;

/**
 * 账户钱包出站端口
 *
 * <p>迁移: 原 domain 直连 {@code @DubboReference IAccountPurseApi} 违依赖硬线,
 * 抽为出站端口, 由 infra 实现(远程调 finance 域查汇付钱包)
 *
 * @author KC
 */
public interface AccountPurseApi {

    /**
     * 查询渠道商汇付钱包信息
     *
     * @param channelId 渠道商ID
     * @return 汇付钱包信息
     */
    HuiFuPurseInfo queryHuiFuPurse(Long channelId);
}
