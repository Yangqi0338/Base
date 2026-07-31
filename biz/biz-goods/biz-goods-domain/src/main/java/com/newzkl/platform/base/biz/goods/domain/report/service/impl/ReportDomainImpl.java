package com.newzkl.platform.base.biz.goods.domain.report.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.report.repository.ReportRepository;
import com.newzkl.platform.base.biz.goods.domain.report.service.ReportDomain;
import com.newzkl.platform.base.biz.goods.model.assembler.ReportAssembler;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.report.ReportReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        ReportRes report = repository.report(id);
        if (report == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "报告");
        }
        if (CollUtil.isNotEmpty(report.getSpuIdList())) {
            report.setSpuList(repository.spuSimpleList(report.getSpuIdList()));
        }
        return report;
    }

    @Override
    public Long add(ReportReq req) {
        return repository.insert(assembler.req2VO(req));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ReportReq req) {
        ReportQuery query = new ReportQuery();
        query.setId(req.getId());
        if (repository.countByQuery(query) == 0) {
            throw new PlatformException(BaseErrorCode.INVALID_UPDATE);
        }
        repository.updateByQuery(assembler.req2VO(req), query);
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