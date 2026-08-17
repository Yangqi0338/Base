package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuDO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsCountVO;
import com.newzkl.platform.base.common.ddd.facade.SpuCountQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * spu
 *
 * @author fang
 */
@Mapper
@Repository
public interface SpuDAO extends BaseMapper<SpuDO> {

    List<SpuCategoryVO> countSpuByCategory(@Param("categoryIdList") List<Long> categoryIdList);

    void editColumn(@Param("id") Long id, @Param("columnList") List<EditColumnVO> editColumnDTOS);

    List<GroupCountRes> spuCount(@Param("query") TimeQuery timeQuery);

    GoodsCountVO goodsCountVO(@Param("supplierId") Long supplierId);

    List<SpuDO> listQuery(@Param("ew") QueryWrapper<SpuDO> queryWrapper);

    List<Map<String, Object>> countByCondition(@Param("query") SpuCountQuery countQuery);

    Page<SpuVO> voPageByQuery(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<SpuDO> queryWrapper);

    /**
     * 构建 SPU 查询条件的 QueryWrapper
     */
    default QueryWrapper<SpuDO> buildQueryWrapper(SpuQuery query) {
        QueryWrapper<SpuDO> wrapper = new QueryWrapper<>();

        // idList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getIdList()), "t.id", query.getIdList());

        // supplierIdList 条件 (映射到 account_id)
        wrapper.in(CollectionUtils.isNotEmpty(query.getSupplierIdList()), "account_id", query.getSupplierIdList());

        // codeList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getCodeList()), "code", query.getCodeList());

        // name 模糊查询
        wrapper.like(StringUtils.isNotBlank(query.getName()), "name", query.getName());

        // title 模糊查询
        wrapper.like(StringUtils.isNotBlank(query.getTitle()), "title", query.getTitle());

        // brandId 条件
        wrapper.eq(query.getBrandId() != null, "brand_id", query.getBrandId());

        // freightTemplateId 条件
        wrapper.eq(query.getFreightTemplateId() != null, "freight_template_id", query.getFreightTemplateId());

        // deliverTimeType 条件
        wrapper.eq(query.getDeliverTimeType() != null, "deliver_time_type", query.getDeliverTimeType());

        // auditStateList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getAuditStateList()), "audit_state", query.getAuditStateList());

        // accountId 条件
        wrapper.eq(query.getAccountId() != null, "account_id", query.getAccountId());

        // categoryIdList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getCategoryIdList()), "category_id", query.getCategoryIdList());

        // stateList 条件
        wrapper.in(CollectionUtils.isNotEmpty(query.getStateList()), "state", query.getStateList());

        // supplyPrice 价格区间
        if (query.getSupplyPriceStart() != null && query.getSupplyPriceEnd() != null) {
            wrapper.ge("supply_price", query.getSupplyPriceStart())
                    .le("supply_price", query.getSupplyPriceEnd());
        }

        // channelType 条件
        wrapper.eq(query.getChannelType() != null, "channel_type", query.getChannelType());

        // roleId 条件
        wrapper.eq(query.getRole() != null, "role", query.getRole());

        // outSpuId 条件
        wrapper.eq(StringUtils.isNotBlank(query.getOutSpuId()), "out_spu_id", query.getOutSpuId());

        return wrapper;
    }
}