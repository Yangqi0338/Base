package com.newzkl.platform.base.biz.finance.domain.purse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.BankRepository;
import com.newzkl.platform.base.biz.finance.domain.purse.service.BankService;
import com.newzkl.platform.base.biz.finance.model.assembler.BankAssembler;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankExcelVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.ddd.model.ScmResult;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelError;
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
     * @return 分页列表
     */
    @Override
    public List<BankVO> queryPageList(BankQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public List<BankBranchVO> queryBranchPageList(@Valid BankQuery query) {
        return repository.queryBranchPage(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ScmResult<Object> huiFuImportExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 4.2.0 版本 ExcelImportUtil.importExcel 需传入 3 个参数
            EasyExcelError easyExcelError = EasyExcelUtil.importBiz(
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
            if (easyExcelError.getErrorCount() > 0) {
                return ScmResult.fail(easyExcelError.getErrorMsg());
            } else {
                return ScmResult.success();
            }
        } catch (Exception e) {
            return ScmResult.fail("文件解析异常：" + e.getMessage());
        }
    }
}

