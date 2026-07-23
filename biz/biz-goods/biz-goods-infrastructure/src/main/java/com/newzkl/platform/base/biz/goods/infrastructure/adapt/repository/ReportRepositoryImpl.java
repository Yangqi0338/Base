package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.report.repository.IReportRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.ReportDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SpuDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ReportDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuSimpleVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 报告存储实现
 *
 * @author kc
 */
@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements IReportRepository {
    @Resource
    private ReportDAO reportDAO;
    @Resource
    private SpuDAO spuDAO;

    @Override
    public ReportRes detail(Long id) {
        ReportDO reportDO = reportDAO.selectById(id);
        if (reportDO == null) {
            throw new ScmException(BaseErrorCode.NODATA, "报告");
        }
        ReportRes report = TransferUtils.transfer(reportDO, ReportRes::new);
        if (CollUtil.isNotEmpty(report.getSpuIdList())) {
            report.setSpuList(
                    TransferUtils.transfers(
                            spuDAO.selectList(
                                    new LambdaQueryWrapper<SpuDO>().in(
                                            SpuDO::getId, report.getSpuIdList()
                                    )
                            ), SpuSimpleVO::new));
        }
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(ReportVO req) {
        ReportDO reportDO = TransferUtils.transfer(req, ReportDO::new);
        reportDAO.insert(reportDO);
        return reportDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ReportVO req, ReportQuery reportQuery) {
        Long reportId = req.getId();

        if (reportDAO.countByQuery(reportQuery) == 0) {
            throw new ScmException(BaseErrorCode.INVALID_UPDATE);
        }

        // 设置要更新的视频id列表
        Set<Long> reportIdSet = CollUtil.newHashSet(reportId);
        if (CollUtil.isEmpty(reportIdSet)) {
            reportIdSet.addAll(reportDAO.idByQuery(reportQuery));
        }

        ReportDO reportDO = TransferUtils.transfer(req, ReportDO::new);
        int i = reportDAO.updateByQuery(reportDO, reportQuery);

        // 修改成功
        if (i > 0) {
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        reportDAO.deleteById(id);
    }

    @Override
    public List<ReportVO> queryList(ReportQuery query) {
        return reportDAO.listByQuery(query);
    }

    @Override
    public Page<ReportVO> queryPage(ReportQuery query) {
        return reportDAO.queryPage(RepositorySupport.page(query), query);
    }
}
