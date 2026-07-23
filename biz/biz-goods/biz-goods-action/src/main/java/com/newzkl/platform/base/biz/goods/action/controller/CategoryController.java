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
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ScmResult<Long> categoryCreate(@Validated @RequestBody SpuCategoryReq categoryReq) {
        return ScmResult.success(spuDomain.categorySave(categoryReq));
    }

    /**
     * 删除分类。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("categoryDelete")
    public ScmResult<Void> categoryDelete(@RequestBody IdListCommand idListObj) {
        spuDomain.categoryDelete(idListObj.getIdList());
        return ScmResult.success();
    }

    /**
     * 修改分类。
     *
     * @param categoryReq 分类请求
     * @return 成功结果
     */
    @PostMapping("categoryUpdate")
    public ScmResult<Void> categoryUpdate(@Validated @RequestBody SpuCategoryReq categoryReq) {
        spuDomain.categorySave(categoryReq);
        return ScmResult.success();
    }

    /**
     * 分类详情。
     *
     * @param id 分类 ID
     * @return 分类 VO
     */
    @GetMapping("category")
    public ScmResult<SpuCategoryVO> categoryVO(@RequestParam("id") Long id) {
        return ScmResult.success(spuDomain.category(id));
    }

    /**
     * 分类列表。
     *
     * @param categoryQuery 查询条件
     * @return 分类列表
     */
    @PostMapping("categoryList")
    public ScmResult<List<SpuCategoryVO>> categoryList(@RequestBody SpuCategoryQuery categoryQuery) {
        return ScmResult.success(spuCategoryService.categoryList(categoryQuery));
    }

    /**
     * app 市场分类查询列表。
     *
     * @param categoryQuery 查询条件
     * @return 分类列表
     */
    @PostMapping("appCategoryList")
    public ScmResult<List<SpuCategoryVO>> appCategoryList(@RequestBody SpuCategoryQuery categoryQuery) {
        return ScmResult.success(spuCategoryService.appCategoryList(categoryQuery));
    }

    /**
     * 货盘分类列表。
     *
     * @param categoryQuery 查询条件
     * @return 分类列表
     */
    @PostMapping("palletCategoryList")
    public ScmResult<List<SpuCategoryVO>> palletCategoryList(@RequestBody @Valid PalletCategoryPageQuery categoryQuery) {
        return ScmResult.success(spuCategoryService.palletCategoryList(categoryQuery));
    }

    /**
     * 分类绑定品牌。
     *
     * @param bindCategory 绑定命令
     * @return 成功结果
     */
    @PostMapping("bindBrand")
    public ScmResult<Void> bindBrand(@RequestBody CategoryCmd.BindBrand bindCategory) {
        spuCategoryService.bindBrand(bindCategory.getCategoryId(), bindCategory.getBrandId(),
                BooleanUtil.isTrue(bindCategory.getIsBind()));
        return ScmResult.success();
    }
}
