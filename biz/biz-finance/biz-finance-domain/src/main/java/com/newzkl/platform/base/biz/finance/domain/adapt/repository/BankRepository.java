package com.newzkl.platform.base.biz.finance.domain.adapt.repository;


import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;

import java.util.List;

/**
 * 银行(bank)存储接口
 *
 * @author kc
 * @since 2025-09-18 10:58:55
 */
public interface BankRepository {
    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    BankVO detail(Long id);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<BankVO> queryPage(BankQuery query);

    /**
     * 新增数据
     *
     * @param bankList 新增实体
     */
    void insertList(List<BankVO> bankList);

    /**
     * 新增分行数据
     *
     * @param bankBranchList 新增实体
     */
    void insertBranchList(List<BankBranchVO> bankBranchList);

    /**
     * 修改数据
     *
     * @param bank  编辑实体
     * @param query 编辑查询
     */
    void edit(BankVO bank, BankQuery query);

    /**
     * 删除
     *
     * @param id 主键
     */
    void del(Long id);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    List<BankBranchVO> queryBranchPage(BankQuery query);
}

