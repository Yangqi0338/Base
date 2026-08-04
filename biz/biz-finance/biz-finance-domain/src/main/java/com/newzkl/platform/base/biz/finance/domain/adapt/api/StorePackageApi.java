package com.newzkl.platform.base.biz.finance.domain.adapt.api;

import com.newzkl.platform.base.biz.finance.model.support.SeatPackageApiVO;

/**
 * 门店席位套餐域跨服务出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.terminal.interfaces.rpc.facede.IStoreFacade#seatPackageVO};
 * 中台化后跨域只经端口, 由 infra 适配器经 {@code biz-store-facade} 覆盖, 领域层不感知 biz-store</p>
 *
 * @author KC
 */
public interface StorePackageApi {

    /**
     * 按 id 查席位套餐
     *
     * @param seatPackageId 席位套餐 id
     * @return 席位套餐投影; 不存在时返回 null
     */
    SeatPackageApiVO seatPackageVO(Long seatPackageId);
}
