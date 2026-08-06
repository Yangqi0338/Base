package com.newzkl.platform.base.biz.user.domain.service;
import com.newzkl.platform.base.common.ddd.facade.StoreTargetInteractionEvent;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.model.interaction.query.BatchInteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionPageQueryRPC;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionQuery;
import com.newzkl.platform.base.biz.user.model.interaction.res.BatchInteractionResult;
import com.newzkl.platform.base.biz.user.model.interaction.vo.InteractionRPCVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionAddVO;
import com.newzkl.platform.base.biz.user.model.relation.vo.InteractionCountVO;

import java.util.List;

/**
 * 用户互动领域服务（点赞/转发/浏览）
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.collection.service.UserInteractionService}。
 * 旧类落 domain 层, 中台沿用同层, 不上移 application。</p>
 *
 * <p>TODO[infra-gap] 旧实现的两处跨域富化在中台缺对应出站能力, 已省略, 不臆造实现:</p>
 * <ul>
 *   <li>{@code IGoodsCountFacade.remoteProcess(StoreTargetInteractionEvent)} — add/cancel 后把
 *       点赞/转发增量同步到商品域计数缓存。中台无 goods 计数出站端口, 增量未同步。</li>
 *   <li>{@code IStoreTargetInteractionFacade.summaryByTargetTypeAndIdList(...)} — 分页出参的
 *       {@code actionTypeCount} 汇总富化。中台无该出站端口, 该字段保持为空。</li>
 * </ul>
 *
 * <p>迁移说明: 源 getUserInteractionsPage 返回 {@code IPage}, 本仓按 {@code rules/Architecture.md}
 * 保留 {@code Page} 分页壳直返, total/pages 元数据不丢。</p>
 *
 * @author KC
 */
public interface UserInteractionDomain {

    /**
     * 新增互动操作（点赞/转发/浏览）
     *
     * <p>同一用户对同一对象的同一操作重复提交时幂等回 true。</p>
     *
     * @param addVO 互动入参
     * @return 是否成功
     */
    boolean add(InteractionAddVO addVO);

    /**
     * 取消互动操作（物理删除记录）
     *
     * @param addVO 互动入参
     * @return 是否成功
     */
    boolean cancel(InteractionAddVO addVO);

    /**
     * 分页查询用户互动记录
     *
     * @param query 分页查询参数
     * @return 互动记录分页
     */
    Page<InteractionRPCVO> getUserInteractionsPage(InteractionPageQueryRPC query);

    /**
     * 检查用户是否已对目标执行互动
     *
     * @param query 查询参数
     * @return 是否已互动
     */
    boolean checkIsInteracted(InteractionQuery query);

    /**
     * 统计目标对象的互动操作数量
     *
     * @param targetType 目标类型编码
     * @param targetId   目标ID
     * @param actionType 操作类型编码
     * @return 统计结果
     */
    InteractionCountVO countTargetInteractions(String targetType, Long targetId, String actionType);

    /**
     * 批量检查用户是否已对目标执行互动
     *
     * @param batchQuery 批量查询参数
     * @return 批量检查结果
     */
    List<BatchInteractionResult> batchCheckIsInteracted(BatchInteractionQuery batchQuery);
}
