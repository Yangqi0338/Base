package com.newzkl.platform.base.biz.content.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.content.domain.adapt.api.RegionApi;
import com.newzkl.platform.base.biz.sys.facade.RegionFacade;
import com.newzkl.platform.base.common.ddd.facade.RegionSimpleVO;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@code RegionApi} 的跨域实现
 *
 * <p>经 {@code RegionFacade} 调 biz-sys 区域能力回填省市区名。单体下 facade 实现
 * ({@code RegionFacadeImpl}) 与本类同上下文, 由 {@code @RpcReference} 按接口注入;
 * 拆服务后处理器切 Dubbo consumer, 本类与展示层零改动</p>
 *
 * @author KC
 */
@Component
public class RegionApiImpl implements RegionApi {

    @RpcReference
    private RegionFacade regionFacade;

    @Override
    public List<RegionSimpleVO> getRegionList(Integer parentCode, boolean flatten) {
        return regionFacade.getRegionList(parentCode, flatten);
    }
}
