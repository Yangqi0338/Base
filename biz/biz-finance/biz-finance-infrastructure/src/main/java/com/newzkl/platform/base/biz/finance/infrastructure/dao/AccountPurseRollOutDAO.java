package com.newzkl.platform.base.biz.finance.infrastructure.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountPurseRollOutDO;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyAuditReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyQuery;
import com.newzkl.platform.base.biz.finance.model.purse.vo.RollOutApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AccountPurseRollOutDAO继承基类
 */
@Mapper
public interface AccountPurseRollOutDAO extends BaseMapper<AccountPurseRollOutDO> {

    default LambdaQueryWrapper<AccountPurseRollOutDO> getLw(RollOutApplyQuery query) {
        LambdaQueryWrapper<AccountPurseRollOutDO> queryWrapper = new BaseLambdaQueryWrapper<AccountPurseRollOutDO>()
                .notEmptyEq(AccountPurseRollOutDO::getAccountId, query.getAccountId())
                .notEmptyEq(AccountPurseRollOutDO::getAccountType, query.getAccountType())
                .notEmptyEq(AccountPurseRollOutDO::getPurseType, query.getPurseType())
                .notEmptyIn(AccountPurseRollOutDO::getAuditState, query.getAuditStateList())
                .notEmptyEq(AccountPurseRollOutDO::getId, query.getId());
        return queryWrapper;
    }

    /**
     * 查询fro update
     *
     * @param id
     * @return
     */
    AccountPurseRollOutDO selectForUpdateById(@Param("id") Long id);

    /**
     * 更新转出申请主键流水号 三方打款余额不足时使用
     *
     * @param id
     * @param newId
     */
    void alterRollOutApplyId(@Param("id") Long id, @Param("newId") Long newId);

    /**
     * 查询转出申请
     *
     * @param query
     * @return
     */
    List<RollOutApplyVO> queryWithdrawRecords(RollOutApplyQuery query);

    /**
     * 更新转出申请三方到账结果
     *
     * @param applyId
     * @param tripartiteState
     * @param tripartiteTradeNo
     * @return
     */
    int alterRollOutTripartiteState(@Param("applyId") Long applyId, @Param("tripartiteState") Integer tripartiteState, @Param("tripartiteTradeNo") String tripartiteTradeNo);

    /**
     * 转出申请审核
     *
     * @param req
     * @return
     */
    int rollOutApplyAudit(RollOutApplyAuditReq req);
}
