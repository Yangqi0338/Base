package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.PromiseFlowRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.PromiseFlowDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.PromiseFlowDO;
import com.newzkl.platform.base.biz.finance.model.pay.req.PromiseFlowQuery;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PromiseFlowVO;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2024/1/25 10:58
 */
@Repository
@RequiredArgsConstructor
public class PromiseFlowRepositoryImpl implements PromiseFlowRepository {

    private final PromiseFlowDAO promiseFlowDAO;

    @Override
    public PromiseFlowVO promiseFlowVO(Long promiseFlowId) {
        PromiseFlowQuery query = new PromiseFlowQuery();
        query.setId(promiseFlowId);
        PromiseFlowDO promiseFlowDO = promiseFlowDAO.selectOne(promiseFlowDAO.getLw(query));
        PromiseFlowVO promiseFlowVO = TransferUtils.transfer(promiseFlowDO, PromiseFlowVO::new);
        return promiseFlowVO;
    }

    @Override
    public List<PromiseFlowVO> promiseFlowPage(PromiseFlowQuery query) {
        Page<PromiseFlowDO> promiseFlowDOPage = promiseFlowDAO.selectPage(RepositorySupport.page(query), promiseFlowDAO.getLw(query));

        return TransferUtils.transfers(promiseFlowDOPage.getRecords(), PromiseFlowVO::new);
    }

    @Override
    public Long promiseFlowSave(PromiseFlowVO promiseFlow) {
        PromiseFlowDO promiseFlowDO = TransferUtils.transfer(promiseFlow, PromiseFlowDO::new);
        promiseFlowDAO.insert(promiseFlowDO);
        return promiseFlowDO.getId();
    }

    @Override
    public int promiseFlowEdit(PromiseFlowVO promiseFlow) {
        return promiseFlowDAO.updateById(TransferUtils.transfer(promiseFlow, PromiseFlowDO::new));
    }

    @Override
    public int promiseFlowDelete(List<Long> promiseFlowIdList) {
        return promiseFlowDAO.deleteByIds(promiseFlowIdList);
    }

    @Override
    public void promiseFlowEdit(List<EditColumnDTO> columnList, Long id) {
        PromiseFlowQuery query = new PromiseFlowQuery();
        query.setId(id);
//        promiseFlowDAO.columnByQuery(columnList, query);
    }

    @Override
    public PromiseFlowVO promiseFlow(Long promiseFlowId) {
        PromiseFlowDO promiseFlowDO = promiseFlowDAO.selectById(promiseFlowId);
        return TransferUtils.transfer(promiseFlowDO, PromiseFlowVO::new);
    }
}
