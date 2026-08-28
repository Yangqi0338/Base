package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.LecturerCategoryDomain;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.biz.course.model.lecturercategory.req.LecturerCategoryReq;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
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
 * 讲师分类管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/lecturer/category")
@RequiredArgsConstructor
@FuncPermission("讲师分类管理")
public class LecturerCategoryController {

    private final LecturerCategoryDomain lecturerCategoryDomain;

    /**
     * 新增讲师分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    @PostMapping("/add")
    @FuncPermission("新增讲师分类")
    public PlatformResult<LecturerCategoryRes> add(@Validated @RequestBody LecturerCategoryReq req) {
        if (req.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(lecturerCategoryDomain.add(req));
    }

    /**
     * 编辑讲师分类
     *
     * @param req 分类请求
     * @return 分类视图
     */
    @PostMapping("/edit")
    @FuncPermission("编辑讲师分类")
    public PlatformResult<LecturerCategoryRes> edit(@Validated @RequestBody LecturerCategoryReq req) {
        if (req.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(lecturerCategoryDomain.edit(req));
    }

    /**
     * 启用讲师分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    @PostMapping("/enable/{id}")
    @FuncPermission("启用讲师分类")
    public PlatformResult<Boolean> enable(@PathVariable("id") Long id) {
        return PlatformResult.success(lecturerCategoryDomain.enable(id));
    }

    /**
     * 禁用讲师分类
     *
     * @param id 分类主键
     * @return 是否成功
     */
    @PostMapping("/disable/{id}")
    @FuncPermission("禁用讲师分类")
    public PlatformResult<Boolean> disable(@PathVariable("id") Long id) {
        return PlatformResult.success(lecturerCategoryDomain.disable(id));
    }

    /**
     * 按主键查讲师分类
     *
     * @param id 分类主键
     * @return 分类视图
     */
    @GetMapping("/get/{id}")
    public PlatformResult<LecturerCategoryRes> get(@PathVariable("id") Long id) {
        return PlatformResult.success(lecturerCategoryDomain.getById(id));
    }

    /**
     * 分页查讲师分类
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<LecturerCategoryRes>> page(@RequestBody LecturerCategoryQuery query) {
        return PlatformResult.success(lecturerCategoryDomain.pageQuery(query));
    }

    /**
     * 查全部启用讲师分类
     *
     * @return 平铺分类列表
     */
    @GetMapping("/listEnabled")
    public PlatformResult<List<LecturerCategoryRes>> listEnabled() {
        return PlatformResult.success(lecturerCategoryDomain.listAllEnabled());
    }
}
