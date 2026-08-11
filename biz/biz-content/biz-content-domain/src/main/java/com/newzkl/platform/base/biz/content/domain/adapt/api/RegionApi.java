package com.newzkl.platform.base.biz.content.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.RegionSimpleVO;

import java.util.List;

/**
 * 行政区域出站端口
 *
 * <p>对等旧 {@code ICommonService#getBusinessRegion} 在项目案例的用法: 列表/详情按省市区 code
 * 回填名称。实现落 biz-content-infrastructure 的 {@code adapt/api}, 经 {@code RegionFacade}
 * 调 biz-sys 区域能力, 领域/展示层只见本端口</p>
 *
 * @author KC
 */
public interface RegionApi {

    /**
     * 获取区域列表(平铺, 仅 code/pid/name)
     *
     * @param parentCode 父编码, 为 null 时返回全部顶层
     * @param flatten    是否平铺
     * @return 区域轻量视图列表, 永不为 null
     */
    List<RegionSimpleVO> getRegionList(Integer parentCode, boolean flatten);
}
