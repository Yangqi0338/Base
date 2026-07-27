package com.newzkl.platform.base.biz.market.action.controller;

import com.newzkl.platform.base.biz.market.domain.category.service.CategoryDomain;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryEditReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategoryReq;
import com.newzkl.platform.base.biz.market.model.biz.req.CategorySyncReq;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商户分类控制器。
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.category.CategoryController}, 端点路径不变。
 * 旧实现 {@code categoryList} 直接注入 Repository 取分页再拼树, 新实现统一收敛到
 * {@link CategoryDomain#categoryTree}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryDomain categoryDomain;

    /**
     * 分类创建。
     *
     * @param req 分类请求
     * @return 新分类ID
     */
    @PostMapping("/categorySave")
    public PlatformResult<Long> categorySave(@Validated @RequestBody CategoryReq req) {
        return PlatformResult.success(categoryDomain.categorySave(req));
    }

    /**
     * 分类修改。
     *
     * @param req 编辑请求
     * @return 成功结果
     */
    @PostMapping("/categoryEdit")
    public PlatformResult<Boolean> categoryEdit(@Validated @RequestBody CategoryEditReq req) {
        categoryDomain.categoryEdit(req);
        return PlatformResult.success();
    }

    /**
     * 分类列表 (树形)。
     *
     * @param query 查询条件
     * @return 树形分类列表
     */
    @PostMapping("/categoryList")
    public PlatformResult<List<CategoryVO>> categoryList(@RequestBody CategoryQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(categoryDomain.categoryTree(query));
    }

    /**
     * 分类同步 (平台分类 -> 当前商户)。
     *
     * @param req 同步请求
     * @return 成功结果
     */
    @PostMapping("/categorySync")
    public PlatformResult<Boolean> categorySync(@RequestBody CategorySyncReq req) {
        categoryDomain.syncCategory(req);
        return PlatformResult.success();
    }
}
