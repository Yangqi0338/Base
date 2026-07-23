package com.newzkl.platform.base.biz.finance.domain.adapt.api;

/**
 * 字典域跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.rpc.facade.IDictFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface DictApi {

    /**
     * 按字典键取值。
     *
     * @param code 字典键
     * @return 字典值
     */
    String get(Long code);

    /**
     * 按字典键设置值。
     *
     * @param code  字典键
     * @param value 字典值
     */
    void set(Long code, String value);
}
