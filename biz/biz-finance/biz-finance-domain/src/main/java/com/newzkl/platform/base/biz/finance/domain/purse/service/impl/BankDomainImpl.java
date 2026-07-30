package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.BankRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.BankService;
import com.newzkl.platform.base.biz.finance.model.assembler.BankAssembler;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankExcelVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.EasyExcelUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 银行(Bank)存储实现
 *
 * @author kc
 * @since 2025-09-18 11:29:44
 */
@Service
@RequiredArgsConstructor
public class BankDomainImpl implements BankService {
    private final BankRepository repository;
    private final BankAssembler assembler;

    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    @Override
    public BankVO detail(Long id) {
        return repository.detail(id);
    }

    /**
     * 新增数据
     *
     * @param saveCommand 新增实体
     */
    @Override
    public Long add(BankReq saveCommand) {
        repository.insertList(CollUtil.newArrayList(assembler.req2VO(saveCommand)));
        return saveCommand.getId();
    }

    @Override
    public void addList(List<BankReq> saveCommandList) {
        repository.insertList(assembler.req2VO(saveCommandList));

        repository.insertBranchList(saveCommandList.stream().flatMap(it -> it.getBranchList().stream()).collect(Collectors.toList()));
    }

    /**
     * 修改数据
     *
     * @param saveCommand 编辑实体
     */
    @Override
    public void edit(BankReq saveCommand) {
        BankQuery query = new BankQuery();
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
     * @return 银行分页
     */
    @Override
    public Page<BankVO> queryPageList(BankQuery query) {
        return repository.queryPage(query);
    }

    /**
     * 查询银行全量列表, 不分页
     *
     * @param query 查询条件
     * @return 银行列表
     */
    @Override
    public List<BankVO> queryList(BankQuery query) {
        return repository.queryList(query);
    }

    /**
     * 查询分行分页列表
     *
     * @param query 查询条件
     * @return 银行支行分页
     */
    @Override
    public Page<BankBranchVO> queryBranchPageList(@Valid BankQuery query) {
        return repository.queryBranchPage(query);
    }

    /**
     * 查询银行支行全量列表, 不分页
     *
     * @param query 查询条件
     * @return 银行支行列表
     */
    @Override
    public List<BankBranchVO> queryBranchList(BankQuery query) {
        if (StrUtil.isBlank(query.getBankCode())) {
            throw new PlatformException(BaseErrorCode.PARAM, "银行编码必传");
        }
        return repository.queryBranchList(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PlatformResult<Boolean> huiFuImportExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 4.2.0 版本 ExcelImportUtil.importExcel 需传入 3 个参数
            EasyExcelErrorVO easyExcelError = EasyExcelUtil.importBiz(
                    inputStream,
                    BankExcelVO.class,
                    list -> {
                        List<BankReq> reqList = new ArrayList<>();
                        list.stream().collect(Collectors.groupingBy(BankExcelVO::getBankCode)).forEach((bankCode, bankList) -> {
                            BankExcelVO baseBank = CollUtil.getFirst(bankList);

                            BankReq bank = new BankReq();
                            bank.setBankCode(bankCode);
                            bank.setBankName(baseBank.getBankName());

                            List<BankBranchVO> branchList = bankList.stream().map(bankExcelVO -> {
                                BankBranchVO bankBranch = new BankBranchVO();

                                bankBranch.setBankCode(bankCode);
                                bankBranch.setBranchCode(bankExcelVO.getBranchCode());
                                bankBranch.setBranchName(bankExcelVO.getBranchName());
                                return bankBranch;
                            }).collect(Collectors.toList());
                            bank.setBranchList(branchList);
                            reqList.add(bank);
                        });
                        addList(reqList);
                    }
            );
            if (easyExcelError.errorCount() > 0) {
                return PlatformResult.fail(easyExcelError.errorMsg());
            } else {
                return PlatformResult.success();
            }
        } catch (Exception e) {
            return PlatformResult.fail("文件解析异常：" + e.getMessage());
        }
    }
}

