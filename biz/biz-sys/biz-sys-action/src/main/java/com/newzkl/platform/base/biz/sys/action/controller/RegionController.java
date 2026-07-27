package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行政区域控制器。
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/common")
@RequiredArgsConstructor
public class RegionController {

    private final RegionDomain regionDomain;

    /**
     * 获取完整区域树 (JSON 串)。
     *
     * @return 区域树 JSON
     */
    @GetMapping("/getRegion")
    public PlatformResult<String> getRegion() {
        return PlatformResult.success(regionDomain.getRegion());
    }

    /**
     * 按编码获取区域名称。
     *
     * @param code 区域编码
     * @return 区域名称
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @GetMapping("/getRegionByCode")
    public PlatformResult<String> getRegionByCode(@RequestParam("code") Integer code) {
        return PlatformResult.success(regionDomain.getRegionByCode(code));
    }

    /**
     * 获取区域列表 (按父编码/平铺过滤)。
     *
     * @param parentCode 父编码, 为 null 时返回全部顶层
     * @param flatten    是否平铺
     * @return 区域列表
     * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
     *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
     *   docs/planning/dead-endpoint-audit/README.md。
     */
    @Deprecated
    @GetMapping("/getRegionList")
    public PlatformResult<List<Area>> getRegionList(@RequestParam(value = "parentCode", required = false) Integer parentCode,
                                               @RequestParam(value = "flatten", defaultValue = "false") boolean flatten) {
        return PlatformResult.success(regionDomain.getRegionList(parentCode, flatten));
    }
}
