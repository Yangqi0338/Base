package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuDO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
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
     * 构建 SPU 通用条件包装器
     *
     * <p>accountIdList 承接旧口径的 supplierIdList (同一列 account_id)。
     * marketId / relationType / type / profit 区间涉及市场关联表, 不在本包装器覆盖范围</p>
     *
     * @param query SPU 查询, 可为 null
     * @return 条件包装器, 恒非 null
     */
    default BaseLambdaQueryWrapper<SpuDO> getLw(SpuQuery query) {
        BaseLambdaQueryWrapper<SpuDO> wrapper = new BaseLambdaQueryWrapper<>(SpuDO.class);
        if (query == null) {
            return wrapper;
        }
        return wrapper.notEmptyIn(SpuDO::getId, query.getIdList())
                .notEmptyIn(SpuDO::getAccountId, query.getAccountIdList())
                .notEmptyIn(SpuDO::getCode, query.getCodeList())
                .notEmptyIn(SpuDO::getCategoryId, query.getCategoryIdList())
                .notEmptyIn(SpuDO::getState, query.getStateList())
                .notEmptyIn(SpuDO::getAuditState, query.getAuditStateList())
                .notEmptyLike(SpuDO::getName, query.getName())
                .notEmptyLike(SpuDO::getTitle, query.getTitle())
                .notEmptyEq(SpuDO::getDeliverTimeType, query.getDeliverTimeType())
                .notEmptyEq(SpuDO::getChannelType, query.getChannelType())
                .notEmptyEq(SpuDO::getIdentity, query.getIdentity())
                .notEmptyEq(SpuDO::getBrandId, query.getBrandId())
                .notEmptyEq(SpuDO::getFreightTemplateId, query.getFreightTemplateId())
                .notEmptyEq(SpuDO::getOutSpuId, query.getOutSpuId())
                .between(SpuDO::getSupplyPrice, query.getSupplyPriceStart(), query.getSupplyPriceEnd())
                .between(SpuDO::getSalePrice, query.getSalePriceStart(), query.getSalePriceEnd())
                .between(SpuDO::getSaleNum, query.getSaleNumStart(), query.getSaleNumEnd())
                .between(SpuDO::getCreateTime, query.getCreateTime());
    }
}