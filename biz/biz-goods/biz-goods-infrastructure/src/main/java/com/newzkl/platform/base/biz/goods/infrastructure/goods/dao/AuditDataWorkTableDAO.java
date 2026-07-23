package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.AuditDataWorkTableDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataWorkTableQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 工单审核数据
 *
 * @author fang
 */
@Mapper
@Repository
public interface AuditDataWorkTableDAO extends BaseMapper<AuditDataWorkTableDO> {
    /**
     * 构建查询条件的 QueryWrapper
     */
    default QueryWrapper<AuditDataWorkTableDO> buildQueryWrapper(AuditDataWorkTableQuery query) {
        QueryWrapper<AuditDataWorkTableDO> wrapper = new QueryWrapper<>();

        // id 条件
        wrapper.eq(query.getId() != null, "id", query.getId());

        // idList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getIdList()), "id", query.getIdList());

        // accountId 条件
        wrapper.eq(query.getAccountId() != null, "account_id", query.getAccountId());

        wrapper.like(query.getSpuName() != null, "spu_name", query.getSpuName());
        wrapper.eq(query.getSpuId() != null, "spu_id", query.getSpuId());
        wrapper.eq(query.getOperateType() != null, "operate_type", query.getOperateType());
        wrapper.eq(query.getOperateTarget() != null, "operate_target", query.getOperateTarget());
        wrapper.eq(query.getState() != null, "state", query.getState());
        wrapper.eq(query.getState() != null, "account_id", query.getAccountId());
        wrapper.in(CollectionUtils.isNotEmpty(query.getStateList()), "state", query.getStateList());
        wrapper.orderByDesc("id");
        return wrapper;
    }
}