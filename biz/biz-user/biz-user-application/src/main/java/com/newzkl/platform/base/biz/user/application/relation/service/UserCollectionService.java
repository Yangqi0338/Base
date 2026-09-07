package com.newzkl.platform.base.biz.user.application.relation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.model.relation.req.UncollectedProductReq;
import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionCreateReq;
import com.newzkl.platform.base.biz.user.model.relation.query.UserCollectionQuery;
import com.newzkl.platform.base.biz.user.model.relation.req.UserCollectionReq;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserCollectionDTO;

import java.util.List;

/**
 * 用户收藏应用服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.application.service.IUserCollectionService}。
 * 沿用旧分层 (application), 因含"查已删记录 → 复活 or 新建"多步编排。</p>
 *
 * <p>TODO[infra-gap] 旧实现的三处跨域调用在中台缺对应出站能力, 已省略, 不臆造实现:</p>
 * <ul>
 *   <li>{@code IDistributionRpcFacade.selectById(storeGoodsId)} — 收藏前校验铺货ID有效性,
 *       并用铺货数据回填 storeId/spuId/skuId/price。中台无 market 铺货出站端口,
 *       改为直接采用入参快照, 铺货ID有效性不校验。</li>
 *   <li>{@code ISpuFacade.apiSpuVOList(...)} — 列表/分页出参用商品域实时数据覆盖 spuName/mainImage。
 *       中台无 goods 出站端口, 出参保持收藏时刻快照。</li>
 *   <li>{@code IDistributionRpcFacade.listByIds / queryGoodsSellNum} — 富化实时售价与销量。
 *       中台无该出站端口, 售价保持快照, 销量字段不存在于中台 {@code UserCollection}。</li>
 * </ul>
 *
 * @author KC
 */
public interface UserCollectionService {

    /**
     * 收藏商品
     *
     * <p>同一用户对同一铺货已有记录（含已逻辑删除）时直接回该记录, 不重复新增。</p>
     *
     * @param req 收藏创建请求
     * @return 收藏记录
     */
    UserCollectionDTO collectProduct(UserCollectionCreateReq req);

    /**
     * 取消收藏商品
     *
     * @param req 取消收藏请求
     * @return 是否成功
     */
    boolean uncollectProduct(UncollectedProductReq req);

    /**
     * 取消当前账号的全部失效收藏
     *
     * @return 是否成功
     */
    Boolean uncollectedInvalidProduct();

    /**
     * 查询用户的全部收藏
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<UserCollectionDTO> getUserCollections(Long userId);

    /**
     * 检查用户是否已收藏指定商品
     *
     * @param req 查询请求
     * @return 是否已收藏
     */
    boolean checkIsCollected(UserCollectionReq req);

    /**
     * 分页查询用户收藏
     *
     * @param query 分页查询
     * @return 收藏分页
     */
    Page<UserCollectionDTO> getUserCollectionsPage(UserCollectionQuery query);
}
