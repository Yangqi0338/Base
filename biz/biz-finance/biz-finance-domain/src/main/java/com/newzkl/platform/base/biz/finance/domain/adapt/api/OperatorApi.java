package com.newzkl.platform.base.biz.finance.domain.adapt.api;

/**
 * 运营商域跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IOperatorFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface OperatorApi {

    /**
     * 保存运营商杠杆配置。
     *
     * @param accountId 账户ID
     * @param radio     杠杆比例
     */
    void leverSave(Long accountId, Integer radio);

    /**
     * 查询运营商杠杆。
     *
     * @param operatorId 运营商ID
     * @return 杠杆值
     */
    Integer getLever(Long operatorId);
}
