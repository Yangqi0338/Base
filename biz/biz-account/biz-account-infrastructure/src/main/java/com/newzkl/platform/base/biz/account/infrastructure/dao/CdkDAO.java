package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.CdkDO;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开通码 DAO。
 *
 * <p>与商品域 {@code biz-goods} 的同名 DAO 无关: 本 DAO 面向 {@code cdk} 表 (角色/门店开通码)。</p>
 *
 * @author KC
 */
@Mapper
public interface CdkDAO extends BaseMapper<CdkDO> {

    /**
     * 构建开通码查询条件。
     *
     * <p>对齐旧 mapper {@code CdkDAO.xml} 的 where 片段: {@code value} 模糊匹配,
     * {@code valueList} 精确 in, {@code toState1} 为 0 时限定未发放, 非 0 时限定已发放
     * ({@code to_state > 0}), 其余字段等值匹配, 按 ID 倒序。</p>
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<CdkDO> getLw(CdkQuery query) {
        BaseLambdaQueryWrapper<CdkDO> wrapper = new BaseLambdaQueryWrapper<CdkDO>()
                .notEmptyIn(CdkDO::getId, query.getIdList())
                .notNullEq(CdkDO::getBelowRole, query.getBelowRole())
                .notNullEq(CdkDO::getOperatorId, query.getOperatorId())
                .notNullEq(CdkDO::getDealerId, query.getDealerId())
                .notNullEq(CdkDO::getChannelId, query.getChannelId())
                .notNullEq(CdkDO::getUseState, query.getUseState())
                .notNullEq(CdkDO::getToState, query.getToState())
                .notNullEq(CdkDO::getSystemType, query.getSystemType())
                .notEmptyIn(CdkDO::getSystemType, query.getSystemTypeList())
                .notNullEq(CdkDO::getOrderId, query.getOrderId())
                .notNullEq(CdkDO::getGetType, query.getGetType())
                .notEmptyIn(CdkDO::getValue, query.getValueList())
                .notEmptyLike(CdkDO::getValue, query.getValue())
                .notEmptyLt(CdkDO::getCreateTime, query.getLessCreateTime());
        Integer toState1 = query.getToState1();
        if (toState1 != null) {
            if (toState1 == 0) {
                wrapper.eq(CdkDO::getToState, 0);
            } else {
                wrapper.gt(CdkDO::getToState, 0);
            }
        }
        wrapper.orderByDesc(CdkDO::getId);
        return wrapper;
    }
}
