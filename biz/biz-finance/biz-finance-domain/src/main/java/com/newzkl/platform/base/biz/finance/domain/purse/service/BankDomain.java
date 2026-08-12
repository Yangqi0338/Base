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
public interface BankDomain {
    /**
     * 新增多条数据
     *
     * @param saveCommandList 新增实体
     */
    void addList(List<BankReq> saveCommandList);

    /**
     * 查询分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    Page<BankVO> queryPageList(BankQuery query);

    /**
     * 查询分行分页列表
     *
     * @param query 查询条件
     * @return 分页列表
     */
    Page<BankBranchVO> queryBranchPageList(BankQuery query);

    /**
     * 汇付银行/支行 Excel 批量导入
     *
     * @param file 银行导入 Excel 文件
     * @return 导入结果, 含解析错误信息
     * @deprecated 运维一次性导入工具。对外 HTTP 入口 {@code PUT /bank/importBankAndBranch}
     * 已于 2026-07-27 死端点清理中删除(前端 7 仓零引用), 本方法暂留供运维脚本/临时接线调用。
     */
    PlatformResult<Boolean> huiFuImportExcel(MultipartFile file);
}

