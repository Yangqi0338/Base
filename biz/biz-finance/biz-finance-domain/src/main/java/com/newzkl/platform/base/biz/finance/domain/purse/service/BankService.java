package com.newzkl.platform.base.biz.finance.domain.purse.service;


import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 银行(bank)存储接口
 *
 * @author kc
 * @since 2025-09-18 11:27:10
 */
public interface BankService {
    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    BankVO detail(Long id);

    /**
     * 新增数据
     *
     * @param saveCommand 新增实体
     */
    Long add(BankReq saveCommand);

    /**
     * 新增多条数据
     *
     * @param saveCommandList 新增实体
     */
    void addList(List<BankReq> saveCommandList);

    /**
     * 修改数据
     *
     * @param saveCommand 编辑实体
     */
    void edit(BankReq saveCommand);

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
    List<BankVO> queryPageList(BankQuery query);

    /**
     * 查询分行分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    List<BankBranchVO> queryBranchPageList(BankQuery query);

    ScmResult<Object> huiFuImportExcel(MultipartFile file);
}

