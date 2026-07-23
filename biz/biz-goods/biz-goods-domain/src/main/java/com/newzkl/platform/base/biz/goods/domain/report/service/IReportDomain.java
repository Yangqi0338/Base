package com.newzkl.platform.base.biz.goods.domain.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.report.ReportReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;

import java.util.List;

/**
 * 报告领域接口
 *
 * @author kc
 */
public interface IReportDomain {
    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    ReportRes detail(Long id);

    /**
     * 新增
     */
    Long add(ReportReq req);

    /**
     * 编辑
     */
    void edit(ReportReq req);

    /**
     * 删除
     *
     * @param id 主键
     */
    void del(Long id);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<ReportVO> queryList(ReportQuery query);

    /**
     * 查询分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    Page<ReportVO> queryPageList(ReportQuery query);
}