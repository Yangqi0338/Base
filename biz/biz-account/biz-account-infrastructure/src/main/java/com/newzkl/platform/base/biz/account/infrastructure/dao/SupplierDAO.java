package com.newzkl.platform.base.biz.account.infrastructure.dao;
import com.newzkl.platform.base.common.core.mybatis.support.BaseQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.SupplierDO;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.biz.account.model.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 供应商
 *
 * @author fang
 */
@Mapper
public interface SupplierDAO extends BaseMapper<SupplierDO> {


    List<SupplierRelationVO> supplierRelationVO(@Param("supplierIdList") List<Long> supplierIdList);

    void changeColumn(@Param("id") Long id, @Param("fromColumn") String fromColumn, @Param("toColumn") String toColumn);

    void resetColumn(@Param("id") Long id, @Param("columnList") List<String> columnList);

    List<SupplierDescVO> supplierDescVOList(@Param("supplierIdList") List<Long> supplierIdList);

    void resetUserOrderCount();

    String getSettlementConfig(@Param("id") Long accountId);


    default BaseLambdaQueryWrapper<SupplierDO> getLw(SupplierQuery query) {
        return new BaseLambdaQueryWrapper<SupplierDO>()
                .notEmptyIn(SupplierDO::getId, query.getIdList())
                .notEmptyIn(SupplierDO::getState, query.getStateList())
                .notEmptyEq(SupplierDO::getPromisePayState, query.getPromisePayState())
                .notEmptyEq(SupplierDO::getPeriodSetState, query.getPeriodSetState())
                .between(SupplierDO::getCreateTime, query.getCreateTime())
                .notEmptyLike(SupplierDO::getIndustryIdList, query.getIndustryId())
                .notEmptyEq(SupplierDO::getCompanyAreaCode, query.getCompanyAreaCode())
                .notEmptyLike(SupplierDO::getName, query.getCompanyName())
                .notEmptyIn(SupplierDO::getAuditState, query.getAuditStateList())
                ;
    }

    default BaseQueryWrapper<SupplierDO> getJoinQw(SupplierQuery query) {
        return getLw(query)
                .unwrapAlias()
                .notEmptyLike("a.username", query.getUsername())
                .notEmptyEq("a.inviteid_account_id", query.getInviteId())
                ;
    }

    /**
     * 和account进行关联查询
     * account alias a. supplier not alias
     */
    BizCountMap countWithAccountByCondition(@Param("query") SupplierQuery query, @Param(Constants.WRAPPER) BaseQueryWrapper<SupplierDO> queryWrapper);

    int columnByQuery(@Param("columnList") List<EditColumnVO> columnList, @Param(Constants.WRAPPER) BaseLambdaQueryWrapper<SupplierDO> queryWrapper);

    Page<SupplierAccountVO> pageListWithAccount(Page<Object> page, @Param(Constants.WRAPPER) BaseQueryWrapper<SupplierDO> queryWrapper);
}
