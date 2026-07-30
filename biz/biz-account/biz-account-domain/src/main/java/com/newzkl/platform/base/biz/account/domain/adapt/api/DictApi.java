package com.newzkl.platform.base.biz.account.domain.adapt.api;

/**
 * 字典出站端口 (outbound port)
 *
 * <p>迁移: 原直连字典 facade {@code IDictFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface DictApi {

    /**
     * 按字典键取值
     *
     * @param code 字典键
     * @return 字典值, 无则 null
     */
    String get(Long code);
}
