package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.AmountRateDTO;

import java.util.List;
import java.util.TreeMap;

/**
 * 字典出站端口 (outbound port)
 *
 * <p>迁移: 原直连字典 facade {@code IDictFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface DictApi {

    TreeMap<Integer, Double> getChannelServiceFee();

}
