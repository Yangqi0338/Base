package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountPurseAlterRecordDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AccountPurseAlterRecordDAO继承基类
 */
@Mapper
public interface AccountPurseAlterRecordDAO extends BaseMapper<AccountPurseAlterRecordDO> {

    default LambdaQueryWrapper<AccountPurseAlterRecordDO> getLw(AccountPurseAlterRecordQuery query) {
        LambdaQueryWrapper<AccountPurseAlterRecordDO> queryWrapper = new BaseLambdaQueryWrapper<AccountPurseAlterRecordDO>()
                .notEmptyEq(AccountPurseAlterRecordDO::getAccountId, query.getAccountId())
                .notEmptyEq(AccountPurseAlterRecordDO::getPurseType, query.getPurseType())
                .notEmptyIn(AccountPurseAlterRecordDO::getAlterType, query.getAlterTypeList())
                .notEmptyEq(AccountPurseAlterRecordDO::getEarningAlterType, query.getEarningAlterType())
                .between(AccountPurseAlterRecordDO::getCreateTime, query.getCreateTime())
                .eq(AccountPurseAlterRecordDO::getAccountType, query.getAccountType())
                .orderByDesc(AccountPurseAlterRecordDO::getId);
        return queryWrapper;
    }

    /**
     * 保存客户账户变动记录
     *
     * @param accountPurseAlterRecords
     */
    void saveAccountPurseAlterRecord(@Param("list") List<AccountPurseAlterRecordVO> accountPurseAlterRecords);

    /**
     * 查询客户账户变动记录
     *
     * @param req
     * @return
     */
    List<AccountPurseAlterRecordVO> queryAccountPurseAlterRecords(AccountPurseAlterRecordQuery req);

    /**
     * 查询供应商结算数据
     *
     * @param remark
     * @return
     */
    Integer querySupplierSettleData(@Param("remark") Integer remark);

    /**
     * 查询渠道商提现记录
     *
     * <p>变动记录表 与 提现申请表(审核中) 的 union all, 按创建时间倒序</p>
     *
     * @param page 分页参数
     * @param query 查询条件
     * @return 提现记录分页
     */
    Page<AccountPurseAlterRecordVO> queryChannelRollOutRecords(Page<?> page, @Param("query") AccountPurseAlterRecordQuery query);
}
