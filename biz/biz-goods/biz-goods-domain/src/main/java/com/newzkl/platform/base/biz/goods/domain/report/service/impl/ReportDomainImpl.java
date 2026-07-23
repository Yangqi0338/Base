package com.newzkl.platform.base.biz.goods.domain.report.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.report.repository.ReportRepository;
import com.newzkl.platform.base.biz.goods.domain.report.service.ReportDomain;
import com.newzkl.platform.base.biz.goods.model.assembler.ReportAssembler;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.report.ReportReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报告领域实现
 *
 * @author kc
 */
@Service
@RequiredArgsConstructor
public class ReportDomainImpl implements ReportDomain {
    private final ReportRepository repository;
    private final ReportAssembler assembler;

    @Override
    public ReportRes detail(Long id) {
        return repository.detail(id);
    }

    @Override
    public Long add(ReportReq req) {
        return repository.insert(assembler.req2VO(req));
    }

    @Override
    public void edit(ReportReq req) {
        ReportQuery query = new ReportQuery();
        query.setId(req.getId());
        repository.edit(assembler.req2VO(req), query);
    }

    @Override
    public void del(Long id) {
        repository.del(id);
    }

    @Override
    public List<ReportVO> queryList(ReportQuery query) {
        return repository.queryList(query);
    }

    @Override
    public Page<ReportVO> queryPageList(ReportQuery query) {
        return repository.queryPage(query);
    }
}