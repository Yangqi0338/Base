package com.newzkl.platform.base.biz.goods.action.controller;

import com.newzkl.platform.base.biz.goods.domain.goodPackage.service.GoodPackageDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品套餐控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/api/good-packages")
@RequiredArgsConstructor
public class GoodPackageController {

    /**
     * 套餐启用状态
     */
    private static final int STATE_ENABLED = 1;

    private final GoodPackageDomain goodPackageDomain;

    /**
     * 创建套餐
     *
     * @param req 套餐请求
     * @return 套餐主键
     */
    @PostMapping
    public PlatformResult<Long> create(@Valid @RequestBody GoodPackageReq req) {
        return PlatformResult.success(goodPackageDomain.create(req));
    }

    /**
     * 更新套餐
     *
     * @param id           套餐主键
     * @param packageName  套餐名称
     * @param goodsNum     商品数量
     * @param packagePrice 套餐价格
     * @param packageDesc  套餐描述
     * @return 空结果
     */
    @PutMapping("/{id}")
    public PlatformResult<Void> update(@PathVariable Long id,
                                       @RequestParam String packageName,
                                       @RequestParam Long goodsNum,
                                       @RequestParam Integer packagePrice,
                                       @RequestParam String packageDesc) {
        GoodPackageReq req = new GoodPackageReq();
        req.setId(id);
        req.setPackageName(packageName);
        req.setGoodsNum(goodsNum);
        // 入参为分 Integer, 显式 Money.of(分) 升为值对象
        req.setPackagePrice(packagePrice == null ? null : Money.of(packagePrice));
        req.setPackageDesc(packageDesc);
        goodPackageDomain.update(req);
        return PlatformResult.success();
    }

    /**
     * 停用套餐
     *
     * @param id 套餐主键
     * @return 空结果
     */
    @PutMapping("/{id}/disable")
    public PlatformResult<Void> disable(@PathVariable Long id) {
        goodPackageDomain.disable(id);
        return PlatformResult.success();
    }

    /**
     * 查询全部启用套餐
     *
     * @return 启用套餐列表
     */
    @GetMapping("/enabled")
    public PlatformResult<List<GoodPackageVO>> getEnabledPackages() {
        GoodPackageQuery query = new GoodPackageQuery();
        query.setState(STATE_ENABLED);
        query.resetQueryList();
        return PlatformResult.success(goodPackageDomain.page(query).getRecords());
    }

    /**
     * 按套餐业务编码查详情
     *
     * @param packageId 套餐业务编码
     * @return 套餐详情
     */
    @GetMapping("/{packageId}")
    public PlatformResult<GoodPackageVO> getByPackageId(@PathVariable String packageId) {
        return PlatformResult.success(goodPackageDomain.detailByPackageId(packageId));
    }

    // 源 enable (PUT /{id}/enable) 标注 @Deprecated, 按规则不迁。
}
