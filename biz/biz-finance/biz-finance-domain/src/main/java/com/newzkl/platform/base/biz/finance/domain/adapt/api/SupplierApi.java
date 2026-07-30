package com.newzkl.platform.base.biz.finance.domain.adapt.api;

/**
 * 供应商域出站端口
 *
 * <p>TODO[cross-service]: 供应商域(user)为独立服务, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以远程 Dubbo consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface SupplierApi {

    /**
     * 查询供应商提现限额
     *
     * @param accountId 供应商账户 id
     * @return 限额 (分), 无限制返回 0
     */
    Integer limitAmount(Long accountId);
}
