package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserInteractionRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.UserInteractionDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserInteractionDO;
import com.newzkl.platform.base.biz.user.model.enums.InteractionEnum.ActionTypeEnum;
import com.newzkl.platform.base.biz.user.model.enums.InteractionEnum.TargetTypeEnum;
import com.newzkl.platform.base.biz.user.model.interaction.query.BatchInteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionBatchItem;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionPageQueryRPC;
import com.newzkl.platform.base.biz.user.model.interaction.res.BatchInteractionResult;
import com.newzkl.platform.base.biz.user.model.interaction.vo.InteractionRPCVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionCountVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserInteraction;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户互动操作仓储实现
 *
 * <p>迁移说明：queryByUserPage 保留 Page 分页壳直返；排序字段
 * createdTime 源不存在于 DO，改用 BaseDO.createTime。</p>
 *
 * @author sijiwang
 */
@Repository
@RequiredArgsConstructor
public class UserInteractionRepositoryImpl implements UserInteractionRepository {

    private final UserInteractionDAO interactionMapper;

    @Override
    public boolean add(UserInteraction interaction) {
        return interactionMapper.insert(TransferUtils.transfer(interaction, UserInteractionDO::new)) > 0;
    }

    @Override
    public boolean cancel(Long userId, TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType) {
        LambdaQueryWrapper<UserInteractionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInteractionDO::getUserId, userId)
                .eq(UserInteractionDO::getTargetType, targetType)
                .eq(UserInteractionDO::getTargetId, targetId)
                .eq(UserInteractionDO::getActionType, actionType);
        return interactionMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean exists(Long userId, TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType) {
        LambdaQueryWrapper<UserInteractionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInteractionDO::getUserId, userId)
                .eq(UserInteractionDO::getTargetType, targetType)
                .eq(UserInteractionDO::getTargetId, targetId)
                .eq(UserInteractionDO::getActionType, actionType);
        return interactionMapper.exists(queryWrapper);
    }

    @Override
    public List<InteractionVO> queryByUser(Long userId, ActionTypeEnum actionType) {
        LambdaQueryWrapper<UserInteractionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInteractionDO::getUserId, userId)
                .eq(UserInteractionDO::getActionType, actionType)
                .orderByDesc(UserInteractionDO::getId);

        List<UserInteractionDO> interactions = interactionMapper.selectList(queryWrapper);
        return TransferUtils.transfers(interactions, InteractionVO.class);
    }

    @Override
    public InteractionCountVO countByTarget(TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType) {
        LambdaQueryWrapper<UserInteractionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInteractionDO::getTargetType, targetType)
                .eq(UserInteractionDO::getTargetId, targetId)
                .eq(UserInteractionDO::getActionType, actionType);

        long count = interactionMapper.selectCount(queryWrapper);
        InteractionCountVO countVO = new InteractionCountVO();
        countVO.setTargetId(targetId);
        countVO.setActionType(actionType.getCode());
        countVO.setCount((int) count);
        return countVO;
    }

    @Override
    public Page<InteractionRPCVO> queryByUserPage(InteractionPageQueryRPC pageDTO) {
        Page<UserInteractionDO> mpPage = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());

        LambdaQueryWrapper<UserInteractionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInteractionDO::getUserId, pageDTO.getUserId())
                .orderByDesc(UserInteractionDO::getId);

        if (pageDTO.getStoreId() != null) {
            queryWrapper.eq(UserInteractionDO::getStoreId, pageDTO.getStoreId());
        }
        if (pageDTO.getTargetType() != null) {
            queryWrapper.eq(UserInteractionDO::getTargetType, pageDTO.getTargetType());
        }
        if (pageDTO.getActionType() != null) {
            queryWrapper.eq(UserInteractionDO::getActionType, pageDTO.getActionType());
        }

        Page<UserInteractionDO> interactionPage = interactionMapper.selectPage(mpPage, queryWrapper);
        return TransferUtils.transferPage(interactionPage, InteractionRPCVO.class);
    }

    @Override
    public List<BatchInteractionResult> batchExists(BatchInteractionQuery batchQuery) {
        TargetTypeEnum targetType = batchQuery.getTargetType();
        ActionTypeEnum actionType = batchQuery.getActionType();
        List<InteractionBatchItem> items = batchQuery.getItems();

        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> userIdSet = items.stream().map(InteractionBatchItem::getUserId).collect(Collectors.toSet());
        Set<Long> targetIdSet = items.stream().map(InteractionBatchItem::getTargetId).collect(Collectors.toSet());

        LambdaQueryWrapper<UserInteractionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInteractionDO::getTargetType, targetType)
                .eq(UserInteractionDO::getActionType, actionType)
                .in(UserInteractionDO::getUserId, userIdSet)
                .in(UserInteractionDO::getTargetId, targetIdSet);

        List<UserInteractionDO> existedInteractions = interactionMapper.selectList(queryWrapper);

        Set<String> existedKeySet = existedInteractions.stream()
                .map(inter -> String.format("%s_%s", inter.getUserId(), inter.getTargetId()))
                .collect(Collectors.toSet());

        return items.stream()
                .map(item -> {
                    String key = String.format("%s_%s", item.getUserId(), item.getTargetId());
                    boolean isInteracted = existedKeySet.contains(key);
                    return new BatchInteractionResult(item.getUserId(), item.getStoreId(), item.getTargetId(), isInteracted);
                })
                .collect(Collectors.toList());
    }
}
