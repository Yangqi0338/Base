package com.newzkl.platform.base.biz.sys.domain.service;

import com.newzkl.platform.base.biz.sys.model.region.req.RegionReq;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;

import java.util.List;

/**
 * 行政区域领域服务
 *
 * @author fang
 */
public interface RegionDomain {

    /**
     * 获取完整区域树 (JSON 串)
     *
     * @return 区域树 JSON
     */
    String getRegion();

    /**
     * 按编码获取区域名称
     *
     * @param code 区域编码
     * @return 区域名称
     */
    String getRegionByCode(Integer code);

    /**
     * 获取区域列表 (按父编码/平铺过滤, 不含运营商筛选)
     *
     * @param parentCode 父编码, 为 null 时返回全部顶层
     * @param flatten    是否平铺
     * @return 区域列表
     */
    List<Area> getRegionList(Integer parentCode, boolean flatten);

    /**
     * 获取区域列表 (业务版: 支持按父编码过滤 / 名称匹配打标 / 剔除打标 / 平展)
     *
     * <p>能力缺口: 入参 {@code operatorFilter=1} 的"按运营商已开通区域打标"分支依赖
     * 运营商域 RPC, 当前 Base 无对应出站端口, 该分支未生效 —— 见实现内
     * {@code TODO[cross-service]}。其余分支与源行为等价</p>
     *
     * @param req 查询入参
     * @return 区域列表
     */
    List<Area> getBusinessRegion(RegionReq req);
}
