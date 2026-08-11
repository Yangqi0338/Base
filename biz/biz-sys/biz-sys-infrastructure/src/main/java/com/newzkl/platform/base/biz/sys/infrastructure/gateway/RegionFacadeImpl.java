package com.newzkl.platform.base.biz.sys.infrastructure.gateway;

import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.facade.RegionFacade;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.RegionSimpleVO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 行政区域跨域服务实现
 *
 * <p>注入 sys 域 {@code RegionDomain} 取区域树, 将 {@code Area} 收敛为轻量 {@code RegionSimpleVO}
 * 输出。单体下与消费方同上下文, 由 {@code @RpcReference} 直接注入; 拆服务后走 Dubbo consumer, 消费方零改动</p>
 *
 * @author KC
 */
@Component
@DubboService
@RequiredArgsConstructor
public class RegionFacadeImpl implements RegionFacade {

    private final RegionDomain regionDomain;

    @Override
    public List<RegionSimpleVO> getRegionList(Integer parentCode, boolean flatten) {
        List<Area> areaList = regionDomain.getRegionList(parentCode, flatten);
        return TransferUtils.transfers(areaList, RegionSimpleVO::new);
    }
}
