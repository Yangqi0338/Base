package com.newzkl.platform.base.biz.sys.domain.adapt.api;

/**
 * 物流轨迹查询出站端口
 *
 * @author KC
 */
public interface LogisticsApi {

    /**
     * 查询物流轨迹
     *
     * @param req 查询入参
     * @return 三方返回的原始报文串, 由前端自行解析 (与旧契约一致)
     */
    String queryLogistics(LogisticsQueryReq req);
}
