package com.newzkl.platform.base.biz.finance.domain.purse.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
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
    Page<BankVO> queryPageList(BankQuery query);

    /**
     * 查询银行全量列表, 不分页
     *
     * @param query 查询条件
     * @return 银行列表
     */
    List<BankVO> queryList(BankQuery query);

    /**
     * 查询分行分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    Page<BankBranchVO> queryBranchPageList(BankQuery query);

    /**
     * 查询银行支行全量列表, 不分页
     *
     * <p>银行编码为必传项, 与 new-scm 源实现的入参校验保持一致。</p>
     *
     * @param query 查询条件
     * @return 银行支行列表
     */
    List<BankBranchVO> queryBranchList(BankQuery query);

    /**
     * 汇付银行/支行 Excel 批量导入
     *
     * @param file 银行导入 Excel 文件
     * @return 导入结果, 含解析错误信息
     * @deprecated 运维一次性导入工具。对外 HTTP 入口 {@code PUT /bank/importBankAndBranch}
     * 已于 2026-07-27 死端点清理中删除(前端 7 仓零引用), 本方法暂留供运维脚本/临时接线调用。
     */
    @Deprecated
    PlatformResult<Boolean> huiFuImportExcel(MultipartFile file);
}

