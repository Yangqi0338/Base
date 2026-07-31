package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.LecturerDomain;
import com.newzkl.platform.base.biz.course.model.lecturer.query.LecturerQuery;
import com.newzkl.platform.base.biz.course.model.lecturer.req.LecturerReq;
import com.newzkl.platform.base.biz.course.model.lecturer.res.LecturerRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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
 * 讲师管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerDomain lecturerDomain;

    /**
     * 新增讲师
     *
     * @param req 讲师请求
     * @return 讲师视图
     */
    @PostMapping("/add")
    public PlatformResult<LecturerRes> add(@Validated @RequestBody LecturerReq req) {
        if (req.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(lecturerDomain.add(req));
    }

    /**
     * 编辑讲师
     *
     * @param req 讲师请求
     * @return 讲师视图
     */
    @PostMapping("/edit")
    public PlatformResult<LecturerRes> edit(@Validated @RequestBody LecturerReq req) {
        if (req.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(lecturerDomain.edit(req));
    }

    /**
     * 启用讲师
     *
     * @param id 讲师主键
     * @return 是否成功
     */
    @PostMapping("/enable/{id}")
    public PlatformResult<Boolean> enable(@PathVariable("id") Long id) {
        return PlatformResult.success(lecturerDomain.enable(id));
    }

    /**
     * 禁用讲师
     *
     * @param id 讲师主键
     * @return 是否成功
     */
    @PostMapping("/disable/{id}")
    public PlatformResult<Boolean> disable(@PathVariable("id") Long id) {
        return PlatformResult.success(lecturerDomain.disable(id));
    }

    /**
     * 按主键查讲师
     *
     * @param id 讲师主键
     * @return 讲师视图
     */
    @GetMapping("/get/{id}")
    public PlatformResult<LecturerRes> get(@PathVariable("id") Long id) {
        return PlatformResult.success(lecturerDomain.getById(id));
    }

    /**
     * 分页查讲师
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<LecturerRes>> page(@RequestBody LecturerQuery query) {
        return PlatformResult.success(lecturerDomain.pageQuery(query));
    }

    /**
     * 查全部启用讲师
     *
     * @return 平铺讲师列表
     */
    @GetMapping("/listEnabled")
    public PlatformResult<List<LecturerRes>> listEnabled() {
        return PlatformResult.success(lecturerDomain.listAllEnabled());
    }

    /**
     * 按绑定主体账号查讲师
     *
     * @param mainAccountId 讲师主体账号
     * @return 讲师视图
     */
    @GetMapping("/getByMainAccountId/{mainAccountId}")
    public PlatformResult<LecturerRes> getByMainAccountId(@PathVariable("mainAccountId") Long mainAccountId) {
        return PlatformResult.success(lecturerDomain.getByMainAccountId(mainAccountId));
    }

    /**
     * 删除讲师
     *
     * @param id 讲师主键
     * @return 是否成功
     */
    @PostMapping("/delete/{id}")
    public PlatformResult<Boolean> delete(@PathVariable("id") Long id) {
        return PlatformResult.success(lecturerDomain.delete(id));
    }
}
