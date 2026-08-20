package com.newzkl.platform.base.biz.store.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreCategoryDomain;
import com.newzkl.platform.base.biz.store.model.store.command.StoreCategorySaveCommand;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreCategory;
import com.newzkl.platform.base.biz.store.model.store.query.StoreCategoryQuery;
import com.newzkl.platform.base.biz.store.model.store.res.StoreCategoryRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.groups.Default;
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

import java.util.List;

/**
 * 门店分类控制器
 *
 * <p>迁移自旧 {@code com.zkl.scm.terminal.interfaces.controller.StoreCategoryController},
 * 端点路径与 HTTP 方法逐字保留。分页出参由旧 {@code PageInfo} 壳改为 MyBatis-Plus {@code Page}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/storeCategory")
@RequiredArgsConstructor
@Slf4j
public class StoreCategoryController {

    private final StoreCategoryDomain storeCategoryDomain;

    /**
     * 门店分类详情
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public PlatformResult<StoreCategory> detail(@PathVariable Long id) {
        StoreCategory dto = storeCategoryDomain.detail(id);
        return PlatformResult.success(dto);
    }

    /**
     * 新增门店分类
     *
     * @param saveCommand 编辑命令
     * @return 新增结果
     */
    @PostMapping("/add")
    public PlatformResult<Long> add(@Validated @RequestBody StoreCategorySaveCommand saveCommand) {
        Long id = storeCategoryDomain.add(saveCommand);
        return PlatformResult.success(id);
    }

    /**
     * 编辑门店分类
     *
     * @param saveCommand 编辑命令
     * @return 编辑结果
     */
    @PutMapping("/edit")
    public PlatformResult<Void> edit(
            @Validated({UpdateCommand.class, Default.class}) @RequestBody StoreCategorySaveCommand saveCommand) {
        storeCategoryDomain.edit(saveCommand);
        return PlatformResult.success();
    }

    /**
     * 删除门店分类
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("/del/{id}")
    public PlatformResult<Void> del(@PathVariable Long id) {
        storeCategoryDomain.del(id);
        return PlatformResult.success();
    }

    /**
     * 查询门店分类列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @PostMapping("/queryList")
    public PlatformResult<List<StoreCategoryRes>> queryList(@RequestBody StoreCategoryQuery query) {
        if (AccountEnum.Identity.PLATFORM == SecurityUtils.getIdentity()) {
            query.addDescSortField("create_time");
        } else {
            query.addSortField("`index`");
        }
        List<StoreCategoryRes> result = storeCategoryDomain.queryList(query);
        return PlatformResult.success(result);
    }

    /**
     * 查询门店分类分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    @PostMapping("/queryPage")
    public PlatformResult<Page<StoreCategoryRes>> queryPage(@RequestBody StoreCategoryQuery query) {
        if (AccountEnum.Identity.PLATFORM == SecurityUtils.getIdentity()) {
            query.addDescSortField("create_time");
        } else {
            query.addSortField("`index`");
        }
        return PlatformResult.success(storeCategoryDomain.queryPage(query));
    }
}
