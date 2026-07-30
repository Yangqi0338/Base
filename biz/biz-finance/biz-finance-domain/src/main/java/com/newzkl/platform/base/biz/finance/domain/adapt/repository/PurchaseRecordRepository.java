package com.newzkl.platform.base.biz.finance.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;

import java.util.List;

/**
 * 购买记录 (purchase_record)存储接口
 *
 * @author kc
 * @since 2025-11-25 17:24:23
 */
public interface PurchaseRecordRepository {
    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    PurchaseRecordVO detail(Long id);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<PurchaseRecordVO> queryList(PurchaseRecordQuery query);

    /**
     * 查询分页
     *
     * @param query 查询条件
     * @return 购买记录分页
     */
    Page<PurchaseRecordVO> queryPage(PurchaseRecordQuery query);

    /**
     * 新增数据
     *
     * @param purchaseRecord 新增实体
     */
    Long insert(PurchaseRecordVO purchaseRecord);

    /**
     * 修改数据
     *
     * @param purchaseRecord 编辑实体
     * @param query          编辑查询
     */
    void edit(PurchaseRecordVO purchaseRecord, PurchaseRecordQuery query);

    /**
     * 删除
     *
     * @param id 主键
     */
    void del(Long id);

}

