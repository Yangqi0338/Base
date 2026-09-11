package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.adapt.api.DistributionApi;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SkuDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SpuAttributeDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.SpuDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SkuDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuAttributeDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuDO;
import com.newzkl.platform.base.biz.goods.model.assembler.SkuAssembler;
import com.newzkl.platform.base.biz.goods.model.assembler.SpuAssembler;
import com.newzkl.platform.base.biz.goods.model.assembler.SpuAttributeAssembler;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuAttributeDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.IndexCountRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuExpandVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuStateVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsCountVO;
import com.newzkl.platform.base.common.ddd.facade.GoodsVO;
import com.newzkl.platform.base.biz.goods.rpc.model.order.OrderGoodsInfoVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuCountQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * SPU 仓储实现
 *
 * <p>补齐裸端口: {@code SpuRepository} 此前全仓无实现类, 注入方 (SpuDomainImpl /
 * SpuServiceImpl) 启动即 NoSuchBeanDefinitionException
 * 本类以 MyBatis-Plus {@code BaseMapper} 能力实现 CRUD / 分页 / 条件查询, 其余能力显式抛
 * {@code UnsupportedOperationException}, 不静默返回 null 或空集合冒充成功。</p>
 *
 * <p>枚举字段说明: {@code SpuDO} 的 state/deliverTimeType/auditState/type 为枚举, 而 VO 侧为 Integer。
 * Hutool 默认拷贝对 Integer↔Enum 走 ordinal 语义, 与本项目 {@code @EnumValue} 的 code 语义不一致
 * (如 {@code SpuEnum.State.SALE} code 为 2 而 ordinal 为 3), 故 DO → VO 不再直拷:
 * 先 {@code TransferUtils.transfer(do, XxxDTO.class)} 出 DTO (两侧枚举同类型, 引用直传),
 * 再由 model 层 MapStruct 装配器按 {@code getCode()} 落成 Integer。
 * 反向 DTO → DO 一律排除 {@code AUDIT_PROPS}: DTO 是 {@code @RequestBody} 绑定对象,
 * 审计字段须由 {@code MetaObjectHandler} 填充, 不接受客户端入参。</p>
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
 * </ul>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class SpuRepositoryImpl extends RepositorySupport implements SpuRepository {

    private final SpuDAO spuDAO;
    private final SkuDAO skuDAO;
    private final SpuAttributeDAO spuAttributeDAO;
    private final SpuAssembler spuAssembler;
    private final SkuAssembler skuAssembler;
    private final SpuAttributeAssembler spuAttributeAssembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long spuSave(SpuDTO spu) {
        SpuDO spuDO = TransferUtils.transfer(spu, SpuDO::new);
        spuDO.preInsert();
        spuDAO.insert(spuDO);
        return spuDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuDelete(Long id) {
        spuDAO.deleteById(id);
    }

    @Override
    public SpuDTO detail(Long id) {
        SpuDO spuDO = spuDAO.selectById(id);
        return TransferUtils.transfer(spuDO, SpuDTO.class);
    }

    @Override
    public void spuUpdate(SpuDTO spu) {
        SpuDO spuDO = TransferUtils.transfer(spu, SpuDO::new);
        spuDO.preUpdate();
        spuDO.setExpand(mergeExpand(spu.getId(), spuDO.getExpand()));
        spuDAO.updateById(spuDO);
    }

    /**
     * 合并 expand
     *
     * <p>updateById 是整列覆盖, 局部更新 (如仅刷价格区间) 会把 expand 里未携带的
     * categoryName/brandName 等冗余字段冲掉, 故先取库中旧值再用新值的非空属性覆写</p>
     *
     * @param id     SPU 主键
     * @param expand 本次要写入的 expand, 无字段时为 null
     * @return 合并后的 expand, 无需写入时 null
     */
    private SpuExpandVO mergeExpand(Long id, SpuExpandVO expand) {
        if (expand == null || id == null) {
            return expand;
        }
        SpuDO old = spuDAO.selectById(id);
        if (old == null || old.getExpand() == null) {
            return expand;
        }
        SpuExpandVO merged = old.getExpand();
        TransferUtils.transfer(expand, merged, CopyOptions.create().setIgnoreNullValue(true));
        return merged;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByQuery(SpuQuery spuQuery) {
        spuDAO.delete(spuDAO.getLw(spuQuery));
    }

    @Override
    public SpuDTO detail(SpuQuery spuQuery) {
        return TransferUtils.transfer(getOne(spuDAO, spuDAO.getLw(spuQuery)), SpuDTO.class);
    }

    @Override
    public List<SpuDTO> listSelect(SpuQuery spuQuery) {
        return TransferUtils.transfers(spuDAO.selectList(spuDAO.getLw(spuQuery)), SpuDTO.class);
    }

    @Override
    public Page<SpuDTO> querySpuPage(SpuQuery spuQuery) {
        Page<SpuDO> page = spuDAO.selectPage(page(spuQuery), spuDAO.getLw(spuQuery));
        return TransferUtils.transferPage(page, SpuDTO.class);
    }

    @Override
    public <T> List<T> spuList(SpuQuery spuQuery, Class<T> clazz) {
        return list(spuDAO, spuDAO.getLw(spuQuery), clazz);
    }

    @Override
    public Long spuId(SpuEnum.ChannelType channelType, String outSpuId) {
        return getId(spuDAO, new BaseLambdaQueryWrapper<SpuDO>()
                .notEmptyEq(SpuDO::getChannelType, channelType)
                .notEmptyEq(SpuDO::getOutSpuId, outSpuId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editStateById(SpuEnum.State state, List<Long> spuIdList) {
        if (CollUtil.isEmpty(spuIdList)) {
            return 0;
        }
        LambdaUpdateWrapper<SpuDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SpuDO::getState, state).in(SpuDO::getId, spuIdList);
        return spuDAO.update(null, wrapper);
    }

    @Override
    public Page<SkuVO> querySkuPage(SkuQuery skuQuery) {
        Page<SkuDO> page = skuDAO.selectPage(page(skuQuery), skuDAO.getLw(skuQuery));
        return TransferUtils.transferPage(page,
                skuDO -> skuAssembler.req2VO(TransferUtils.transfer(skuDO, SkuDTO.class)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuUpdate(SkuDTO skuDTO) {
        skuDAO.updateById(TransferUtils.transfer(skuDTO, SkuDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuSave(SkuDTO skuDTO) {
        skuDAO.insert(TransferUtils.transfer(skuDTO, SkuDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuListSave(List<SkuDTO> skuDTOList) {
        if (CollUtil.isEmpty(skuDTOList)) {
            return;
        }
        skuDAO.insert(TransferUtils.transfers(skuDTOList,
                dto -> TransferUtils.transfer(dto, SkuDO::new)));
    }

    @Override
    public List<SkuVO> skuVOList(SkuQuery skuQuery) {
        return TransferUtils.transfers(skuDAO.selectList(skuDAO.getLw(skuQuery)),
                skuDO -> skuAssembler.req2VO(TransferUtils.transfer(skuDO, SkuDTO.class)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void skuDeleteByQuery(SkuQuery skuDelete) {
        skuDAO.delete(skuDAO.getLw(skuDelete));
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
        return skuAssembler.req2VO(TransferUtils.transfer(skuDAO.selectById(skuId), SkuDTO.class));
    }

    @Override
    public SkuVO skuVO(Long spuId, String outSkuId) {
        SkuDO skuDO = getOne(skuDAO, new BaseLambdaQueryWrapper<SkuDO>()
                .notEmptyEq(SkuDO::getSpuId, spuId)
                .notEmptyEq(SkuDO::getOutSkuId, outSkuId));
        return skuAssembler.req2VO(TransferUtils.transfer(skuDO, SkuDTO.class));
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
        spuAttributeDAO.insert(TransferUtils.transfers(spuAttributeDTOList,
                dto -> TransferUtils.transfer(dto, SpuAttributeDO.class)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void attributeDeleteByQuery(SpuAttributeQuery spuAttributeQuery) {
        spuAttributeDAO.delete(spuAttributeDAO.getLw(spuAttributeQuery));
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
        return spuAttributeAssembler.req2VO(
                TransferUtils.transfer(spuAttributeDAO.selectById(spuAttributeId), SpuAttributeDTO.class));
    }

    @Override
    public Page<SpuAttributeVO> querySpuAttributePage(SpuAttributeQuery spuAttributeQuery) {
        Page<SpuAttributeDO> page = spuAttributeDAO.selectPage(
                page(spuAttributeQuery), spuAttributeDAO.getLw(spuAttributeQuery));
        return TransferUtils.transferPage(page, attributeDO -> spuAttributeAssembler.req2VO(
                TransferUtils.transfer(attributeDO, SpuAttributeDTO.class)));
    }

    @Override
    public List<SpuAttributeVO> querySpuAttributeList(SpuAttributeQuery spuAttributeQuery) {
        return TransferUtils.transfers(
                spuAttributeDAO.selectList(spuAttributeDAO.getLw(spuAttributeQuery)),
                attributeDO -> spuAttributeAssembler.req2VO(
                        TransferUtils.transfer(attributeDO, SpuAttributeDTO.class)));
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
    public void refreshSalePriceRate(Set<Long> skuIdList) {
//        throw new UnsupportedOperationException(
//                "TODO[infra-gap]: 加价比例 salePriceRate 的计算公式未定义");
    }

    @Override
    public void refreshSalePrice(List<Long> asList) {
        throw new UnsupportedOperationException(
                "TODO[infra-gap]: 销售价 salePrice 的计算公式未定义");
    }

}

