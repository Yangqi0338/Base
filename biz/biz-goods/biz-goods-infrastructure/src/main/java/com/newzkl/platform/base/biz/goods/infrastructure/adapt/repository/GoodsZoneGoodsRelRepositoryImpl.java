package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.repository.GoodsZoneGoodsRelRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler.GoodsZoneGoodsRelAssembler;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.GoodsZoneGoodsRelDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.GoodsZoneGoodsRelDO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZoneGoodsRel;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelPageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品分组-商品关联仓储实现
 * @author sijiwang
 * @since 2026-03-18
 */
@Repository
@RequiredArgsConstructor
public class GoodsZoneGoodsRelRepositoryImpl extends ServiceImpl<GoodsZoneGoodsRelDAO, GoodsZoneGoodsRelDO>
        implements GoodsZoneGoodsRelRepository {

    private final GoodsZoneGoodsRelAssembler relAssembler;

    @Override
    public GoodsZoneGoodsRel saveRel(GoodsZoneGoodsRel rel) {
        GoodsZoneGoodsRelDO relDO = relAssembler.goodsZoneGoodsRelDOConvert(rel);
        this.saveOrUpdate(relDO);
        return relAssembler.goodsZoneGoodsRelConvert(relDO);
    }

    @Override
    public boolean batchSave(List<GoodsZoneGoodsRel> relList) {
        List<GoodsZoneGoodsRelDO> doList = relList.stream()
                .map(relAssembler::goodsZoneGoodsRelDOConvert)
                .collect(Collectors.toList());
        return this.saveBatch(doList);
    }

    @Override
    public Optional<GoodsZoneGoodsRel> findById(Long id) {
        GoodsZoneGoodsRelDO relDO = this.getById(id);
        if (relDO == null) {
            return Optional.empty();
        }
        return Optional.of(relAssembler.goodsZoneGoodsRelConvert(relDO));
    }

    @Override
    public Set<Long> findSpuIdsByGroupId(Long groupId) {
        LambdaQueryWrapper<GoodsZoneGoodsRelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsZoneGoodsRelDO::getGroupId, groupId)
                .eq(GoodsZoneGoodsRelDO::getDelFlag, 0);
        List<GoodsZoneGoodsRelDO> list = this.list(wrapper);
        return list.stream()
                .map(GoodsZoneGoodsRelDO::getSpuId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<GoodsZoneGoodsRel> findByGroupId(Long groupId) {
        LambdaQueryWrapper<GoodsZoneGoodsRelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsZoneGoodsRelDO::getGroupId, groupId)
                .eq(GoodsZoneGoodsRelDO::getDelFlag, 0); // 排除逻辑删除

        List<GoodsZoneGoodsRelDO> doList = this.list(wrapper);
        return doList.stream()
                .map(relAssembler::goodsZoneGoodsRelConvert)
                .collect(Collectors.toList());
    }

    @Override
    public Page<GoodsZoneGoodsRel> pageQuery(GoodsZoneGoodsRelPageReq queryReq) {
        LambdaQueryWrapper<GoodsZoneGoodsRelDO> wrapper = new LambdaQueryWrapper<>();
        // 分组ID筛选
        if (queryReq.getGroupId() != null) {
            wrapper.eq(GoodsZoneGoodsRelDO::getGroupId, queryReq.getGroupId());
        }
        // 商品ID筛选
        if (queryReq.getSpuId() != null) {
            wrapper.eq(GoodsZoneGoodsRelDO::getSpuId, queryReq.getSpuId());
        }
        // 排除逻辑删除
        wrapper.eq(GoodsZoneGoodsRelDO::getDelFlag, 0);
        // 按创建时间倒序
        wrapper.orderByDesc(GoodsZoneGoodsRelDO::getCreateTime);

        Page<GoodsZoneGoodsRelDO> doPage = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        Page<GoodsZoneGoodsRelDO> resultDO = this.page(doPage, wrapper);

        return TransferUtils.transferPage(resultDO, relAssembler::goodsZoneGoodsRelConvert);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(Long groupId, List<Long> spuIdList) {
        LambdaQueryWrapper<GoodsZoneGoodsRelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsZoneGoodsRelDO::getGroupId, groupId);

        if (!CollectionUtils.isEmpty(spuIdList)){
            wrapper.in(GoodsZoneGoodsRelDO::getSpuId, spuIdList);
        }

        return this.remove(wrapper);
    }

    @Override
    public boolean existsByGroupIdAndSpuId(Long id, Long spuId) {
        LambdaQueryWrapper<GoodsZoneGoodsRelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsZoneGoodsRelDO::getId, id)
                .eq(GoodsZoneGoodsRelDO::getSpuId, spuId)
                .eq(GoodsZoneGoodsRelDO::getDelFlag, 0);
        return this.count(wrapper) > 0;
    }

    @Override
    public Integer countByGroupId(Long id) {
        LambdaQueryWrapper<GoodsZoneGoodsRelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsZoneGoodsRelDO::getId, id)
                .eq(GoodsZoneGoodsRelDO::getDelFlag, 0);
        return Math.toIntExact(this.count(wrapper));
    }
}