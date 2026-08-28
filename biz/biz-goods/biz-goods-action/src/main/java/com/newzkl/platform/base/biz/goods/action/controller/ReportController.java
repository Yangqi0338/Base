package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.report.service.ReportDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.report.ReportReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
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
 * 商品-报告控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/report")
@RequiredArgsConstructor
@FuncPermission("报告管理")
public class ReportController {

    private final ReportDomain reportDomain;

    /**
     * 新增报告
     *
     * @param req 报告请求
     * @return 报告主键
     */
    @PostMapping("/add")
    @FuncPermission("新增报告")
    public PlatformResult<Long> add(@Validated @RequestBody ReportReq req) {
        return PlatformResult.success(reportDomain.add(req));
    }

    /**
     * 报告详情
     *
     * @param id 报告主键
     * @return 报告详情
     */
    @GetMapping("/{id}")
    public PlatformResult<ReportRes> detail(@PathVariable Long id) {
        return PlatformResult.success(reportDomain.detail(id));
    }

    /**
     * 编辑报告
     *
     * @param req 报告请求 (id 必填)
     * @return 空结果
     */
    @PutMapping("/edit")
    @FuncPermission("编辑报告")
    public PlatformResult<Void> edit(@Validated({UpdateCommand.class, Default.class}) @RequestBody ReportReq req) {
        reportDomain.edit(req);
        return PlatformResult.success();
    }

    /**
     * 删除报告
     *
     * @param id 报告主键
     * @return 空结果
     */
    @DeleteMapping("/del/{id}")
    @FuncPermission("删除报告")
    public PlatformResult<Void> del(@PathVariable Long id) {
        reportDomain.del(id);
        return PlatformResult.success();
    }

    /**
     * 报告列表
     *
     * @param query 报告查询条件
     * @return 报告列表
     */
    @PostMapping("/queryList")
    public PlatformResult<List<ReportVO>> queryList(@RequestBody ReportQuery query) {
        return PlatformResult.success(reportDomain.queryList(query));
    }

    /**
     * 报告分页
     *
     * @param query 报告查询条件
     * @return 报告分页
     */
    @PostMapping("/queryPageList")
    public PlatformResult<Page<ReportVO>> queryPageList(@RequestBody ReportQuery query) {
        return PlatformResult.success(reportDomain.queryPageList(query));
    }
}
