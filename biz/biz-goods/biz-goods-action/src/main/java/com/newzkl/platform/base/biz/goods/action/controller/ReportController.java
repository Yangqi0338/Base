package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.report.service.ReportDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.report.ReportReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
 * 商品-报告控制器。
 *
 * @author kc
 */
@RestController
@RequestMapping("/goods/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportDomain reportDomain;

    /**
     * 新增报告。
     *
     * @param req 新增请求
     * @return 主键
     */
    @PostMapping("/add")
    public ScmResult<Long> add(@Validated @RequestBody ReportReq req) {
        return ScmResult.success(reportDomain.add(req));
    }

    /**
     * 报告详情。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("/{id}")
    public ScmResult<ReportRes> detail(@PathVariable Long id) {
        return ScmResult.success(reportDomain.detail(id));
    }

    /**
     * 编辑报告。
     *
     * @param req 编辑请求
     * @return 成功结果
     */
    @PutMapping("/edit")
    public ScmResult<Void> edit(@Validated @RequestBody ReportReq req) {
        reportDomain.edit(req);
        return ScmResult.success();
    }

    /**
     * 删除报告。
     *
     * @param id 主键
     * @return 成功结果
     */
    @DeleteMapping("/del/{id}")
    public ScmResult<Void> del(@PathVariable Long id) {
        reportDomain.del(id);
        return ScmResult.success();
    }

    /**
     * 查询报告列表。
     *
     * @param query 查询条件
     * @return 列表
     */
    @PostMapping("/queryList")
    public ScmResult<List<ReportVO>> queryList(@RequestBody ReportQuery query) {
        return ScmResult.success(reportDomain.queryList(query));
    }

    /**
     * 查询报告分页列表。
     *
     * @param query 查询条件
     * @return 分页列表
     */
    @PostMapping("/queryPageList")
    public ScmResult<Page<ReportVO>> queryPageList(@RequestBody ReportQuery query) {
        return ScmResult.success(reportDomain.queryPageList(query));
    }
}
