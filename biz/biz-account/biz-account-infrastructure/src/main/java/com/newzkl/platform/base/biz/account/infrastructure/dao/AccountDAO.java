package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.biz.account.infrastructure.entity.AccountDO;
import com.newzkl.platform.base.biz.account.model.req.AccountQuery;
import com.newzkl.platform.base.biz.account.model.req.SimpleAccountQuery;
import com.newzkl.platform.base.biz.account.model.res.AccountOutRes;
import com.newzkl.platform.base.biz.account.model.res.SimpleAccountRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountRPCResVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户账号
 *
 * @author fang
 */
@Mapper
public interface AccountDAO extends BaseMapper<AccountDO> {

    default BaseLambdaQueryWrapper<AccountDO> getLw(AccountQuery query) {
        return new BaseLambdaQueryWrapper<AccountDO>()
                .notEmptyEq(AccountDO::getPhone, query.getPhone())
                .notEmptyLike(AccountDO::getNickname, query.getNickname())
                .notEmptyIn(AccountDO::getInviteAccountId, query.getInviteAccountIdList())
                .notEmptyEq(AccountDO::getPid, query.getPid())
                .notEmptyIn(AccountDO::getUsername, query.getUsernameList())
                .notEmptyLike(AccountDO::getUsername, query.getUsername())
                .notEmptyLike(AccountDO::getRealName, query.getRealName())
                .likeList(query.getSearch(), AccountDO::getUsername, AccountDO::getNickname, AccountDO::getRealName)
                .notEmptyIn(AccountDO::getId, query.getIdList())
                .notEmptyEq(AccountDO::getState, query.getState())
                .notEmptyGe(AccountDO::getState, query.getStateOver())
                .likeList(AccountDO::getIdentityList, query.getIdentityList())
                .notEmptyEq(AccountDO::getYqm, query.getYqm())
                .notEmptyEq(AccountDO::getClient, query.getClient())
                .notEmptyGe(AccountDO::getCancelTime, query.getCancelTime())
                ;
    }

    BizCountMap countByCondition(@Param("query") AccountQuery query, @Param("ew") BaseLambdaQueryWrapper<AccountDO> queryWrapper);

    AccountDO accountByPidAndUserName(@Param("pid") Long pid, @Param("username") String username);

    List<GroupCountRes> groupCount(@Param("query") TimeQuery timeQuery);

    List<SimpleAccountRes> simpleAccountPage(@Param("query") SimpleAccountQuery accountQuery);

    List<AccountStructureVO> findScopeSubAccountStructure(@Param("accountId") Long accountId);

    List<Long> selectSonIdList(@Param("id") Long id);

    /**
     * 根据账号查询账号
     *
     * @param userAccount
     * @return
     */
    AccountDO selectByUserAccount(@Param("userAccount") String userAccount);

    /**
     * 分页查询AccountDO列表（检索条件：昵称/账号/手机号/状态）
     *
     * @param query 分页+检索条件
     * @return 分页后的AccountDO列表
     */
    List<AccountDO> pageAccountDO(@Param("query") AccountQuery query);

    /**
     * 统计符合条件的AccountDO总数量（与pageAccountDO检索条件一致）
     *
     * @param query 检索条件
     * @return 总数量
     */
    Long countAccountDO(@Param("query") AccountQuery query);

    /**
     * 根据ID列表批量查询账号信息（解决N+1查询问题）
     * @param ids 账号ID列表
     * @return 账号VO列表
     */
    List<AccountVO> listAccountByIds(@Param("ids") List<Long> ids);
}
