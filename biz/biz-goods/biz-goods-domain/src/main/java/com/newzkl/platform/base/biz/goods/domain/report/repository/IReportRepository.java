package com.newzkl.platform.base.biz.goods.domain.report.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;

import java.util.List;

/**
 * 报告存储接口
 *
 * @author kc
 */
public interface IReportRepository {

    ReportRes detail(Long id);

    Long insert(ReportVO req);

    void edit(ReportVO req, ReportQuery query);

    void del(Long id);

    List<ReportVO> queryList(ReportQuery query);

    Page<ReportVO> queryPage(ReportQuery query);
}