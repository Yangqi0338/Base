package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserInteractionRepository;
import com.newzkl.platform.base.biz.user.domain.service.UserInteractionDomain;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.ActionTypeEnum;
import com.newzkl.platform.base.common.ddd.model.enums.interaction.InteractionEnum.TargetTypeEnum;
import com.newzkl.platform.base.biz.user.model.interaction.query.BatchInteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.res.BatchInteractionResult;
import com.newzkl.platform.base.biz.user.model.interaction.vo.InteractionRPCVO;
import com.newzkl.platform.base.biz.user.model.relation.req.InteractionAddReq;
import com.newzkl.platform.base.biz.user.model.relation.res.InteractionCountRes;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserInteractionDTO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(InteractionAddReq addVO) {
        InteractionQuery existsQuery = TransferUtils.transfer(addVO, InteractionQuery.class);
        if (userInteractionRepository.exists(existsQuery)) {
            log.info("用户{}已对目标{}执行过{}", addVO.getUserId(), addVO.getTargetId(), addVO.getTargetType());
            return true;
        }

        return userInteractionRepository.add(TransferUtils.transfer(addVO, UserInteractionDTO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(InteractionAddReq addVO) {
        InteractionQuery cancelQuery = TransferUtils.transfer(addVO, InteractionQuery.class);
        boolean result = userInteractionRepository.cancel(cancelQuery);
        if (!result) {
            throw new PlatformException(BaseErrorCode.NODATA, addVO.getTargetType().getValue());
        }

        // TODO[infra-gap] 旧实现成功后调 IGoodsCountFacade.remoteProcess 同步 -1 增量到商品域, 中台无该出站端口。
        return true;
    }

    @Override
    public Page<InteractionRPCVO> getUserInteractionsPage(InteractionQuery query) {
        return userInteractionRepository.queryByUserPage(query);
    }

    @Override
    public boolean checkIsInteracted(InteractionQuery query) {
        return userInteractionRepository.exists(query);
    }

    @Override
    public InteractionCountRes countTargetInteractions(TargetTypeEnum targetType, Long targetId, ActionTypeEnum actionType) {
        return userInteractionRepository.countByTarget(targetType, targetId, actionType);
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
