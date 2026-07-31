package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.report.repository.ReportRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.ReportDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SpuDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ReportDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuSimpleVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报告存储实现
 *
 * @author kc
 */
@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {
    @Resource
    private ReportDAO reportDAO;
    @Resource
    private SpuDAO spuDAO;

    @Override
    public ReportRes report(Long id) {
        ReportDO reportDO = reportDAO.selectById(id);
        if (reportDO == null) {
            return null;
        }
        return TransferUtils.transfer(reportDO, ReportRes::new);
    }

    @Override
    public List<SpuSimpleVO> spuSimpleList(List<Long> spuIdList) {
        if (CollUtil.isEmpty(spuIdList)) {
            return List.of();
        }
        return TransferUtils.transfers(
                spuDAO.selectList(new LambdaQueryWrapper<SpuDO>().in(SpuDO::getId, spuIdList)),
                SpuSimpleVO::new);
    }

    @Override
    public int countByQuery(ReportQuery query) {
        return reportDAO.countByQuery(query);
    }

    @Override
    public int updateByQuery(ReportVO req, ReportQuery query) {
        ReportDO reportDO = TransferUtils.transfer(req, ReportDO::new);
        return reportDAO.updateByQuery(reportDO, query);
    }

    @Override
    public Long insert(ReportVO req) {
        ReportDO reportDO = TransferUtils.transfer(req, ReportDO::new);
        reportDAO.insert(reportDO);
        return reportDO.getId();
    }

    @Override
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
