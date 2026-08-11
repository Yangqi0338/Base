package com.newzkl.platform.base.biz.sys.facade;

import com.newzkl.platform.base.common.ddd.facade.RegionSimpleVO;

import java.util.List;

/**
 * 行政区域跨域服务
 *
 * <p>对外只暴露区域名称回填所需的轻量视图, 屏蔽 biz-sys 域内 {@code Area} 树结构。
 * 消费方按 code + pid 定位区域名</p>
 *
 * @author KC
 */
public interface RegionFacade {

    /**
     * 获取区域列表(平铺, 仅 code/pid/name)
     *
     * @param parentCode 父编码, 为 null 时返回全部顶层
     * @param flatten    是否平铺
     * @return 区域轻量视图列表, 永不为 null
     */
    List<RegionSimpleVO> getRegionList(Integer parentCode, boolean flatten);
}
