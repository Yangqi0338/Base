package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.AuditDataSpuDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataSpuQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品上传审核数据
 *
 * @author fang
 */
@Mapper
@Repository
public interface AuditDataSpuDAO extends BaseMapper<AuditDataSpuDO> {
    /**
     * 查询条件的 QueryWrapper
     */
    default QueryWrapper<AuditDataSpuDO> buildQueryWrapper(AuditDataSpuQuery query) {
        QueryWrapper<AuditDataSpuDO> wrapper = new QueryWrapper<>();

        // id 条件
        wrapper.eq(query.getId() != null, "id", query.getId());

        // idList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getIdList()), "id", query.getIdList());

        // accountId 条件
        wrapper.eq(query.getAccountId() != null, "account_id", query.getAccountId());


        wrapper.in(CollectionUtils.isNotEmpty(query.getSpuIdList()), "spu_id", query.getSpuIdList());
        wrapper.eq(query.getState() != null, "state", query.getState());
        wrapper.in(CollectionUtils.isNotEmpty(query.getStateList()), "state", query.getStateList());
        wrapper.eq(query.getCategoryId() != null, "category_id", query.getCategoryId());
        wrapper.like(query.getName() != null, "name", query.getName());
        wrapper.like(query.getBrandName() != null, "brandName", query.getBrandName());
        wrapper.eq(query.getIsNew() != null, "isNew", query.getIsNew());
        wrapper.between(query.getSupplyPriceStart() != null && query.getSupplyPriceEnd() != null, "supply_price", query.getSupplyPriceStart(), query.getSupplyPriceEnd());
        return wrapper;
    }
}