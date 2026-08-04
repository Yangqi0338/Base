package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SkuDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SpuAttributeDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SpuDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SkuDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuAttributeDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuDO;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuAttributeDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.IndexCountRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuSaleAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuStateVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.count.GoodsCountVO;
import com.newzkl.platform.base.biz.goods.rpc.model.order.GoodsVO;
import com.newzkl.platform.base.biz.goods.rpc.model.order.OrderGoodsInfoVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SpuCountQuery;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SpuQuery;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * SPU 仓储实现
 *
 * <p>补齐裸端口: {@code SpuRepository} 此前全仓无实现类, 注入方 (SpuDomainImpl /
 * GoodsZoneGoodsRelDomainServiceImpl / SpuServiceImpl) 启动即 NoSuchBeanDefinitionException
 * 本类以 MyBatis-Plus {@code BaseMapper} 能力实现 CRUD / 分页 / 条件查询, 其余能力显式抛
 * {@code UnsupportedOperationException}, 不静默返回 null 或空集合冒充成功。</p>
 *
 * <p>枚举字段说明: {@code SpuDO} 的 goodsType/state/deliverTimeType/auditState/specType 为枚举,
 * 而 DTO/VO 侧为 Integer/String。Hutool 默认拷贝对 Integer↔Enum 走 ordinal 语义, 与本项目
 * {@code @EnumValue} 的 code 语义不一致 (如 {@code SpuEnum.State.INIT} code 为 -1),
 * 故这些字段一律从拷贝中排除并按 code 显式互转。</p>
 *
 * <p>未实现方法 (gap) 一览:</p>
 * <ul>
 *   <li>{@code editColumn} — 运行期动态列名增量更新, {@code SpuDAO.editColumn} 的 XML 用
 *       {@code `${column.name}`} 拼列名, 存在 SQL 注入面且无列白名单可依据, 故不启用</li>
 *   <li>{@code indexCount} — 依赖 {@code SpuDAO.spuCount} 分组结果补齐时间轴, 需
 *       {@code SpuDAO.countByQuery} 与分组补全工具, 本仓尚未提供</li>
 *   <li>{@code countByCondition} — {@code SpuDAO.countByCondition} 的 XML 用
 *       {@code ${query.fieldSQL}} 等拼 SQL 片段, 存在注入面, 口径待定, 故不启用</li>
 *   <li>{@code queryOrderSkuInfoVOList} — 接口签名 ({@code Long skuId} 单参) 与
 *       {@code SkuDAO.queryOrderSkuInfoVOList} (入参 {@code List<Long> skuIdList}) 不匹配,
 *       映射口径未定</li>
 *   <li>{@code resetSpuOrderCount} — 重置口径 (重置哪些计数列、作用范围) 未定义</li>
 *   <li>{@code spuDownAfter} / {@code spuUpAfter} — 上下架后置处理的跨模块副作用未定义</li>
 *   <li>{@code refreshSalePriceRate} / {@code refreshSalePrice} — 加价比例与销售价的计算公式
 *       未在本仓定义</li>
 *   <li>{@code notifyUp} / {@code notifySpuBase} / {@code notifySkuDelete} /
 *       {@code notifySkuEdit} — 开发者通知的外部投递通道 (HTTP 回调或 MQ) 未定义</li>
 * </ul>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class SpuRepositoryImpl implements SpuRepository {

    /**
     * SpuDO 与 DTO/VO 之间类型不一致、需显式转换的属性
     */
    private static final String[] SPU_ENUM_PROPS =
            {"goodsType", "state", "deliverTimeType", "auditState", "specType"};

    private final SpuDAO spuDAO;
    private final SkuDAO skuDAO;
    private final SpuAttributeDAO spuAttributeDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long spuSave(SpuDTO spu) {
        SpuDO spuDO = toSpuDO(spu);
        spuDAO.insert(spuDO);
        return spuDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuDelete(Long id) {
        spuDAO.deleteById(id);
    }

    @Override
    public SpuDTO getById(Long id) {
        SpuDO spuDO = spuDAO.selectById(id);
        if (spuDO == null) {
            return null;
        }
        return TransferUtils.transfer(spuDO, SpuDTO::new, (s, d) -> {
            d.setGoodsType(s.getGoodsType() == null ? null : s.getGoodsType().getCode());
            d.setState(s.getState() == null ? null : s.getState().getCode());
            d.setDeliverTimeType(s.getDeliverTimeType() == null ? null : s.getDeliverTimeType().getCode());
            d.setAuditState(s.getAuditState() == null ? null : s.getAuditState().getCode());
            d.setSpecType(s.getSpecType() == null ? null : s.getSpecType().name());
        }, ignoring(SPU_ENUM_PROPS));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuUpdate(SpuDTO spu) {
        spuDAO.updateById(toSpuDO(spu));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByQuery(SpuQuery spuQuery) {
        spuDAO.delete(requireCondition(spuWrapper(spuQuery)));
    }

    @Override
    public SpuVO voByQuery(SpuQuery spuQuery) {
        List<SpuDO> list = spuDAO.selectList(spuWrapper(spuQuery).last("limit 1"));
        return CollUtil.isEmpty(list) ? null : toSpuVO(list.get(0));
    }

    @Override
    public List<SpuVO> listSelect(SpuQuery spuQuery) {
        return TransferUtils.transfers(spuDAO.selectList(spuWrapper(spuQuery)), this::toSpuVO);
    }

    @Override
    public Page<SpuVO> querySpuPage(SpuQuery spuQuery) {
        Page<SpuDO> page = spuDAO.selectPage(RepositorySupport.page(spuQuery), spuWrapper(spuQuery));
        return TransferUtils.transferPage(page, this::toSpuVO);
    }

    @Override
    public List<SpuStateVO> spuStateList(SpuQuery spuQuery) {
        List<SpuDO> list = spuDAO.selectList(
                spuWrapper(spuQuery).select(SpuDO::getId, SpuDO::getState));
        return TransferUtils.transfers(list, spuDO -> {
            SpuStateVO vo = new SpuStateVO();
            vo.setId(spuDO.getId());
            vo.setState(spuDO.getState() == null ? null : spuDO.getState().getCode());
            return vo;
        });
    }

    @Override
    public Long spuId(Integer channelType, String outSpuId) {
        // select 返回父类 LambdaQueryWrapper, 会丢掉 BaseLambdaQueryWrapper 的 notEmptyXxx, 故放链尾
        List<SpuDO> list = spuDAO.selectList(new BaseLambdaQueryWrapper<SpuDO>()
                .notEmptyEq(SpuDO::getChannelType, SpuEnum.ChannelType.getByCode(channelType))
                .notEmptyEq(SpuDO::getOutSpuId, outSpuId)
                .last("limit 1")
                .select(SpuDO::getId));
        return CollUtil.isEmpty(list) ? null : list.get(0).getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editStateById(Integer state, List<Long> spuIdList) {
        if (CollUtil.isEmpty(spuIdList)) {
            return 0;
        }
        LambdaUpdateWrapper<SpuDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SpuDO::getState, stateOf(state)).in(SpuDO::getId, spuIdList);
        return spuDAO.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuSelectorNumAdd(List<Long> spuIdList, Integer num) {
        if (CollUtil.isEmpty(spuIdList) || num == null) {
            return;
        }
        LambdaUpdateWrapper<SpuDO> wrapper = new LambdaUpdateWrapper<>();
        // num 为 Integer, 拼接不存在注入面
        wrapper.setSql("selection_num = IFNULL(selection_num, 0) + " + num)
                .in(SpuDO::getId, spuIdList);
        spuDAO.update(null, wrapper);
    }

    @Override
    public Page<SkuVO> querySkuPage(SkuQuery skuQuery) {
        Page<SkuDO> page = skuDAO.selectPage(RepositorySupport.page(skuQuery), skuWrapper(skuQuery));
        return TransferUtils.transferPage(page, this::toSkuVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuUpdate(SkuDTO skuDTO) {
        skuDAO.updateById(toSkuDO(skuDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuSave(SkuDTO skuDTO) {
        skuDAO.insert(toSkuDO(skuDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuListSave(List<SkuDTO> skuDTOList) {
        if (CollUtil.isEmpty(skuDTOList)) {
            return;
        }
        skuDAO.insert(TransferUtils.transfers(skuDTOList, this::toSkuDO));
    }

    @Override
    public List<SkuVO> skuVOList(SkuQuery skuQuery) {
        return TransferUtils.transfers(skuDAO.selectList(skuWrapper(skuQuery)), this::toSkuVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuDeleteByQuery(SkuQuery skuDelete) {
        skuDAO.delete(requireCondition(skuWrapper(skuDelete)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuDeleteBySpuId(Long spuId) {
        if (spuId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "spuId 不能为空");
        }
        skuDAO.delete(new BaseLambdaQueryWrapper<SkuDO>().eq(SkuDO::getSpuId, spuId));
    }

    @Override
    public SkuVO skuVO(Long skuId) {
        SkuDO skuDO = skuDAO.selectById(skuId);
        return skuDO == null ? null : toSkuVO(skuDO);
    }

    @Override
    public SkuVO skuVO(Long spuId, String outSkuId) {
        List<SkuDO> list = skuDAO.selectList(new BaseLambdaQueryWrapper<SkuDO>()
                .notEmptyEq(SkuDO::getSpuId, spuId)
                .notEmptyEq(SkuDO::getOutSkuId, outSkuId)
                .last("limit 1"));
        return CollUtil.isEmpty(list) ? null : toSkuVO(list.get(0));
    }

    @Override
    public float maxSalePriceRate(Long spuId) {
        List<SkuDO> list = skuDAO.selectList(new BaseLambdaQueryWrapper<SkuDO>()
                .select(SkuDO::getSalePriceRate)
                .eq(SkuDO::getSpuId, spuId));
        return (float) list.stream().map(SkuDO::getSalePriceRate).filter(Objects::nonNull)
                .mapToDouble(Float::doubleValue).max().orElse(0D);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuAttributeListSave(List<SpuAttributeDTO> spuAttributeDTOList) {
        if (CollUtil.isEmpty(spuAttributeDTOList)) {
            return;
        }
        spuAttributeDAO.insert(TransferUtils.transfers(spuAttributeDTOList, this::toSpuAttributeDO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void attributeDeleteByQuery(SpuAttributeQuery spuAttributeQuery) {
        spuAttributeDAO.delete(requireCondition(spuAttributeWrapper(spuAttributeQuery)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuAttributeDeleteBySpuId(Long spuId) {
        if (spuId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "spuId 不能为空");
        }
        spuAttributeDAO.delete(
                new BaseLambdaQueryWrapper<SpuAttributeDO>().eq(SpuAttributeDO::getSpuId, spuId));
    }

    @Override
    public SpuAttributeVO spuAttributeById(Long spuAttributeId) {
        SpuAttributeDO attributeDO = spuAttributeDAO.selectById(spuAttributeId);
        return attributeDO == null ? null : toSpuAttributeVO(attributeDO);
    }

    @Override
    public Page<SpuAttributeVO> querySpuAttributePage(SpuAttributeQuery spuAttributeQuery) {
        Page<SpuAttributeDO> page = spuAttributeDAO.selectPage(
                RepositorySupport.page(spuAttributeQuery), spuAttributeWrapper(spuAttributeQuery));
        return TransferUtils.transferPage(page, this::toSpuAttributeVO);
    }

    @Override
    public List<SpuAttributeVO> querySpuAttributeList(SpuAttributeQuery spuAttributeQuery) {
        return TransferUtils.transfers(
                spuAttributeDAO.selectList(spuAttributeWrapper(spuAttributeQuery)),
                this::toSpuAttributeVO);
    }

    @Override
    public List<SpuCategoryVO> countSpuByCategory(List<Long> categoryIdList) {
        // 空集合直接短路: XML 用 foreach 拼 IN, 空 list 会生成 `IN ()` 语法错
        if (CollUtil.isEmpty(categoryIdList)) {
            return List.of();
        }
        return spuDAO.countSpuByCategory(categoryIdList);
    }

    @Override
    public void editColumn(Long id, List<EditColumnVO> editColumnDTOS) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 缺 SpuDAO.editColumn 的 mapper XML, 且动态列名增量更新无列白名单可依据");
    }

    @Override
    public IndexCountRes indexCount(TimeQuery timeQuery) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 缺 SpuDAO.spuCount 的 mapper XML (按时间分组统计 SPU 数)");
    }

    @Override
    public List<Map<String, Object>> countByCondition(SpuCountQuery countQuery) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 缺 SpuDAO.countByCondition 的 mapper XML (按供应商分组统计)");
    }

    @Override
    public GoodsCountVO goodsCountVO(Long supplierId) {
        return spuDAO.goodsCountVO(supplierId);
    }

    @Override
    public List<OrderGoodsInfoVO> queryOrderGoodsInfoVOList(List<GoodsVO> goods, Long channelId, Long storeId) {
        return skuDAO.queryOrderGoodsInfoVOList(goods, channelId, storeId);
    }

    @Override
    public OrderGoodsInfoVO queryOrderSkuInfoVOList(Long skuId) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 缺 SkuDAO.queryOrderSkuInfoVOList 的 mapper XML");
    }

    @Override
    public void resetSpuOrderCount() {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 重置口径未定义 (待重置的计数列与作用范围均无出处)");
    }

    @Override
    public void spuDownAfter(List<Long> spuIdList) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: SPU 下架后置处理的跨模块副作用未定义");
    }

    @Override
    public void spuUpAfter(List<Long> spuIdList) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: SPU 上架后置处理的跨模块副作用未定义");
    }

    @Override
    public void refreshSalePriceRate(Set<Long> skuIdList) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 加价比例 salePriceRate 的计算公式未定义");
    }

    @Override
    public void refreshSalePrice(List<Long> asList) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 销售价 salePrice 的计算公式未定义");
    }

    @Override
    public void notifyUp(Long spuId, Integer state) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 开发者通知 (SPU 上下架) 的外部投递通道未定义");
    }

    @Override
    public void notifySpuBase(Long spuId) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 开发者通知 (SPU 基本信息) 的外部投递通道未定义");
    }

    @Override
    public void notifySkuDelete(Long spuId, Long skuId) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 开发者通知 (SKU 删除) 的外部投递通道未定义");
    }

    @Override
    public void notifySkuEdit(Long spuId, Long skuId) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 开发者通知 (SKU 变更) 的外部投递通道未定义");
    }

    /**
     * 构建 SPU 条件包装器
     *
     * <p>条件集合与 {@code SpuDAO.buildQueryWrapper} 保持一致 (含 supplierIdList 映射到
     * account_id 的既有口径), 并补齐 SpuDO 上确有对应列的 salePrice / saleNum 区间。
     * marketId / relationType / type / profit 区间涉及市场关联表, 本包装器不处理。</p>
     *
     * @param query SPU 查询
     * @return 条件包装器, 恒非 null
     */
    private BaseLambdaQueryWrapper<SpuDO> spuWrapper(SpuQuery query) {
        BaseLambdaQueryWrapper<SpuDO> wrapper = new BaseLambdaQueryWrapper<>(SpuDO.class);
        if (query == null) {
            return wrapper;
        }
        wrapper.notEmptyIn(SpuDO::getId, query.getIdList())
                .notEmptyIn(SpuDO::getAccountId, query.getAccountIdList())
                .notEmptyIn(SpuDO::getAccountId, query.getSupplierIdList())
                .notEmptyIn(SpuDO::getCode, query.getCodeList())
                .notEmptyIn(SpuDO::getCategoryId, query.getCategoryIdList())
                .notEmptyIn(SpuDO::getState, query.getStateList())
                .notEmptyIn(SpuDO::getAuditState, query.getAuditStateList())
                .notEmptyLike(SpuDO::getName, query.getName())
                .notEmptyLike(SpuDO::getTitle, query.getTitle())
                .notEmptyLike(SpuDO::getAccountName, query.getAccountName());
        wrapper.notEmptyEq(SpuDO::getGoodsType, saleTypeOf(query.getGoodsType()))
                .notEmptyEq(SpuDO::getDeliverTimeType, query.getDeliverTimeType())
                .notEmptyEq(SpuDO::getChannelType, query.getChannelType())
                .notEmptyEq(SpuDO::getSpecType, query.getSpecType())
                .notEmptyEq(SpuDO::getRole, query.getRole())
                .notEmptyEq(SpuDO::getBrandId, query.getBrandId())
                .notEmptyEq(SpuDO::getFreightTemplateId, query.getFreightTemplateId())
                .notEmptyEq(SpuDO::getOutSpuId, query.getOutSpuId())
                .notEmptyEq(SpuDO::getMinPricingNum, query.getMinPricingNum())
                // 价格列已 Money 化, 区间入参 (SpuQuery 分 Integer) 显式 Money.of(分) 后 doBetween
                .doBetween(SpuDO::getSupplyPrice, moneyBound(query.getSupplyPriceStart()), moneyBound(query.getSupplyPriceEnd()))
                .doBetween(SpuDO::getSalePrice, moneyBound(query.getSalePriceStart()), moneyBound(query.getSalePriceEnd()))
                // saleNum 为数量列 (Integer), 保持 Integer 区间
                .doBetween(SpuDO::getSaleNum, query.getSaleNumStart(), query.getSaleNumEnd())
                .between(SpuDO::getCreateTime, query.getCreateTime());
        return wrapper;
    }

    /**
     * 构建 SKU 条件包装器
     *
     * @param query SKU 查询
     * @return 条件包装器, 恒非 null
     */
    private BaseLambdaQueryWrapper<SkuDO> skuWrapper(SkuQuery query) {
        BaseLambdaQueryWrapper<SkuDO> wrapper = new BaseLambdaQueryWrapper<>(SkuDO.class);
        if (query == null) {
            return wrapper;
        }
        wrapper.notEmptyIn(SkuDO::getId, query.getIdList())
                .notEmptyIn(SkuDO::getSpuId, query.getSpuIdList());
        return wrapper;
    }

    /**
     * 构建 SPU 属性条件包装器
     *
     * @param query SPU 属性查询
     * @return 条件包装器, 恒非 null
     */
    private BaseLambdaQueryWrapper<SpuAttributeDO> spuAttributeWrapper(SpuAttributeQuery query) {
        BaseLambdaQueryWrapper<SpuAttributeDO> wrapper = new BaseLambdaQueryWrapper<>(SpuAttributeDO.class);
        if (query == null) {
            return wrapper;
        }
        wrapper.notEmptyIn(SpuAttributeDO::getId, query.getIdList())
                .notEmptyIn(SpuAttributeDO::getSpuId, query.getSpuIdList())
                .notEmptyEq(SpuAttributeDO::getType, attributeTypeOf(query.getType()));
        return wrapper;
    }

    /**
     * 校验批量删除条件非空
     *
     * <p>入参查询对象若未携带任何条件, 包装器会退化为无 where 条件, 导致全表逻辑删除
     * 此处显式拒绝, 而非静默执行。</p>
     *
     * @param wrapper 条件包装器
     * @param <T>     实体类型
     * @return 原包装器
     */
    private <T> BaseLambdaQueryWrapper<T> requireCondition(BaseLambdaQueryWrapper<T> wrapper) {
        String segment = wrapper.getSqlSegment();
        if (segment == null || segment.isBlank()) {
            throw new PlatformException(BaseErrorCode.PARAM, "批量删除必须携带条件");
        }
        return wrapper;
    }

    /**
     * SpuDTO 转 SpuDO
     *
     * @param dto SPU 操作对象
     * @return SPU 数据对象, 入参为 null 时返回 null
     */
    private SpuDO toSpuDO(SpuDTO dto) {
        return TransferUtils.transfer(dto, SpuDO::new, (s, d) -> {
            d.setGoodsType(saleTypeOf(s.getGoodsType()));
            d.setState(stateOf(s.getState()));
            d.setDeliverTimeType(deliverTimeTypeOf(s.getDeliverTimeType()));
            d.setAuditState(AuditEnum.State.getByCode(s.getAuditState()));
            d.setSpecType(specTypeOf(s.getSpecType()));
        }, ignoring(SPU_ENUM_PROPS));
    }

    /**
     * SpuDO 转 SpuVO
     *
     * @param spuDO SPU 数据对象
     * @return SPU 视图对象, 入参为 null 时返回 null
     */
    private SpuVO toSpuVO(SpuDO spuDO) {
        return TransferUtils.transfer(spuDO, SpuVO::new, (s, v) -> {
            v.setGoodsType(s.getGoodsType() == null ? null : s.getGoodsType().getCode());
            v.setState(s.getState() == null ? null : s.getState().getCode());
            v.setDeliverTimeType(s.getDeliverTimeType() == null ? null : s.getDeliverTimeType().getCode());
            v.setAuditState(s.getAuditState() == null ? null : s.getAuditState().getCode());
            v.setSpecType(s.getSpecType() == null ? null : s.getSpecType().name());
        }, ignoring(SPU_ENUM_PROPS));
    }

    /**
     * SkuDTO 转 SkuDO
     *
     * <p>saleAttribute 在 DTO 侧为对象列表, 在 DO 侧为 JSON 列, 故单独序列化。</p>
     *
     * @param dto SKU 操作对象
     * @return SKU 数据对象, 入参为 null 时返回 null
     */
    private SkuDO toSkuDO(SkuDTO dto) {
        return TransferUtils.transfer(dto, SkuDO::new, (s, d) ->
                        d.setSaleAttribute(s.getSaleAttribute() == null
                                ? null : JSON.toJSONString(s.getSaleAttribute())),
                ignoring("saleAttribute"));
    }

    /**
     * SkuDO 转 SkuVO
     *
     * <p>DO 的 JSON 列同时回填 VO 的 saleAttributeJson (原文) 与 saleAttribute (解析结果),
     * 与 {@code GoodsQueryServiceImpl} 既有用法保持一致。</p>
     *
     * @param skuDO SKU 数据对象
     * @return SKU 视图对象, 入参为 null 时返回 null
     */
    private SkuVO toSkuVO(SkuDO skuDO) {
        return TransferUtils.transfer(skuDO, SkuVO::new, (s, v) -> {
            v.setSaleAttributeJson(s.getSaleAttribute());
            v.setSaleAttribute(s.getSaleAttribute() == null
                    ? null : JSON.parseArray(s.getSaleAttribute(), SkuSaleAttributeVO.class));
        }, ignoring("saleAttribute"));
    }

    /**
     * SpuAttributeDTO 转 SpuAttributeDO
     *
     * @param dto SPU 属性操作对象
     * @return SPU 属性数据对象, 入参为 null 时返回 null
     */
    private SpuAttributeDO toSpuAttributeDO(SpuAttributeDTO dto) {
        return TransferUtils.transfer(dto, SpuAttributeDO::new,
                (s, d) -> d.setType(attributeTypeOf(s.getType())), ignoring("type"));
    }

    /**
     * SpuAttributeDO 转 SpuAttributeVO
     *
     * @param attributeDO SPU 属性数据对象
     * @return SPU 属性视图对象, 入参为 null 时返回 null
     */
    private SpuAttributeVO toSpuAttributeVO(SpuAttributeDO attributeDO) {
        return TransferUtils.transfer(attributeDO, SpuAttributeVO::new,
                (s, v) -> v.setType(s.getType() == null ? null : s.getType().getCode()),
                ignoring("type"));
    }

    /**
     * 构建排除指定属性的拷贝选项
     *
     * @param props 需排除的属性名
     * @return 拷贝选项
     */
    private static CopyOptions ignoring(String... props) {
        return CopyOptions.create().setIgnoreProperties(props);
    }

    /**
     * 分区间边界 (Integer 分) 显式升 Money, null 保持 null 以便 doBetween 跳过该侧条件
     *
     * @param cent 分, 可为 null
     * @return Money 或 null
     */
    private static Money moneyBound(Integer cent) {
        return cent == null ? null : Money.of(cent);
    }

    /**
     * code 转 SPU 状态枚举
     *
     * @param code 状态码
     * @return 状态枚举, 无匹配返回 null
     */
    private static SpuEnum.State stateOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (SpuEnum.State state : SpuEnum.State.values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }

    /**
     * code 转商品类型枚举
     *
     * @param code 商品类型码
     * @return 商品类型枚举, 无匹配返回 null
     */
    private static SpuEnum.SaleType saleTypeOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (SpuEnum.SaleType saleType : SpuEnum.SaleType.values()) {
            if (saleType.getCode().equals(code)) {
                return saleType;
            }
        }
        return null;
    }

    /**
     * code 转发货时效类型枚举
     *
     * @param code 发货时效类型码
     * @return 发货时效类型枚举, 无匹配返回 null
     */
    private static SpuEnum.DeliverTimeType deliverTimeTypeOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (SpuEnum.DeliverTimeType type : SpuEnum.DeliverTimeType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * code 转 SPU 属性类型枚举
     *
     * @param code 属性类型码
     * @return 属性类型枚举, 无匹配返回 null
     */
    private static SpuEnum.SpuAttributeType attributeTypeOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (SpuEnum.SpuAttributeType type : SpuEnum.SpuAttributeType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 名称转规格类型枚举
     *
     * <p>DTO/VO 侧的 specType 存的是枚举 name (见 {@code SpuDomainImpl.setSpecType}),
     * 而非 code。</p>
     *
     * @param name 规格类型枚举名
     * @return 规格类型枚举, 无匹配返回 null
     */
    private static SpuEnum.SpecType specTypeOf(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (SpuEnum.SpecType type : SpuEnum.SpecType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }
}

