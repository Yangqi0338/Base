package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.SupplierRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.SupplierDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.SupplierDO;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.vo.SupplierAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 供应商
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class SupplierRepositoryImpl extends RepositorySupport implements SupplierRepository {

    private final SupplierDAO supplierDAO;

    @Override
    public Long supplierSave(SupplierVO supplier) {
        SupplierDO supplierDO = TransferUtils.transfer(supplier, SupplierDO.class);
        supplierDAO.insert(supplierDO);
        return supplierDO.getId();
    }

    @Override
    public int supplierEdit(SupplierVO supplier) {
        SupplierDO supplierDO = TransferUtils.transfer(supplier, SupplierDO.class);
        return supplierDAO.updateById(supplierDO);
    }

    @Override
    public int supplierDelete(List<Long> supplierIdList) {
        return supplierDAO.deleteByIds(supplierIdList);
    }

    @Override
    public void supplierEdit(List<EditColumnVO> columnList, Long id) {
        SupplierQuery query = new SupplierQuery();
        query.setId(id);
        supplierDAO.columnByQuery(columnList, supplierDAO.getLw(query));
    }

    @Override
    public SupplierVO supplier(Long supplierId) {
        SupplierDO supplierDO = supplierDAO.selectById(supplierId);
        return TransferUtils.transfer(supplierDO, SupplierVO.class);
    }

    @Override
    public List<Long> idByQuery(SupplierQuery query) {
        return listOneField(supplierDAO, supplierDAO.getLw(query), SupplierDO::getId);
    }

    @Override
    public Page<SupplierVO> pageList(SupplierQuery query) {
        return pageObj(query, SupplierVO.class);
    }

    @Override
    public <T> Page<T> pageObj(SupplierQuery query, Class<T> clazz) {
        return TransferUtils.transferPage(supplierDAO.selectPage(RepositorySupport.page(query), supplierDAO.getLw(query)), clazz);
    }

    @Override
    public Page<SupplierAccountVO> pageListWithAccount(SupplierQuery query) {
        BaseQueryWrapper<SupplierDO> joinQw = supplierDAO.getJoinQw(query);
        return supplierDAO.pageListWithAccount(RepositorySupport.page(query), joinQw);
    }

    @Override
    public SettlementConfigVO getSettlementConfig(Long accountId) {
        return findById2OneField(supplierDAO, accountId, SupplierDO::getPeriodSetConfig);
    }

    @Override
    public Long selectCount(SupplierQuery query) {
        query.addCountField();
        BizCountMap countMap = supplierDAO.countWithAccountByCondition(query, supplierDAO.getJoinQw(query));
        return countMap.getCount(0);
    }

    @Override
    public List<SupplierVO> listByIdList(List<Long> idList) {
        List<SupplierDO> supplierList =
                Optional.ofNullable(supplierDAO.selectByIds(idList)).orElse(Collections.emptyList());
        return TransferUtils.transfers(supplierList, SupplierVO.class);
    }
}
