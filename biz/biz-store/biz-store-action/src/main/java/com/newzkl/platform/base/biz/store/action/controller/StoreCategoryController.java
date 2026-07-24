package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreCategoryDomain;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.store.model.store.command.StoreCategorySaveCommand;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.req.StoreCategoryQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreCategoryRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.groups.Default;
import java.util.List;

/**
 * 门店分类控制器。
 *
 * @author kc
 */
@RestController
@RequestMapping("/storeCategory")
@RequiredArgsConstructor
@Slf4j
public class StoreCategoryController {

    private final StoreCategoryDomain storeCategoryDomain;

    /**
     * 分类详情。
     *
     * @param id 分类 ID
     * @return 分类
     */
    @GetMapping("/{id}")
    public ScmResult<StoreCategory> detail(@PathVariable Long id) {
        return ScmResult.success(storeCategoryDomain.detail(id));
    }

    /**
     * 新增分类。
     *
     * @param saveCommand 保存命令
     * @return 分类 ID
     */
    @PostMapping("/add")
    public ScmResult<Long> add(@Validated @RequestBody StoreCategorySaveCommand saveCommand) {
        return ScmResult.success(storeCategoryDomain.add(saveCommand));
    }

    /**
     * 编辑分类。
     *
     * @param saveCommand 保存命令
     * @return 成功结果
     */
    @PutMapping("/edit")
    public ScmResult<Void> edit(@Validated({UpdateCommand.class, Default.class}) @RequestBody StoreCategorySaveCommand saveCommand) {
        storeCategoryDomain.edit(saveCommand);
        return ScmResult.success();
    }

    /**
     * 删除分类。
     *
     * @param id 分类 ID
     * @return 成功结果
     */
    @DeleteMapping("/del/{id}")
    public ScmResult<Void> del(@PathVariable Long id) {
        storeCategoryDomain.del(id);
        return ScmResult.success();
    }

    /**
     * 分类列表。
     *
     * @param query 查询条件
     * @return 分类列表
     */
    @PostMapping("/queryList")
    public ScmResult<List<StoreCategoryRes>> queryList(@RequestBody StoreCategoryQuery query) {
        applySort(query);
        return ScmResult.success(storeCategoryDomain.queryList(query));
    }

    /**
     * 分类分页。
     *
     * @param query 查询条件
     * @return 分类分页
     */
    @PostMapping("/queryPage")
    public ScmResult<IPage<StoreCategoryRes>> queryPage(@RequestBody StoreCategoryQuery query) {
        applySort(query);
        return ScmResult.success(storeCategoryDomain.queryPage(query));
    }

    /**
     * 按角色应用排序: 平台按创建时间倒序, 其余按 index。
     *
     * @param query 查询条件
     */
    private void applySort(StoreCategoryQuery query) {
        if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(SecurityUtils.getRoleId())) {
            query.addDescSortField("id");
        } else {
            query.addSortField("`index`");
        }
    }
}
