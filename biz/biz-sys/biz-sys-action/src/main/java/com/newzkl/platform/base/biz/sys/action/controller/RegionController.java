package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
