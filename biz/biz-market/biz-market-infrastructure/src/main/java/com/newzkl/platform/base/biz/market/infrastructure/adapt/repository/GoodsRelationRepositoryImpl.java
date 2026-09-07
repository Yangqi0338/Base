package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.market.domain.adapt.repository.GoodsRelationRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketBindDAO;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketGoodsRelationDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketBindDO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketGoodsRelationDO;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.dto.relation.MarketGoodsRelationDTO;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.biz.market.model.req.relation.PlatformQueryMarketNotAddGoodsReq;
import com.newzkl.platform.base.biz.market.model.req.relation.UpdateGoodsRelationReq;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.SpuRelevancyMarketVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import com.newzkl.platform.base.biz.market.model.vo.relation.GoodsRelationListVO;
import com.newzkl.platform.base.biz.market.model.vo.relation.MarketGoodsInfoVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code GoodsRelationRepository} 实现
 *
 * <p>照 {@code MarketRepositoryImpl} 风格: 单表读写用 MyBatis-Plus 条件组装,
 * 跨表(market_goods_relation JOIN 商品表)的查询委派给 {@code MarketGoodsRelationDAO}
 * 已声明的同签名自定义方法。</p>
 *
 * <p>说明: 委派给 DAO 自定义方法的分页查询依赖 MyBatis 语句绑定, 未绑定时由 MyBatis
 * 抛 Invalid bound statement, 不会静默成功。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class GoodsRelationRepositoryImpl implements GoodsRelationRepository {

    private final MarketGoodsRelationDAO marketGoodsRelationDAO;
    private final MarketBindDAO marketBindDAO;

    @Override
    public List<SpuRelevancyMarketVO> getSpuRelevancyMarketNum(List<Long> spuIdList) {
        if (CollectionUtils.isEmpty(spuIdList)) {
            return new ArrayList<>();
        }
        return marketGoodsRelationDAO.getSpuRelevancyMarketNum(spuIdList);
    }

    @Override
    public Page<MarketGoodsRelationDTO> queryGoodsRelationPage(GoodsRelationQueryDTO query) {
        Page<MarketGoodsRelationDO> page = marketGoodsRelationDAO.selectPage(
                RepositorySupport.page(query),
                marketGoodsRelationDAO.buildQueryWrapper(query)
        );
        Page<MarketGoodsRelationDTO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(toDTOList(page.getRecords()));
        return result;
    }

    @Override
    public List<MarketGoodsRelationDTO> queryGoodsRelationListByDTO(GoodsRelationQueryDTO query) {
        return toDTOList(marketGoodsRelationDAO.selectList(marketGoodsRelationDAO.buildQueryWrapper(query)));
    }

    /**
     * 反查订阅指定 SPU 的渠道商账户ID列表
     *
     * <p>一次取回 goods_id=spuId 的全部关系行, 内存分流两路: SELECT_GOODS 的 user_id 直接是渠道商;
     * MARKET_GOODS 的 market_id 再查 market_bind 中 bind_type=CHANNEL 且 state=YES 的 user_id。
     * 单 SPU 的关系行量级是「几个市场 × 几个渠道」, 一次取回比按 relation_type 发两条 SQL 便宜</p>
     *
     * <p>逻辑删由 {@code @TableLogic} 自动追加 del_flag 条件兜住, 故 A 路不加 state 过滤
     * (market_goods_relation.state 无业务写入点, 加了结果集恒空); B 路的 market_bind.state 是活字段,
     * 解绑时置 NO 而不走逻辑删, 必须显式过滤</p>
     *
     * @param spuId SPU 主键
     * @return 渠道商账户ID列表 无订阅返回空列表
     */
    @Override
    public List<Long> channelIdListBySpuId(Long spuId) {
        if (spuId == null) {
            return new ArrayList<>();
        }
        List<MarketGoodsRelationDO> relations = marketGoodsRelationDAO.selectList(
                new LambdaQueryWrapper<MarketGoodsRelationDO>()
                        .eq(MarketGoodsRelationDO::getGoodsId, spuId)
        );
        if (CollectionUtils.isEmpty(relations)) {
            return new ArrayList<>();
        }
        // A 选品路径: relation_type=SELECT_GOODS 的 user_id 即渠道商
        Set<Long> channelIds = relations.stream()
                .filter(r -> GoodsRelationEnum.GoodsRelation.SELECT_GOODS == r.getRelationType())
                .map(MarketGoodsRelationDO::getUserId)
                .filter(userId -> userId != null && userId > 0)
                .collect(Collectors.toSet());
        // B 专区路径: relation_type=MARKET_GOODS 的 market_id 反查绑定生效的渠道商
        List<Long> marketIdList = relations.stream()
                .filter(r -> GoodsRelationEnum.GoodsRelation.MARKET_GOODS == r.getRelationType())
                .map(MarketGoodsRelationDO::getMarketId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(marketIdList)) {
            List<MarketBindDO> binds = marketBindDAO.selectList(
                    new LambdaQueryWrapper<MarketBindDO>()
                            .in(MarketBindDO::getMarketId, marketIdList)
                            .eq(MarketBindDO::getBindType, AccountEnum.Identity.CHANNEL)
                            .eq(MarketBindDO::getState, CommonEnum.YesOrNo.YES)
            );
            binds.stream()
                    .map(MarketBindDO::getUserId)
                    .filter(userId -> userId != null && userId > 0)
                    .forEach(channelIds::add);
        }
        return new ArrayList<>(channelIds);
    }

    @Override
    public void batchSaveGoodsRelation(List<MarketGoodsRelationDTO> marketGoodsRelations) {
        if (CollectionUtils.isEmpty(marketGoodsRelations)) {
            return;
        }
        for (MarketGoodsRelationDTO dto : marketGoodsRelations) {
            marketGoodsRelationDAO.insert(toDO(dto));
        }
    }

    @Override
    public Page<GoodsRelationListVO> queryGoodsRelationList(GoodsListPageQuery req) {
        return marketGoodsRelationDAO.queryGoodsRelationList(RepositorySupport.page(req), req);
    }

    @Override
    public Page<GoodsRelationListVO> queryClientBindMarketGoodsRelationList(GoodsListPageQuery req) {
        return marketGoodsRelationDAO.queryClientBindMarketGoodsRelationList(RepositorySupport.page(req), req);
    }

    @Override
    public Page<GoodsRelationListVO> platformQueryMarketNotAddGoodsList(PlatformQueryMarketNotAddGoodsReq req) {
        return marketGoodsRelationDAO.platformQueryMarketNotAddGoodsList(RepositorySupport.page(req), req);
    }

    @Override
    public Page<GoodsRelationListVO> channelMarketNotSelectedGoodsList(PlatformQueryMarketNotAddGoodsReq req) {
        return marketGoodsRelationDAO.channelMarketNotSelectedGoodsList(RepositorySupport.page(req), req);
    }

    @Override
    public void channelCancelSelected(Long id) {
        // 取消选品 = 逻辑删除该条关系记录
        marketGoodsRelationDAO.deleteById(id);
    }

    @Override
    public Page<ApiChannelSpuRelationVO> channelSpuRelationList(GoodsListPageQuery query) {
        return marketGoodsRelationDAO.channelSpuRelationList(RepositorySupport.page(query), query);
    }

    @Override
    public void updateMarketGoodsLabel(UpdateGoodsRelationReq req) {
        MarketGoodsRelationDO relationDO = new MarketGoodsRelationDO();
        relationDO.setId(req.getId());
        relationDO.setGoodsInfo(parseGoodsInfo(req.getGoodsInfo()));
        marketGoodsRelationDAO.updateById(relationDO);
    }

    /**
     * DO 集合转 DTO 集合
     *
     * @param list DO 集合
     * @return DTO 集合, 恒非 null
     */
    private List<MarketGoodsRelationDTO> toDTOList(List<MarketGoodsRelationDO> list) {
        List<MarketGoodsRelationDTO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        for (MarketGoodsRelationDO relationDO : list) {
            result.add(toDTO(relationDO));
        }
        return result;
    }

    /**
     * JSON 字符串转商品信息对象
     *
     * <p>走 JSON 反序列化而非拼接 SQL 片段, 避免 goodsInfo 内容注入。</p>
     *
     * @param json 商品信息 JSON 字符串
     * @return 商品信息对象, 入参为空时返回 null
     */
    private MarketGoodsInfoVO parseGoodsInfo(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        return JSONUtil.toBean(json, MarketGoodsInfoVO.class);
    }

    /**
     * DO 转 DTO
     *
     * <p>{@code goodsInfo} 两侧类型不同(DO 为对象, DTO 为字符串), 逐字段手工映射,
     * 不用 TransferUtils 以免该字段被静默丢弃。</p>
     *
     * @param relationDO 关系 DO
     * @return 关系 DTO
     */
    private MarketGoodsRelationDTO toDTO(MarketGoodsRelationDO relationDO) {
        MarketGoodsRelationDTO dto = new MarketGoodsRelationDTO();
        dto.setId(relationDO.getId());
        dto.setGoodsId(relationDO.getGoodsId());
        dto.setMarketId(relationDO.getMarketId());
        dto.setRelationType(relationDO.getRelationType());
        dto.setUserId(relationDO.getUserId());
        dto.setSellNum(relationDO.getSellNum());
        dto.setSellAmount(relationDO.getSellAmount());
        dto.setState(relationDO.getState());
        dto.setDeBindTime(relationDO.getDeBindTime());
        dto.setCreateTime(relationDO.getCreateTime());
        dto.setDiscountRate(relationDO.getDiscountRate());
        dto.setGoodsInfo(relationDO.getGoodsInfo() == null ? null : relationDO.getGoodsInfo().toString());
        return dto;
    }

    /**
     * DTO 转 DO
     *
     * <p>{@code goodsInfo} 为字符串, DO 侧为 {@code MarketGoodsInfoVO} 对象列,
     * 无法在此安全反序列化(缺字段语义), 故不参与映射。</p>
     *
     * @param dto 关系 DTO
     * @return 关系 DO
     */
    private MarketGoodsRelationDO toDO(MarketGoodsRelationDTO dto) {
        MarketGoodsRelationDO relationDO = new MarketGoodsRelationDO();
        relationDO.setId(dto.getId());
        relationDO.setGoodsId(dto.getGoodsId());
        relationDO.setMarketId(dto.getMarketId());
        relationDO.setRelationType(dto.getRelationType());
        relationDO.setUserId(dto.getUserId());
        relationDO.setSellNum(dto.getSellNum());
        relationDO.setSellAmount(dto.getSellAmount());
        relationDO.setState(dto.getState());
        relationDO.setDeBindTime(dto.getDeBindTime());
        relationDO.setDiscountRate(dto.getDiscountRate());
        return relationDO;
    }
}
