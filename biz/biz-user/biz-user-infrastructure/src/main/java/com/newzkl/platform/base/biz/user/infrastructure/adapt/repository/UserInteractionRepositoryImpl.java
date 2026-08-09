package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserInteractionRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.UserInteractionDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserInteractionDO;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.ActionTypeEnum;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.TargetTypeEnum;
import com.newzkl.platform.base.biz.user.model.interaction.query.BatchInteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.req.InteractionBatchItem;
import com.newzkl.platform.base.biz.user.model.interaction.res.BatchInteractionResult;
import com.newzkl.platform.base.biz.user.model.interaction.vo.InteractionRPCVO;
import com.newzkl.platform.base.biz.user.model.relation.res.InteractionCountRes;
import com.newzkl.platform.base.biz.user.model.relation.res.InteractionRes;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserInteractionDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
public class UserInteractionRepositoryImpl extends RepositorySupport implements UserInteractionRepository {

    private final UserInteractionDAO userInteractionDAO;

    @Override
    public boolean add(UserInteractionDTO interaction) {
        return userInteractionDAO.insert(TransferUtils.transfer(interaction, UserInteractionDO::new)) > 0;
    }

    @Override
    public boolean cancel(InteractionQuery query) {
        return userInteractionDAO.delete(userInteractionDAO.getLw(query)) > 0;
    }

    @Override
    public boolean exists(InteractionQuery query) {
        return userInteractionDAO.exists(userInteractionDAO.getLw(query));
    }

    @Override
    public List<InteractionRes> queryByUser(Long userId, ActionTypeEnum actionType) {
        InteractionQuery query = new InteractionQuery();
        query.setUserId(userId);
        query.setActionType(actionType);
        List<UserInteractionDO> interactions = userInteractionDAO.selectList(userInteractionDAO.getLw(query));
        return TransferUtils.transfers(interactions, InteractionRes.class);
    }

    @Override
    public InteractionCountRes countByTarget(TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType) {
        InteractionQuery query = new InteractionQuery();
        query.setTargetType(targetType);
        query.setTargetId(targetId);
        query.setActionType(actionType);

        long count = userInteractionDAO.selectCount(userInteractionDAO.getLw(query));
        InteractionCountRes countVO = new InteractionCountRes();
        countVO.setTargetId(targetId);
        countVO.setActionType(actionType.getCode());
        countVO.setCount((int) count);
        return countVO;
    }

    @Override
    public Page<InteractionRPCVO> queryByUserPage(InteractionQuery query) {
        Page<UserInteractionDO> interactionPage = userInteractionDAO.selectPage(RepositorySupport.page(query), userInteractionDAO.getLw(query));
        return TransferUtils.transferPage(interactionPage, InteractionRPCVO.class);
    }

    @Override
    public List<BatchInteractionResult> batchExists(BatchInteractionQuery batchQuery) {
        List<InteractionBatchItem> items = batchQuery.getItems();
        List<Long> userIdList = items.stream().map(InteractionBatchItem::getUserId).collect(Collectors.toList());
        List<Long> targetIdSet = items.stream().map(InteractionBatchItem::getTargetId).collect(Collectors.toList());

        InteractionQuery query = new InteractionQuery();
        query.setTargetType(batchQuery.getTargetType());
        query.setActionType(batchQuery.getActionType());
        query.setTargetIdList(targetIdSet);
        query.setUserIdList(userIdList);

        List<UserInteractionDO> existedInteractions = userInteractionDAO.selectList(userInteractionDAO.getLw(query));

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
