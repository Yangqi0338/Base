package com.newzkl.platform.base.biz.store.facade;

import com.newzkl.platform.base.biz.store.facade.model.SeatPackageFacadeVO;

/**
 * 席位套餐域对外契约 (inbound provider)
 *
 * <p>对等旧 {@code com.zkl.scm.terminal.interfaces.rpc.facede.IStoreFacade} 中被跨域调用的
 * 席位套餐查询能力。供 biz-finance 渠道商购买席位时按 id 取套餐数量与价格, 避免调用方直连
 * biz-store 内部 domain / model</p>
 *
 * @author KC
 */
public interface SeatPackageFacade {

    /**
     * 按 id 查席位套餐对外投影
     *
     * @param seatPackageId 席位套餐 id
     * @return 席位套餐投影; 不存在时返回 null
     */
    SeatPackageFacadeVO seatPackageVO(Long seatPackageId);
}
