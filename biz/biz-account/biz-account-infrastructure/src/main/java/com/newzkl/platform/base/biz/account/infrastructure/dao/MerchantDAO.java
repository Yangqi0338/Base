package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.MerchantDO;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户 DAO。
 *
 * @author KC
 */
@Mapper
public interface MerchantDAO extends BaseMapper<MerchantDO> {

    /**
     * 构建商户查询条件。
     *
     * <p>与旧 mapper {@code MerchantDAO.xml} 的 where 片段一致: id / idList /
     * name / username 均为精确匹配。</p>
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<MerchantDO> getLw(MerchantQuery query) {
        BaseLambdaQueryWrapper<MerchantDO> wrapper = new BaseLambdaQueryWrapper<MerchantDO>()
                .notEmptyIn(MerchantDO::getId, query.getIdList())
                .notNullEq(MerchantDO::getName, query.getName())
                .notNullEq(MerchantDO::getUsername, query.getUsername());
        wrapper.orderByDesc(MerchantDO::getCreateTime);
        return wrapper;
    }
}
