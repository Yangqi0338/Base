package com.newzkl.platform.base.biz.finance.application.pay.service;

import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;

/**
 * 席位购买记录编排接口。
 *
 * @author niu
 */
public interface PurchaseRecordService {

    /**
     * 新增席位购买记录 并 修改钱包
     *
     * @param saveCommand
     */
    boolean seatPackageSaveOrUpdate(PurchaseRecordReq saveCommand);
}
