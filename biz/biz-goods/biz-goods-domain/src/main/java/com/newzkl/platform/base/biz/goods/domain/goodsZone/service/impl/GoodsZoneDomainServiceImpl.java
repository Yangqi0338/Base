package com.newzkl.platform.base.biz.goods.domain.goodsZone.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.convert.GoodsZoneConvertUtil;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.repository.GoodsZoneRepository;
import com.newzkl.platform.base.biz.goods.domain.goodsZone.service.GoodsZoneDomainService;
import com.newzkl.platform.base.biz.goods.model.goods.dto.goodsZooe.GoodsZone;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZoneAddReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone.GoodsZonePageReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.goodsZone.GoodsZoneRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品分组领域服务实现
 * @author sijiwang
 * @since 2026-03-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsZoneDomainServiceImpl implements GoodsZoneDomainService {

    private final GoodsZoneRepository goodsZoneRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsZoneRes add(GoodsZoneAddReq addReq) {
        // 1. 校验分组名称唯一性
        checkGroupNameUnique(null, addReq.getGroupName());

        // 2. 转换为领域实体
        GoodsZone goodsZone = GoodsZoneConvertUtil.convertToEntity(addReq);

        // 3. 保存分组
        GoodsZone savedZone = goodsZoneRepository.saveGoodsZone(goodsZone);

        // 4. 转换为VO返回
        return GoodsZoneConvertUtil.convertToVO(savedZone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsZoneRes edit(GoodsZoneAddReq addReq) {
        // 1. 校验ID
        if (addReq.getId() == null) {
            throw new ScmException(BaseErrorCode.PARAM, "编辑失败：分组ID不能为空");
        }

        // 2. 查询分组是否存在
        Optional<GoodsZone> zoneOpt = goodsZoneRepository.findById(addReq.getId());
        if (!zoneOpt.isPresent()) {
            throw new ScmException(BaseErrorCode.NODATA, "编辑失败：分组不存在");
        }

        // 3. 校验名称唯一性
        checkGroupNameUnique(addReq.getId(), addReq.getGroupName());

        // 4. 转换为领域实体
        GoodsZone goodsZone = GoodsZoneConvertUtil.convertToEntity(addReq);
        goodsZone.setGoodsNum(zoneOpt.get().getGoodsNum()); // 保留商品数量

        // 5. 保存更新
        GoodsZone updatedZone = goodsZoneRepository.saveGoodsZone(goodsZone);

        // 6. 转换为VO返回
        return GoodsZoneConvertUtil.convertToVO(updatedZone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enable(Long id, String operator) {
        // 1. 校验分组存在
        Optional<GoodsZone> zoneOpt = goodsZoneRepository.findById(id);
        if (!zoneOpt.isPresent()) {
            throw new ScmException(BaseErrorCode.NODATA, "启用失败：分组不存在");
        }

        // 2. 更新状态
        boolean result = goodsZoneRepository.updateState(id, 1);
        log.info("启用商品分组：ID={}, 操作人={}, 结果={}", id, operator, result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disable(Long id, String operator) {
        // 1. 校验分组存在
        Optional<GoodsZone> zoneOpt = goodsZoneRepository.findById(id);
        if (!zoneOpt.isPresent()) {
            throw new ScmException(BaseErrorCode.NODATA, "禁用失败：分组不存在");
        }

        // 2. 更新状态
        boolean result = goodsZoneRepository.updateState(id, 0);
        log.info("禁用商品分组：ID={}, 操作人={}, 结果={}", id, operator, result);
        return result;
    }

    @Override
    public GoodsZoneRes getById(Long id) {
        Optional<GoodsZone> zoneOpt = goodsZoneRepository.findById(id);
        if (!zoneOpt.isPresent()) {
            throw new ScmException(BaseErrorCode.NODATA, "查询失败：分组不存在");
        }
        return GoodsZoneConvertUtil.convertToVO(zoneOpt.get());
    }

    @Override
    public Page<GoodsZoneRes> pageQuery(GoodsZonePageReq queryReq) {
        Page<GoodsZone> zonePage = goodsZoneRepository.pageQuery(queryReq);
        return TransferUtils.transferPage(zonePage, GoodsZoneConvertUtil::convertToVO);
    }

    @Override
    public List<GoodsZoneRes> listAllEnabled() {
        GoodsZonePageReq queryReq = new GoodsZonePageReq();
        queryReq.setState(1);
        queryReq.setSize(Integer.MAX_VALUE); // 查询全部
        Page<GoodsZone> zonePage = goodsZoneRepository.pageQuery(queryReq);
        return zonePage.getRecords().stream()
                .map(GoodsZoneConvertUtil::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        // 1. 校验分组存在
        Optional<GoodsZone> zoneOpt = goodsZoneRepository.findById(id);
        if (!zoneOpt.isPresent()) {
            throw new ScmException(BaseErrorCode.NODATA, "删除失败：分组不存在");
        }

        // 2. 删除分组
        boolean result = goodsZoneRepository.deleteById(id);
        log.info("删除商品分组：ID={}, 结果={}", id, result);
        return result;
    }

    /**
     * 检查分组名称唯一性
     * @param excludeId 排除ID
     * @param groupName 分组名称
     */
    private void checkGroupNameUnique(Long excludeId, String groupName) {
        if (StrUtil.isBlank(groupName)) {
            return;
        }
        if (goodsZoneRepository.existsByName(groupName, excludeId)) {
            throw new ScmException(BaseErrorCode.EXIST_DATA, "分组名称已存在");
        }
    }
}