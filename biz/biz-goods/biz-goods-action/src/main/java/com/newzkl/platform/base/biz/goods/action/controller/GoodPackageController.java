package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.goodPackage.service.GoodPackageDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
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
 * 商品-套餐控制器。
 *
 * <p>商品域「商品套餐/组包」配置, 区别于甄选师「入会礼包」(归 biz-benefit-order)。</p>
 *
 * <p>迁移偏离: 旧 {@code /api/good-packages} REST 风格 (PathVariable + 散装 RequestParam)
 * 统一为 {@code /goods/goodPackage} + POST + RequestBody, 返回 {@code PlatformResult}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/goodPackage")
@RequiredArgsConstructor
public class GoodPackageController {

    private final GoodPackageDomain goodPackageDomain;

    /**
     * 创建套餐。
     *
     * @param req 套餐请求
     * @return 套餐主键 ID
     */
    @PostMapping("create")
    public PlatformResult<Long> create(@Validated @RequestBody GoodPackageReq req) {
        return PlatformResult.success(goodPackageDomain.create(req));
    }

    /**
     * 修改套餐。
     *
     * @param req 套餐请求 (id 必填)
     * @return 成功结果
     */
    @PostMapping("update")
    public PlatformResult<Void> update(@Validated(UpdateCommand.class) @RequestBody GoodPackageReq req) {
        goodPackageDomain.update(req);
        return PlatformResult.success();
    }

    /**
     * 停用套餐。
     *
     * @param id 套餐主键 ID
     * @return 成功结果
     */
    @PostMapping("disable")
    public PlatformResult<Void> disable(@RequestParam("id") Long id) {
        goodPackageDomain.disable(id);
        return PlatformResult.success();
    }

    /**
     * 套餐详情 (按套餐业务编码)。
     *
     * @param packageId 套餐业务编码
     * @return 套餐视图对象
     */
    @GetMapping("detail")
    public PlatformResult<GoodPackageVO> detail(@RequestParam("packageId") String packageId) {
        return PlatformResult.success(goodPackageDomain.detailByPackageId(packageId));
    }

    /**
     * 套餐分页 (传 state=1 取启用套餐, 替代旧 {@code GET /enabled})。
     *
     * @param query 套餐查询
     * @return 套餐分页
     */
    @PostMapping("page")
    public PlatformResult<Page<GoodPackageVO>> page(@RequestBody GoodPackageQuery query) {
        return PlatformResult.success(goodPackageDomain.page(query));
    }
}
