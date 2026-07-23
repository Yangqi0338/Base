package com.newzkl.platform.base.biz.finance.domain.pay.service;

import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;

import java.util.List;

/**
 * 购买记录 (purchase_record)存储接口
 *
 * @author kc
 * @since 2025-11-25 17:24:24
 */
public interface PurchaseRecordDomain {
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
     * 新增数据
     *
     * @param saveCommand 新增实体
     */
    Long add(PurchaseRecordReq saveCommand);

    /**
     * 修改数据
     *
     * @param saveCommand 编辑实体
     */
    void edit(PurchaseRecordReq saveCommand);

    /**
     * 删除
     *
     * @param id 主键
     */
    void del(Long id);

    /**
     * 查询分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    List<PurchaseRecordVO> queryPage(PurchaseRecordQuery query);

}

