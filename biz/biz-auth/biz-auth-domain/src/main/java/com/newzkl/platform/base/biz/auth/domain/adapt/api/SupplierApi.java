package com.newzkl.platform.base.biz.auth.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.SupplierOutVO;

/**
 * 供应商查询出站端口 (auth 域 -> account 域)
 *
 * <p>登录侧在供应商端登录后回填供应商状态用, 屏蔽 account 域内部 facade / model</p>
 *
 * @author KC
 */
public interface SupplierApi {

    /**
     * 查供应商信息
     *
     * @param accountId 账号ID
     * @return 供应商信息, 无供应商记录返回 null
     */
    SupplierOutVO supplier(Long accountId);
}
