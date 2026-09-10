package com.newzkl.platform.base.biz.goods.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;

import java.util.List;

/**
 * 选品跨服务出站端口 (outbound port)
 *
 * @author KC
 */
public interface DistributionApi {

    void down(List<Long> spuIdList);

}
