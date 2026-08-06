package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.interaction.repository.StoreTargetInteractionStatRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler.StoreTargetInteractionStatAssembler;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.StoreTargetInteractionStatDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.StoreTargetInteractionStatDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatBatchReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.StoreTargetInteractionStatPageReq;
import com.newzkl.platform.base.common.ddd.facade.StoreTargetInteractionSummaryObj;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 统计仓储实现（纯MyBatis + PageHelper，彻底移除MyBatis-Plus）
 * @author sijiwang
 */
@Repository
@RequiredArgsConstructor
public class StoreTargetInteractionStatRepositoryImpl implements StoreTargetInteractionStatRepository {

    private final StoreTargetInteractionStatDAO statDAO;

    private final StoreTargetInteractionStatAssembler statAssembler;

    @Override
    public boolean batchSaveOrUpdate(List<StoreTargetInteractionStat> domainModelList) {
        if (domainModelList == null || domainModelList.isEmpty()) {
            return true;
        }
        List<StoreTargetInteractionStatDO> doList = statAssembler.toDOList(domainModelList);
        int rows = statDAO.batchInsertOrUpdate(doList);
        return rows > 0;
    }

    @Override
    public StoreTargetInteractionStat findByStoreTarget(Long storeId, String targetType, Long targetId) {
        StoreTargetInteractionStatDO doEntity = statDAO.selectByStoreTarget(storeId, targetType, targetId);
        return statAssembler.toDomain(doEntity);
    }

    @Override
    public boolean deleteByStoreTarget(Long storeId, String targetType, Long targetId) {
        int rows = statDAO.deleteByStoreTarget(storeId, targetType, targetId);
        return rows > 0;
    }

    /**
     * 分页查询（PageHelper实现）
     */
    @Override
    public Page<StoreTargetInteractionStat> pageQuery(StoreTargetInteractionStatPageReq queryVO) {
        // 1. 构建查询条件Map
        Map<String, Object> condition = new HashMap<>();
        condition.put("storeId", queryVO.getStoreId());

        // 目标类型转换
        if (Objects.nonNull(queryVO.getTargetTypeCode())) {
            InteractionEnum.TargetTypeEnum targetType = InteractionEnum.TargetTypeEnum.getByCode(queryVO.getTargetTypeCode());
            if (Objects.nonNull(targetType)) {
                condition.put("targetType", targetType);
            }
        }
        condition.put("targetIdList", queryVO.getTargetIdList());
        condition.put("viewCountMin", queryVO.getViewCountMin());
        condition.put("likeCountMin", queryVO.getLikeCountMin());
        condition.put("shareCountMin", queryVO.getShareCountMin());

        // 3. 查询数据
        Page<StoreTargetInteractionStatDO> page = new Page<>(queryVO.getPageNum(), queryVO.getPageSize());
        Page<StoreTargetInteractionStatDO> doPage = statDAO.selectPageByCondition(page, condition);

        return TransferUtils.transferPage(doPage, statAssembler::toDomain);
    }

    /**
     * 批量查询（纯MyBatis动态SQL）
     */
    @Override
    public List<StoreTargetInteractionStat> batchQuery(StoreTargetInteractionStatBatchReq queryVO) {
        // 1. 构建查询条件Map
        Map<String, Object> condition = new HashMap<>();
        condition.put("storeIdList", queryVO.getStoreIdList());
        condition.put("targetType", queryVO.getTargetType());
        condition.put("targetIdList", queryVO.getTargetIdList());

        // 浏览量范围
        if (Objects.nonNull(queryVO.getViewCountRange()) && queryVO.getViewCountRange().length == 2) {
            condition.put("viewCountMin", queryVO.getViewCountRange()[0]);
            condition.put("viewCountMax", queryVO.getViewCountRange()[1]);
        }
        // 点赞量范围
        if (Objects.nonNull(queryVO.getLikeCountRange()) && queryVO.getLikeCountRange().length == 2) {
            condition.put("likeCountMin", queryVO.getLikeCountRange()[0]);
            condition.put("likeCountMax", queryVO.getLikeCountRange()[1]);
        }
        // 分享量范围
        if (Objects.nonNull(queryVO.getShareCountRange()) && queryVO.getShareCountRange().length == 2) {
            condition.put("shareCountMin", queryVO.getShareCountRange()[0]);
            condition.put("shareCountMax", queryVO.getShareCountRange()[1]);
        }

        // 2. 查询DO列表
        List<StoreTargetInteractionStatDO> doList = statDAO.selectListByCondition(condition);

        // 3. DO → 领域模型
        return statAssembler.toDomainListFromDO(doList);
    }

    // 如需实现单条保存/更新，补充以下方法（按需）
    @Override
    public boolean save(StoreTargetInteractionStat domainModel) {
        StoreTargetInteractionStatDO doEntity = statAssembler.toDO(domainModel);
        int rows = statDAO.insert(doEntity);
        return rows > 0;
    }

    @Override
    public boolean update(StoreTargetInteractionStat domainModel) {
        StoreTargetInteractionStatDO doEntity = statAssembler.toDO(domainModel);
        int rows = statDAO.updateById(doEntity);
        return rows > 0;
    }

    @Override
    public List<StoreTargetInteractionStat> selectByTargetTypeAndIdList(List<String> targetTypeList, List<Long> targetIdList) {
        List<StoreTargetInteractionStatDO> storeTargetInteractionStatDOS = statDAO.selectByTargetTypeAndIdList(targetTypeList, targetIdList);
        return storeTargetInteractionStatDOS.stream().map(statAssembler::toDomain).collect(Collectors.toList());
    }

    @Override
    public StoreTargetInteractionSummaryObj selectSummaryByStoreIdList(List<Long> targetIdList) {
        return statDAO.selectSummaryByStoreIdList(targetIdList);
    }

    @Override
    public StoreTargetInteractionSummaryObj selectSummaryByPublisherId(Long publisherId) {
        return statDAO.selectSummaryByPublisherId(publisherId);
    }

    @Override
    public List<StoreTargetInteractionStat> selectByPublisherId(Long publisherId) {
        List<StoreTargetInteractionStatDO> storeTargetInteractionStatDOS = statDAO.selectByPublisherId(publisherId);
        return storeTargetInteractionStatDOS.stream().map(statAssembler::toDomain).collect(Collectors.toList());
    }
}