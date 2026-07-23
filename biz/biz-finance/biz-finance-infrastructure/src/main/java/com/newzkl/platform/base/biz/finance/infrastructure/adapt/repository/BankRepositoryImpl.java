package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.BankRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.BankBranchDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.BankDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.BankBranchDO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.BankDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.BankQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankBranchVO;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 银行(Bank)存储实现
 *
 * @author kc
 * @since 2025-09-18 11:02:23
 */
@Repository
@RequiredArgsConstructor
public class BankRepositoryImpl implements BankRepository {

    private final BankDAO bankDAO;
    private final BankBranchDAO branchDAO;

    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    @Override
    public BankVO detail(Long id) {
        BankDO bankDO = bankDAO.selectById(id);
        BankVO bank = TransferUtils.transfer(bankDO, BankVO::new);
        if (bank != null) {

        }
        return bank;
    }

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public List<BankVO> queryPage(BankQuery query) {
        LambdaQueryWrapper<BankDO> queryWrapper = bankDAO.getLw(query);
        Page<BankDO> pageList = bankDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transfers(pageList.getRecords(), BankVO.class);
    }

    /**
     * 批量
     *
     * @param bankList 新增实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertList(List<BankVO> bankList) {
        List<BankDO> bankDOList = TransferUtils.transfers(bankList, BankDO.class);
        bankDAO.insert(bankDOList);
    }

    /**
     * 批量
     *
     * @param bankBranchList 新增实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBranchList(List<BankBranchVO> bankBranchList) {
        List<BankBranchDO> bankDOList = TransferUtils.transfers(bankBranchList, BankBranchDO.class);
        branchDAO.insert(bankDOList);
    }

    /**
     * 修改数据
     *
     * @param bank  编辑实体
     * @param query 编辑查询
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(BankVO bank, BankQuery query) {
        LambdaQueryWrapper<BankDO> queryWrapper = bankDAO.getLw(query);
        if (bankDAO.exists(queryWrapper)) {
            throw new ScmException(BaseErrorCode.INVALID_UPDATE);
        }

        BankDO bankDO = TransferUtils.transfer(bank, BankDO::new);
        int effectRows = bankDAO.update(bankDO, queryWrapper);
        // 修改成功
        if (effectRows > 0) {

        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        bankDAO.deleteById(id);
    }

    @Override
    public List<BankBranchVO> queryBranchPage(BankQuery query) {
        LambdaQueryWrapper<BankBranchDO> queryWrapper = branchDAO.getLw(query);
        Page<BankBranchDO> pageList = branchDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transfers(pageList.getRecords(), BankBranchVO.class);
    }
}

