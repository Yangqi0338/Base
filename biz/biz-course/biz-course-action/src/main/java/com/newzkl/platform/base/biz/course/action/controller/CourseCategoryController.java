package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.CourseCategoryDomain;
import com.newzkl.platform.base.biz.course.model.category.query.CourseCategoryQuery;
import com.newzkl.platform.base.biz.course.model.category.req.CourseCategoryReq;
import com.newzkl.platform.base.biz.course.model.category.res.CourseCategoryRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程分类管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/courseCategory")
@RequiredArgsConstructor
@FuncPermission("课程分类管理")
public class CourseCategoryController {

    private final CourseCategoryDomain courseCategoryDomain;

    /**
     * 新增课程分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    @PostMapping("/add")
    @FuncPermission("新增课程分类")
    public PlatformResult<CourseCategoryRes> add(@Validated @RequestBody CourseCategoryReq req) {
        if (req.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(courseCategoryDomain.add(req));
    }

    /**
     * 编辑课程分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    @PostMapping("/edit")
    @FuncPermission("编辑课程分类")
    public PlatformResult<CourseCategoryRes> edit(@Validated @RequestBody CourseCategoryReq req) {
        if (req.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(courseCategoryDomain.edit(req));
    }

    /**
     * 启用课程分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    @PostMapping("/enable/{id}")
    @FuncPermission("启用课程分类")
    public PlatformResult<Boolean> enable(@PathVariable("id") Long id) {
        return PlatformResult.success(courseCategoryDomain.enable(id));
    }

    /**
     * 禁用课程分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    @PostMapping("/disable/{id}")
    @FuncPermission("禁用课程分类")
    public PlatformResult<Boolean> disable(@PathVariable("id") Long id) {
        return PlatformResult.success(courseCategoryDomain.disable(id));
    }

    /**
     * 按主键查课程分类
     *
     * @param id 分类主键
     * @return 分类视图
     */
    @GetMapping("/get/{id}")
    public PlatformResult<CourseCategoryRes> get(@PathVariable("id") Long id) {
        return PlatformResult.success(courseCategoryDomain.getById(id));
    }

    /**
     * 分页查课程分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<CourseCategoryRes>> page(@RequestBody CourseCategoryQuery query) {
        return PlatformResult.success(courseCategoryDomain.pageQuery(query));
    }

    /**
     * 查全部启用分类
     *
     * @return 平铺分类列表
     */
    @GetMapping("/listEnabled")
    public PlatformResult<List<CourseCategoryRes>> listEnabled() {
        return PlatformResult.success(courseCategoryDomain.listAllEnabled());
    }

    /**
     * 删除课程分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    @PostMapping("/delete/{id}")
    @FuncPermission("删除课程分类")
    public PlatformResult<Boolean> delete(@PathVariable("id") Long id) {
        return PlatformResult.success(courseCategoryDomain.delete(id));
    }

    /**
     * 批量删除课程分类
     *
     * @param idListCommand 主键列表命令
     * @return 是否成功
     */
    @PostMapping("/batchDelete")
    @FuncPermission("批量删除课程分类")
    public PlatformResult<Boolean> batchDelete(@Validated @RequestBody IdCommand idListCommand) {
        return PlatformResult.success(courseCategoryDomain.batchDelete(idListCommand.getIdList()));
    }

    /**
     * 恢复已删除分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    @PostMapping("/recover/{id}")
    @FuncPermission("恢复已删除分类")
    public PlatformResult<Boolean> recover(@PathVariable("id") Long id) {
        return PlatformResult.success(courseCategoryDomain.recover(id));
    }
}
