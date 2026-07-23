package com.newzkl.platform.base.biz.goods.domain.goodsZone.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.convert.GoodsZoneGoodsRelConvertUtil;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.repository.GoodsZoneGoodsRelRepository;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.repository.GoodsZoneRepository;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.service.GoodsZoneGoodsRelDomainService;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.ISpuRepository;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZone;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZoneGoodsRel;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneGoodsRelPageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneGoodsRelRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SpuQuery;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品分组-商品关联领域服务实现
 * @author sijiwang
 * @since 2026-03-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsZoneGoodsRelDomainServiceImpl implements GoodsZoneGoodsRelDomainService {

    private final GoodsZoneGoodsRelRepository relRepository;
    private final GoodsZoneRepository goodsZoneRepository;
    private final ISpuRepository spuRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAdd(GoodsZoneGoodsRelAddReq addReq) {
        Long groupId = addReq.getGroupId();

        // 校验分组是否存在
        GoodsZone goodsZone = goodsZoneRepository.findById(groupId)
                .orElseThrow(() -> new ScmException(BaseErrorCode.NODATA, "添加失败：分组不存在"));

        // 批量查询已关联的SPU ID
        Set<Long> existedSpuIds = relRepository.findSpuIdsByGroupId(groupId);
        // 筛选出未关联的SPU ID
        List<Long> unAssociatedSpuIds = addReq.getSpuList().stream()
                .filter(spuId -> !existedSpuIds.contains(spuId))
                .distinct() // 去重，避免重复ID
                .collect(Collectors.toList());

        // 无未关联商品则直接返回成功（无需执行后续操作）
        if (CollectionUtils.isEmpty(unAssociatedSpuIds)) {
            log.info("批量添加商品到分组：分组ID={}，所有商品已关联，无需添加", groupId);
            return true;
        }

        //  批量查询未关联的SPU详情（仅查询需要添加的）
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setIdList(unAssociatedSpuIds);
        List<SpuVO> unAssociatedSpus = spuRepository.listSelect(spuQuery);
        // 校验查询结果（防止传入不存在的SPU ID）
        if (CollectionUtils.isEmpty(unAssociatedSpus)) {
            log.warn("批量添加商品到分组：分组ID={}，筛选后的未关联商品ID不存在，无需添加", groupId);
            return true;
        }

        // 构建关联关系列表
        List<GoodsZoneGoodsRel> relList = unAssociatedSpus.stream()
                .map(spu -> {
                    GoodsZoneGoodsRel rel = new GoodsZoneGoodsRel();
                    rel.setGroupId(groupId);
                    rel.setSpuId(spu.getId());
                    rel.setSpu(JSON.toJSONString(spu));
                    return rel;
                })
                .collect(Collectors.toList());

        //  批量保存关联关系
        boolean saveResult = relRepository.batchSave(relList);

        //  成功则更新分组商品数量
        if (saveResult) {
            int actualAddNum = unAssociatedSpus.size();
            goodsZoneRepository.updateGoodsNum(groupId, goodsZone.getGoodsNum() + actualAddNum);
            log.info("批量添加商品到分组成功：分组ID={}，待添加商品数={}，实际添加数={}",
                    groupId, addReq.getSpuList().size(), actualAddNum);
        } else {
            log.error("批量添加商品到分组失败：分组ID={}，实际待添加商品数={}", groupId, unAssociatedSpus.size());
        }

        return saveResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(Long groupId, List<Long> spuIdList) {

        //  校验分组存在
        Optional<GoodsZone> goodsZoneOptional = goodsZoneRepository.findById(groupId);
        if (!goodsZoneOptional.isPresent()) {
            throw new ScmException(BaseErrorCode.NODATA, "删除失败：分组不存在");
        }
        GoodsZone goodsZone = goodsZoneOptional.get();
        // 3. 批量删除关联关系（逻辑删除）
        boolean deleteResult = relRepository.batchDelete(groupId, spuIdList);

        // 4. 更新分组商品数量
        if (deleteResult) {
            Integer newGoodsNum = CollectionUtils.isEmpty(spuIdList)?0:goodsZone.getGoodsNum() - spuIdList.size();
            goodsZoneRepository.updateGoodsNum(groupId, newGoodsNum);
            log.info("批量删除分组下商品成功：分组ID={}, 商品数量={}", groupId, spuIdList.size());
        }

        return deleteResult;
    }

    @Override
    public List<GoodsZoneGoodsRelRes> listByGroupId(Long groupId) {
        List<GoodsZoneGoodsRel> relList = relRepository.findByGroupId(groupId);
        return GoodsZoneGoodsRelConvertUtil.convertToVOList(relList);
    }

    @Override
    public Page<GoodsZoneGoodsRelRes> pageQuery(GoodsZoneGoodsRelPageReq queryReq) {
        Page<GoodsZoneGoodsRel> relPage = relRepository.pageQuery(queryReq);
        return TransferUtils.transferPage(relPage, GoodsZoneGoodsRelConvertUtil::convertToVO);
    }

    @Override
    public boolean checkExists(Long groupId, Long spuId) {
        return relRepository.existsByGroupIdAndSpuId(groupId, spuId);
    }
}