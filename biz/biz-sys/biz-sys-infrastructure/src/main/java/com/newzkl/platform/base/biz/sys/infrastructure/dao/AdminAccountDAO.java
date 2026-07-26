package com.newzkl.platform.base.biz.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AdminAccountDO;
import com.newzkl.platform.base.biz.sys.model.adminaccount.query.AdminAccountQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台账号 DAO。
 *
 * @author KC
 */
@Mapper
public interface AdminAccountDAO extends BaseMapper<AdminAccountDO> {

    /**
     * 构建账号查询条件。
     *
     * <p>迁移说明: 旧 mapper xml 用 {@code arole_id_list like %roleId%} 做角色过滤,
     * 这里以 {@link BaseLambdaQueryWrapper#notEmptyLike} 等价保留。</p>
     *
     * @param query 账号查询
     * @return 查询条件
     */
    default LambdaQueryWrapper<AdminAccountDO> getLw(AdminAccountQuery query) {
        return new BaseLambdaQueryWrapper<AdminAccountDO>()
                .notEmptyIn(AdminAccountDO::getId, query.getIdList())
                .notEmptyLike(AdminAccountDO::getNickname, query.getNickname())
                .notEmptyLike(AdminAccountDO::getUsername, query.getUsername())
                .notEmptyLike(AdminAccountDO::getPhone, query.getPhone())
                .notNullEq(AdminAccountDO::getState, query.getState())
                .notEmptyLike(AdminAccountDO::getAroleIdList, query.getRoleId())
                .orderByDesc(AdminAccountDO::getCreateTime);
    }
}
