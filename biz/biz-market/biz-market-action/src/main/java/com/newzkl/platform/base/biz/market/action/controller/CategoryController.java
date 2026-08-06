package com.newzkl.platform.base.biz.market.action.controller;

import com.newzkl.platform.base.biz.market.domain.category.service.CategoryDomain;
import com.newzkl.platform.base.biz.market.model.biz.req.query.CategoryQuery;
import com.newzkl.platform.base.biz.market.model.biz.vo.CategoryVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 市场-商户分类控制器
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.category.CategoryController}。
 * 类级与方法级路径逐字沿用旧写法 (方法路径无前导斜杠), 保持对外契约不变。</p>
 *
 * <p>旧 {@code categorySave} / {@code categoryEdit} / {@code categorySync} 三个
 * {@code @Deprecated} 死端点不迁。</p>
 *
 * @author KC
 */
@RestController("marketCategoryController")
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryDomain categoryDomain;

    /**
     * 分类列表
     *
     * <p>旧实现为 {@code categoryRepository.categoryPage(query).getList()} 再经
     * {@code ScmUtil.listToTree} 组树; 新 {@code CategoryDomain#categoryTree} 已内含
     * 组树逻辑, 返回结构一致 (树形 {@code CategoryVO} 列表)。</p>
     *
     * @param categoryQuery 分类查询条件
     * @return 树形分类列表
     */
    @PostMapping("categoryList")
    public PlatformResult<List<CategoryVO>> categoryPage(@RequestBody CategoryQuery categoryQuery) {
        categoryQuery.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(categoryDomain.categoryTree(categoryQuery));
    }
}
