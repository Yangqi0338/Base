package com.newzkl.platform.base.biz.account.domain.adapt.api;

/**
 * 开放平台开发者出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.openapi.rpc.facade.IDeveloperFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface DeveloperApi {

    /**
     * 初始化开发者应用
     *
     * @param req 初始化入参
     */
    void initDeveloper(DeveloperInitReq req);
}
