package com.newzkl.platform.base.biz.market.action.controller;

import com.newzkl.platform.base.biz.market.domain.category.service.CategoryDomain;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
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
}
