package com.newzkl.platform.base.biz.goods.domain.adapt.api;

/**
 * 字典域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.admin.rpc.facade.IDictFacade}。
 * 席位套餐纵切迁入 biz-goods 时随迁 (唯一消费者为 {@code SeatPackageServiceImpl})</p>
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
