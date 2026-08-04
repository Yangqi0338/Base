package com.newzkl.platform.base.biz.account.facade;


import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.biz.account.facade.model.SupplierRpcQuery;
import com.newzkl.platform.base.biz.account.facade.model.SupplierRefundVO;
import com.newzkl.platform.base.biz.account.facade.model.SupplierRelationVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 供应商
 * @date 2023/8/310:34
 */
public interface SupplierFacade {

    /**
     * 获取账号信息
     * @param accountId
     * @return
     */
    SupplierOutVO getSupplierVO(Long accountId);
    /**
     * 批量查询结算配置
     * @param query
     * @return
     */
    List<SettlementConfigOutVO> settlementConfigList(SupplierRpcQuery query);

}
