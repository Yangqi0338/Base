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

    /**
     * 营业执照识别 (识别 + 地址反查区域编码)
     *
     * <p>经 OCR 出站端口识别营业执照图片, 再对识别出的注册地址做区域反查: 从区域树按名称匹配
     * 收集编码, 逆序后逐级把已匹配的区域名从地址串剔除, 匹配到的编码列表回填到结果顶层
     * {@code areaCode} 字段, 供前端联动地址选择器。识别失败时返回 {@code null} (与源逐字一致)</p>
     *
     * @param imageUrl 营业执照图片 URL
     * @return 识别结果 (结构 {@code {result:{...}, areaCode:[]}}), 识别失败返回 {@code null}
     */
    Object businessIdentify(String imageUrl);
}
