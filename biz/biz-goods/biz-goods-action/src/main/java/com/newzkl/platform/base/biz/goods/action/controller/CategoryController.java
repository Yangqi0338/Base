package com.newzkl.platform.base.biz.goods.action.controller;

import cn.hutool.core.util.BooleanUtil;
import com.newzkl.platform.base.biz.goods.action.cmd.CategoryCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.PalletCategoryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.SpuCategoryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 商品-分类控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/goods/category")
@RequiredArgsConstructor
public class CategoryController {

    private final SpuDomain spuDomain;
    private final SpuCategoryService spuCategoryService;

    /**
     * 创建分类。
     *
     * @param categoryReq 分类请求
     * @return 分类 ID
     */
    @PostMapping("categoryCreate")
    public PlatformResult<Long> categoryCreate(@Validated @RequestBody SpuCategoryReq categoryReq) {
        return PlatformResult.success(spuDomain.categorySave(categoryReq));
    }

    /**
     * 删除分类。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("categoryDelete")
    public PlatformResult<Void> categoryDelete(@RequestBody IdListCommand idListObj) {
        spuDomain.categoryDelete(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改分类。
     *
     * @param categoryReq 分类请求
     * @return 成功结果
     */
    @PostMapping("categoryUpdate")
    public PlatformResult<Void> categoryUpdate(@Validated @RequestBody SpuCategoryReq categoryReq) {
        spuDomain.categorySave(categoryReq);
        return PlatformResult.success();
    }

    /**
     * 分类列表。
     *
     * @param categoryQuery 查询条件
     * @return 分类列表
     */
    @PostMapping("categoryList")
    public PlatformResult<List<SpuCategoryVO>> categoryList(@RequestBody SpuCategoryQuery categoryQuery) {
        return PlatformResult.success(spuCategoryService.categoryList(categoryQuery));
    }

    /**
     * app 市场分类查询列表。
     *
     * @param categoryQuery 查询条件
     * @return 分类列表
     */
    @PostMapping("appCategoryList")
    public PlatformResult<List<SpuCategoryVO>> appCategoryList(@RequestBody SpuCategoryQuery categoryQuery) {
        return PlatformResult.success(spuCategoryService.appCategoryList(categoryQuery));
    }

    /**
     * 货盘分类列表。
     *
     * @param categoryQuery 查询条件
     * @return 分类列表
     */
    @PostMapping("palletCategoryList")
    public PlatformResult<List<SpuCategoryVO>> palletCategoryList(@RequestBody @Valid PalletCategoryPageQuery categoryQuery) {
        return PlatformResult.success(spuCategoryService.palletCategoryList(categoryQuery));
    }

    /**
     * 分类绑定品牌。
     *
     * @param bindCategory 绑定命令
     * @return 成功结果
     */
    @PostMapping("bindBrand")
    public PlatformResult<Void> bindBrand(@RequestBody CategoryCmd.BindBrand bindCategory) {
        spuCategoryService.bindBrand(bindCategory.getCategoryId(), bindCategory.getBrandId(),
                BooleanUtil.isTrue(bindCategory.getIsBind()));
        return PlatformResult.success();
    }
}
