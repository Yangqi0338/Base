package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.brand.service.BrandDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.BrandPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.BrandReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-品牌控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/goods/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandDomain brandDomain;

    /**
     * 创建品牌。
     *
     * @param brandReq 品牌请求
     * @return 品牌 ID
     */
    @PostMapping("brandCreate")
    public PlatformResult<Long> brandCreate(@Validated @RequestBody BrandReq brandReq) {
        Long brandId = brandDomain.brandSave(brandReq);
        return PlatformResult.success(brandId);
    }

    /**
     * 删除品牌。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("brandDelete")
    public PlatformResult<Void> brandDelete(@RequestBody IdListCommand idListObj) {
        brandDomain.brandDelete(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改品牌。
     *
     * @param brandReq 品牌请求
     * @return 成功结果
     */
    @PostMapping("brandUpdate")
    public PlatformResult<Void> brandUpdate(@Validated @RequestBody BrandReq brandReq) {
        brandDomain.brandSave(brandReq);
        return PlatformResult.success();
    }

    /**
     * 品牌详情。
     *
     * @param id 品牌 ID
     * @return 品牌 VO
     */
    @GetMapping("brand")
    public PlatformResult<BrandVO> brandVO(@RequestParam("id") Long id) {
        return PlatformResult.success(brandDomain.brandById(id));
    }

    /**
     * 品牌分页。
     *
     * @param brandQuery 分页查询
     * @return 品牌分页
     */
    @PostMapping("brandPage")
    public PlatformResult<Page<BrandVO>> brandPage(@RequestBody BrandPageQuery brandQuery) {
        return PlatformResult.success(brandDomain.brandPage(brandQuery));
    }
}
