package com.newzkl.platform.base.biz.finance.domain.pay.service.impl;

import com.newzkl.platform.base.biz.finance.domain.adapt.repository.PurchaseRecordRepository;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PurchaseRecordDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.PurchaseRecordAssembler;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购买记录 (PurchaseRecord)存储实现
 *
 * @author kc
 * @since 2025-11-25 17:24:24
 */
@Service
@RequiredArgsConstructor
public class PurchaseRecordDomainImpl implements PurchaseRecordDomain {
    private final PurchaseRecordRepository repository;
    private final PurchaseRecordAssembler assembler;

    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    @Override
    public PurchaseRecordVO detail(Long id) {
        return repository.detail(id);
    }

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public List<PurchaseRecordVO> queryList(PurchaseRecordQuery query) {
        return repository.queryList(query);
    }

    /**
     * 新增数据
     *
     * @param saveCommand 新增实体
     */
    @Override
    public Long add(PurchaseRecordReq saveCommand) {
        return repository.insert(assembler.req2VO(saveCommand));
    }

    /**
     * 修改数据
     *
     * @param saveCommand 编辑实体
     */
    @Override
    public void edit(PurchaseRecordReq saveCommand) {
        PurchaseRecordQuery query = new PurchaseRecordQuery();
        query.setId(saveCommand.getId());
        repository.edit(assembler.req2VO(saveCommand), query);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    @Override
    public void del(Long id) {
        repository.del(id);
    }

    /**
     * 查询分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    @Override
    public List<PurchaseRecordVO> queryPage(PurchaseRecordQuery query) {
        return repository.queryPage(query);
    }
}

