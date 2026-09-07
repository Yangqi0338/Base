package com.newzkl.platform.base.biz.account.domain.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.vo.SupplierAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigVO;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;

import java.util.List;

/**
 * 供应商
 *
 * @author fang
 */
public interface SupplierRepository {

    Long supplierSave(SupplierVO supplier);

    int supplierEdit(SupplierVO supplier);

    int supplierDelete(List<Long> supplierIdList);

    void supplierEdit(List<EditColumnVO> columnList, Long id);

    SupplierVO supplier(Long supplierId);

    List<Long> idByQuery(SupplierQuery supplierQuery);

    /**
     * 分页列表
     */
    Page<SupplierVO> pageList(SupplierQuery supplierQuery);

    /**
     * 任意返回参的分页列表
     */
    <T> Page<T> pageObj(SupplierQuery supplierQuery, Class<T> clazz);

    /**
     * 带账户信息的分页列表
     */
    Page<SupplierAccountVO> pageListWithAccount(SupplierQuery query);

    SettlementConfigVO getSettlementConfig(Long accountId);

    Long selectCount(SupplierQuery query);

    /**
     * 按主键批量查供应商身份行
     *
     * @param idList 供应商 (账号) ID 列表
     * @return 供应商视图列表, 无则空列表
     */
    List<SupplierVO> listByIdList(List<Long> idList);
}
