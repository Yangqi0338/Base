package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.repository.GoodsZoneRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler.GoodsZoneAssembler;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.GoodsZoneDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodsZoneDO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZone;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZonePageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 商品分组仓储实现
 * @author sijiwang
 * @since 2026-03-18
 */
@Repository
@RequiredArgsConstructor
public class GoodsZoneRepositoryImpl extends ServiceImpl<GoodsZoneDAO, GoodsZoneDO>
        implements GoodsZoneRepository {

    private final GoodsZoneAssembler goodsZoneAssembler;

    @Override
    public GoodsZone saveGoodsZone(GoodsZone goodsZone) {
        GoodsZoneDO goodsZoneDO = goodsZoneAssembler.goodsZoneDOConvert(goodsZone);
        this.saveOrUpdate(goodsZoneDO);
        return goodsZoneAssembler.goodsZoneConvert(goodsZoneDO);
    }

    @Override
    public Optional<GoodsZone> findById(Long id) {
        GoodsZoneDO goodsZoneDO = this.getById(id);
        if (goodsZoneDO == null) {
            return Optional.empty();
        }
        return Optional.of(goodsZoneAssembler.goodsZoneConvert(goodsZoneDO));
    }

    @Override
    public Page<GoodsZone> pageQuery(GoodsZonePageReq queryReq) {
        LambdaQueryWrapper<GoodsZoneDO> wrapper = new LambdaQueryWrapper<>();
        // 分组名称模糊查询（匹配产品设计的查询区域）
        if (queryReq.getGroupName() != null) {
            wrapper.like(GoodsZoneDO::getGroupName, queryReq.getGroupName());
        }
        // 状态筛选
        if (queryReq.getState() != null) {
            wrapper.eq(GoodsZoneDO::getState, queryReq.getState());
        }
        // 按创建时间倒序（匹配产品设计的列表排序）
        wrapper.orderByDesc(GoodsZoneDO::getCreateTime);

        Page<GoodsZoneDO> doPage = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        Page<GoodsZoneDO> resultDO = this.page(doPage, wrapper);

        return TransferUtils.transferPage(resultDO, goodsZoneAssembler::goodsZoneConvert);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean updateState(Long id, Integer state) {
        GoodsZoneDO updateDO = new GoodsZoneDO();
        updateDO.setId(id);
        updateDO.setState(state);
        updateDO.setUpdateTime(java.time.LocalDateTime.now());
        return this.updateById(updateDO);
    }

    @Override
    public boolean existsByName(String groupName, Long excludeId) {
        LambdaQueryWrapper<GoodsZoneDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsZoneDO::getGroupName, groupName);
        if (excludeId != null) {
            wrapper.ne(GoodsZoneDO::getId, excludeId);
        }
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean updateGoodsNum(Long id, Integer goodsNum) {
        LambdaQueryWrapper<GoodsZoneDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsZoneDO::getId, id);

        GoodsZoneDO updateDO = new GoodsZoneDO();
        updateDO.setGoodsNum(goodsNum);
        updateDO.setUpdateTime(java.time.LocalDateTime.now());

        return this.update(updateDO, wrapper);
    }
}