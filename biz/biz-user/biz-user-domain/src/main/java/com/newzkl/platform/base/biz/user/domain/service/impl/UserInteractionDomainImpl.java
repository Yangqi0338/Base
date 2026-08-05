package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserInteractionRepository;
import com.newzkl.platform.base.biz.user.domain.service.UserInteractionDomain;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.ActionTypeEnum;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.TargetTypeEnum;
import com.newzkl.platform.base.biz.user.model.interaction.query.BatchInteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionPageQueryRPC;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.res.BatchInteractionResult;
import com.newzkl.platform.base.biz.user.model.interaction.vo.InteractionRPCVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionAddVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionCountVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserInteraction;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户互动领域服务实现
 *
 * <p>迁移自旧 {@code UserInteractionServiceImpl}。语义迁移: 旧 {@code IllegalArgumentException}
 * / {@code RuntimeException} 统一改 {@code PlatformException}; 旧 {@code IPage} 出参保留 Page 分页壳直返。</p>
 *
 * <p>TODO[infra-gap] 旧实现的跨域富化在中台缺出站能力, 已省略 (见 {@code UserInteractionDomain} 类注释):
 * goods 计数增量同步、互动汇总数富化。</p>
 *
 * @author KC
 */
@Slf4j
@Service("userInteractionDomainImpl")
@RequiredArgsConstructor
public class UserInteractionDomainImpl implements UserInteractionDomain {

    private final UserInteractionRepository userInteractionRepository;

    /**
     * 解析目标类型编码
     *
     * @param code 目标类型编码
     * @return 目标类型枚举
     */
    private TargetTypeEnum parseTargetType(String code) {
        TargetTypeEnum targetType = TargetTypeEnum.getByCode(code);
        if (targetType == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "targetType 不合法：" + code);
        }
        return targetType;
    }

    /**
     * 解析操作类型编码
     *
     * @param code 操作类型编码
     * @return 操作类型枚举
     */
    private ActionTypeEnum parseActionType(String code) {
        ActionTypeEnum actionType = ActionTypeEnum.getByCode(code);
        if (actionType == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "actionType 不合法：" + code);
        }
        return actionType;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(InteractionAddVO addVO) {
        TargetTypeEnum targetType = parseTargetType(addVO.getTargetType());
        ActionTypeEnum actionType = parseActionType(addVO.getActionType());

        if (userInteractionRepository.exists(addVO.getUserId(), targetType, addVO.getTargetId(), actionType)) {
            log.info("用户{}已对目标{}执行过{}", addVO.getUserId(), addVO.getTargetId(), actionType.getCode());
            return true;
        }

        UserInteraction interaction = new UserInteraction();
        interaction.setUserId(addVO.getUserId());
        interaction.setPublisherId(addVO.getPublisherId());
        interaction.setStoreId(addVO.getStoreId());
        interaction.setTargetType(targetType);
        interaction.setTargetId(addVO.getTargetId());
        interaction.setActionType(actionType);
        interaction.setCreatedTime(LocalDateTime.now());
        interaction.setUpdatedTime(LocalDateTime.now());

        // TODO[infra-gap] 旧实现成功后调 IGoodsCountFacade.remoteProcess 同步 +1 增量到商品域, 中台无该出站端口。
        return userInteractionRepository.add(interaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(InteractionAddVO addVO) {
        TargetTypeEnum targetType = parseTargetType(addVO.getTargetType());
        ActionTypeEnum actionType = parseActionType(addVO.getActionType());

        boolean result = userInteractionRepository.cancel(addVO.getUserId(), targetType, addVO.getTargetId(), actionType);
        if (!result) {
            throw new PlatformException(BaseErrorCode.NODATA, actionType.getDesc());
        }

        // TODO[infra-gap] 旧实现成功后调 IGoodsCountFacade.remoteProcess 同步 -1 增量到商品域, 中台无该出站端口。
        return true;
    }

    @Override
    public Page<InteractionRPCVO> getUserInteractionsPage(InteractionPageQueryRPC query) {
        parseActionType(query.getActionType());
        // TODO[infra-gap] 旧实现用 IStoreTargetInteractionFacade 汇总补 actionTypeCount, 中台无该出站端口, 字段保持为空。
        return userInteractionRepository.queryByUserPage(query);
    }

    @Override
    public boolean checkIsInteracted(InteractionQuery query) {
        TargetTypeEnum targetType = parseTargetType(query.getTargetType());
        ActionTypeEnum actionType = parseActionType(query.getActionType());
        return userInteractionRepository.exists(query.getUserId(), targetType, query.getTargetId(), actionType);
    }

    @Override
    public InteractionCountVO countTargetInteractions(String targetType, Long targetId, String actionType) {
        return userInteractionRepository.countByTarget(parseTargetType(targetType), targetId, parseActionType(actionType));
    }

    @Override
    public List<BatchInteractionResult> batchCheckIsInteracted(BatchInteractionQuery batchQuery) {
        if (batchQuery == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "批量查询参数不能为空");
        }
        if (batchQuery.getTargetType() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "targetType 不合法");
        }
        if (batchQuery.getActionType() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "actionType 不合法");
        }
        if (batchQuery.getItems() == null || batchQuery.getItems().isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM, "批量检查的子项列表不能为空");
        }
        return userInteractionRepository.batchExists(batchQuery);
    }
}
