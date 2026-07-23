package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
    public ScmResult<String> getRegion() {
        return ScmResult.success(regionDomain.getRegion());
    }

    /**
     * 按编码获取区域名称。
     *
     * @param code 区域编码
     * @return 区域名称
     */
    @GetMapping("/getRegionByCode")
    public ScmResult<String> getRegionByCode(@RequestParam("code") Integer code) {
        return ScmResult.success(regionDomain.getRegionByCode(code));
    }

    /**
     * 获取区域列表 (按父编码/平铺过滤)。
     *
     * @param parentCode 父编码, 为 null 时返回全部顶层
     * @param flatten    是否平铺
     * @return 区域列表
     */
    @GetMapping("/getRegionList")
    public ScmResult<List<Area>> getRegionList(@RequestParam(value = "parentCode", required = false) Integer parentCode,
                                               @RequestParam(value = "flatten", defaultValue = "false") boolean flatten) {
        return ScmResult.success(regionDomain.getRegionList(parentCode, flatten));
    }
}
