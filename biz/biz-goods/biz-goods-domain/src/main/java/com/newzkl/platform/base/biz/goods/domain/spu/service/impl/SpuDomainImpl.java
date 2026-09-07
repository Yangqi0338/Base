package com.newzkl.platform.base.biz.goods.domain.spu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.adapt.api.EventApi;
import com.newzkl.platform.base.biz.goods.domain.brand.repository.BrandRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuCategoryRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuAttributeDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.OutSpuEditCommand;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.SpuCategoryReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.SpuAttributeDiffRes;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.SpuAuditRes;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.SpuUpdateRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.*;
import com.newzkl.platform.base.common.ddd.facade.GoodsCountVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.SpuErrorCode;
import com.newzkl.platform.base.common.core.model.properties.SysProperties;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SPU领域服务实现类
 * 负责SPU相关的核心业务逻辑处理
 *
 * @author fang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpuDomainImpl implements SpuDomain {

    private final SpuRepository spuRepository;
    private final SpuCategoryRepository spuCategoryRepository;
    private final BrandRepository brandRepository;
    private final EventApi eventApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long spuPreSave(SpuDTO spuDTO) {
        // 构建并保存SKU
        List<SkuDTO> skuDTOList = buildSkuList(spuDTO);

        // 构建并保存SPU（包含统计信息）
        SpuDTO spu = buildSpu(spuDTO, true, skuDTOList);

        // 持久化
        Long spuId = spuRepository.spuSave(spu);

        // 设置SKU的spuId与编码
        bindSku(skuDTOList, spuId, spu.getCode());

        // 构建并保存属性
        List<SpuAttributeDTO> spuAttributeDTOList = buildSpuAttributeList(spuDTO, spuId);

        if (ObjectUtil.isNotEmpty(spuAttributeDTOList)) {
            spuRepository.spuAttributeListSave(spuAttributeDTOList);
        }
        if (ObjectUtil.isNotEmpty(skuDTOList)) {
            spuRepository.skuListSave(skuDTOList);
        }

        return spu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long spuCreate(SpuDTO spuDTO) {
        // 构建并保存SKU
        List<SkuDTO> skuDTOList = buildSkuList(spuDTO);

        // 构建并保存SPU（包含统计信息）
        SpuDTO spu = buildSpu(spuDTO, false, skuDTOList);

        // 持久化
        Long spuId = spuRepository.spuSave(spu);

        // 设置SKU的spuId与编码
        bindSku(skuDTOList, spuId, spu.getCode());

        // 构建并保存属性
        List<SpuAttributeDTO> spuAttributeDTOList = buildSpuAttributeList(spuDTO, spuId);

        if (ObjectUtil.isNotEmpty(spuAttributeDTOList)) {
            spuRepository.spuAttributeListSave(spuAttributeDTOList);
        }
        if (ObjectUtil.isNotEmpty(skuDTOList)) {
            spuRepository.skuListSave(skuDTOList);
        }
        return spu.getId();
    }

    /**
     * 判断金额是否有值 (非 java null 且非 Money NULL 语义)
     *
     * @param money 待判断金额
     * @return 有值 true
     */
    private boolean isPresent(Money money) {
        return money != null && !money.isNull();
    }

    /**
     * 取两金额较小者, 累积方为 null 时直接取当前值
     *
     * @param acc 累积较小值, 可为 null
     * @param cur 当前金额
     * @return 较小金额
     */
    private Money minMoney(Money acc, Money cur) {
        return acc == null ? cur : (cur.smallerThan(acc) ? cur : acc);
    }

    /**
     * 取两金额较大者, 累积方为 null 时直接取当前值
     *
     * @param acc 累积较大值, 可为 null
     * @param cur 当前金额
     * @return 较大金额
     */
    private Money maxMoney(Money acc, Money cur) {
        return acc == null ? cur : (cur.greaterThan(acc) ? cur : acc);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void spuPreUpdate(SpuDTO spuDTO) {
        SpuDTO spu = spuRepository.detail(spuDTO.getId());
        if (spu == null || !CollUtil.newArrayList(SpuEnum.State.STORE, SpuEnum.State.INIT).contains(spu.getState())) {
            throw new PlatformException(SpuErrorCode.NOT_EXIST_OR_STATE_ERROR);
        }
        // 仓库中商品直接编辑: 复用 spuUpdate 差异化更新, 不删主表重插, 保住 spuId/skuId 与已有关联数据
        spuUpdate(spuDTO);
    }

    @Override
    public SpuDTO detail(Long id) {
        return spuRepository.detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpuUpdateRes spuUpdate(SpuDTO spuDTO) {
        log.info("待修改聚合信息: {}", JSONObject.toJSONString(spuDTO));

        SpuUpdateRes spuUpdateRes = new SpuUpdateRes();

        // 处理销售属性变更
        handleSpuAttributeChange(spuDTO, spuUpdateRes);

        // 处理参数属性
        updateAttributes(spuDTO);

        // 更新SKU信息
        updateSkuList(spuDTO, spuUpdateRes);

        // 根据SKU列表计算SPU统计信息
        calculateSpuStatistics(spuDTO, spuDTO.getSkuList());

        // 分类/品牌变更时同步冗余名称
        fillCategoryAndBrandName(spuDTO);

        // 更新SPU基本信息
        spuRepository.spuUpdate(spuDTO);

        // 发商品业务事件: 按 SKU 变更结果推断语义类型, 无 SKU 变更则视为 SPU 基础信息变更
        publishSpuUpdateEvent(spuDTO.getId(), spuUpdateRes);
        return spuUpdateRes;
    }

    /**
     * 依 SPU 更新结果发布商品业务事件
     *
     * <p>删除有 SKU → 规格删除; 新增或更新有 SKU → 规格变更; 均无 → SPU 基础信息变更</p>
     *
     * @param spuId SPU 主键
     * @param res SPU 更新结果, 含新增/更新/删除的 SKU 主键
     */
    private void publishSpuUpdateEvent(Long spuId, SpuUpdateRes res) {
        List<Long> deleteSkuIdList = res.getDeleteSkuIdList();
        List<Long> addSkuIdList = res.getAddSkuIdList();
        List<Long> updateSkuIdList = res.getUpdateSkuIdList();
        boolean hasDelete = CollUtil.isNotEmpty(deleteSkuIdList);
        boolean hasChange = CollUtil.isNotEmpty(addSkuIdList) || CollUtil.isNotEmpty(updateSkuIdList);
        if (hasDelete) {
            eventApi.publishSkuDelete(spuId, deleteSkuIdList);
        }
        if (hasChange) {
            List<Long> changedSkuIdList = new ArrayList<>();
            if (CollUtil.isNotEmpty(addSkuIdList)) {
                changedSkuIdList.addAll(addSkuIdList);
            }
            if (CollUtil.isNotEmpty(updateSkuIdList)) {
                changedSkuIdList.addAll(updateSkuIdList);
            }
            eventApi.publishSkuEdit(spuId, changedSkuIdList);
        }
        if (!hasDelete && !hasChange) {
            eventApi.publishSpuEdit(spuId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuAuditSuccess(SpuVO spuCommand, Map<Long, String> skuSalePriceMap) {
        // 解析并更新SKU销售价
        List<SkuDTO> skuUpdateList = new ArrayList<>();
        List<Money> salePriceList = new ArrayList<>();
        List<Money> supplyPriceList = new ArrayList<>();

        parseAndCollectSkuPrices(spuCommand.getSkuList(), skuSalePriceMap,
                skuUpdateList, salePriceList, supplyPriceList);
        
        // 构建SPU更新对象并设置价格区间
        SpuDTO spu = buildSpuForAuditSuccess(spuCommand.getId(),
                skuUpdateList, salePriceList, supplyPriceList);

        // 执行更新
        spuRepository.spuUpdate(spu);

        // 平台改价时发 SKU 价格变更事件
        if (CollUtil.isNotEmpty(skuUpdateList)) {
            List<Long> priceSkuIdList = skuUpdateList.stream().map(SkuDTO::getId).collect(Collectors.toList());
            eventApi.publishSkuPrice(spuCommand.getId(), priceSkuIdList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int spuUp(CommonEnum.YesOrNo enable, List<Long> spuIdList) {
        SpuEnum.State targetState;
        if (CommonEnum.YesOrNo.YES == enable) {
            targetState = SpuEnum.State.SALE;
        }else if (SecurityUtils.getClient() == AccountEnum.Client.ADMIN) {
            targetState = SpuEnum.State.PLATFORM_DOWN;
        }else {
            targetState = SpuEnum.State.DOWN;
        }
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setIdList(spuIdList);
        List<SpuStateVO> stateList = spuRepository.spuList(spuQuery, SpuStateVO.class);
        for (SpuStateVO state : stateList) {
            if (CommonEnum.YesOrNo.YES == enable) {
                if (CollUtil.newArrayList(SpuEnum.State.DOWN, SpuEnum.State.PLATFORM_DOWN).contains(state.getState())) {
                    continue;
                }
            } else {
                if (SpuEnum.State.SALE == state.getState()) {
                    continue;
                }
            }
            throw new PlatformException(SpuErrorCode.NOT_EXIST_OR_STATE_ERROR);
        }
        int result = updateSpuState(spuIdList, targetState);
        // 发上下架事件, 原状态未知传 null
        eventApi.publishSaleState(spuIdList, null, targetState.getCode());
        return result;
    }

    @Override
    public List<SkuVO> skuVOList(SkuQuery skuQuery) {
        return spuRepository.skuVOList(skuQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuAuditFail(SpuVO spuVO, String lastRefuseReason) {
        // 状态修改
        SpuDTO spuUpdate = new SpuDTO();
        spuUpdate.setId(spuVO.getId());
        spuUpdate.setAuditState(AuditEnum.State.FAIL);
        spuUpdate.setLastRefuseReason(lastRefuseReason);
        spuRepository.spuUpdate(spuUpdate);
    }

    @Override
    public void spuSubmit(Long spuId) {
        // 状态修改
        SpuDTO spuUpdate = new SpuDTO();
        spuUpdate.setId(spuId);
        spuUpdate.setAuditState(AuditEnum.State.AUDITING);
        spuRepository.spuUpdate(spuUpdate);
    }

    @Override
    public void spuAuditStop(SpuVO spuVO) {
        // 状态修改
        SpuDTO spuUpdate = new SpuDTO();
        spuUpdate.setId(spuVO.getId());
        spuUpdate.setAuditState(AuditEnum.State.STOP);
        spuRepository.spuUpdate(spuUpdate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuDelete(List<Long> spuIdList) {
        if (CollUtil.isEmpty(spuIdList)) {
            return;
        }

        // 删除SPU关联数据
        SpuQuery spuDelete = new SpuQuery();
        spuDelete.setIdList(spuIdList);
        spuRepository.deleteByQuery(spuDelete);
        
        // 删除SKU
        SkuQuery skuDelete = new SkuQuery();
        skuDelete.setSpuIdList(spuIdList);
        spuRepository.skuDeleteByQuery(skuDelete);
        
        // 删除属性
        SpuAttributeQuery spuAttributeQuery = new SpuAttributeQuery();
        spuAttributeQuery.setSpuIdList(spuIdList);
        spuRepository.attributeDeleteByQuery(spuAttributeQuery);
    }

    @Override
    public void outSpuEdit(OutSpuEditCommand outSpuEditCommand) {
        // 组装SPU修改参数
        SpuDTO spuUpdate = buildSpuUpdateCommand(outSpuEditCommand);
        
        // 组装SKU修改参数
        Map<Long, SkuDTO> skuUpdateMap = buildSkuUpdateMap(outSpuEditCommand);
        spuUpdate.setSkuList(CollUtil.newArrayList(skuUpdateMap.values()));
        
        // 执行修改
        spuRepository.spuUpdate(spuUpdate);
        
        // 刷新加价比例
        spuRepository.refreshSalePriceRate(outSpuEditCommand.getSkuSalePrice().keySet());
    }

    @Override
    public Page<SpuVO> querySpuPage(SpuQuery spuQuery) {
        return TransferUtils.transferPage(spuRepository.querySpuPage(spuQuery), SpuVO.class);
    }

    @Override
    public Page<SpuAuditRes> spuAuditPage(SpuQuery spuQuery) {
        return TransferUtils.transferPage(spuRepository.querySpuPage(spuQuery), SpuAuditRes.class);
    }

    @Override
    public Page<SkuVO> querySkuPage(SkuQuery skuQuery) {
        return spuRepository.querySkuPage(skuQuery);
    }

    @Override
    public SpuVO voByQuery(SpuQuery spuQuery) {
        return TransferUtils.transfer(spuRepository.detail(spuQuery), SpuVO.class);
    }

    @Override
    public List<SpuAttributeVO> querySpuAttributeList(SpuAttributeQuery spuAttributeQuery) {
        return spuRepository.querySpuAttributeList(spuAttributeQuery);
    }

    @Override
    public List<SpuVO> listSelect(SpuQuery spuQuery) {
        return TransferUtils.transfers(spuRepository.listSelect(spuQuery), SpuVO.class);
    }

    @Override
    public GoodsCountVO goodsCountVO(Long supplierId) {
        return spuRepository.goodsCountVO(supplierId);
    }

    @Override
    public SkuVO skuVO(Long skuId) {
        return spuRepository.skuVO(skuId);
    }

    @Override
    public SpuAttributeVO spuAttributeById(Long spuAttributeId) {
        return spuRepository.spuAttributeById(spuAttributeId);
    }

    @Override
    public Page<SpuAttributeVO> querySpuAttributePage(SpuAttributeQuery spuAttributeQuery) {
        return spuRepository.querySpuAttributePage(spuAttributeQuery);
    }

    /**
     * 解析并收集SKU价格信息
     */
    private void parseAndCollectSkuPrices(List<SkuVO> skuList, Map<Long, String> skuSalePriceMap,
                                          List<SkuDTO> skuUpdateList,
                                         List<Money> salePriceList,
                                         List<Money> supplyPriceList) {



        for (SkuVO skuVO : skuList) {
            String amount = skuSalePriceMap.get(skuVO.getId());
            if (amount != null) {
                Money salePrice = Money.of(amount);
                SkuDTO sku = new SkuDTO();
                sku.setId(skuVO.getId());
                sku.setSalePrice(salePrice);
                skuUpdateList.add(sku);

                // 统计价格
                salePriceList.add(salePrice);
                if (isPresent(skuVO.getSupplyPrice())) {
                    supplyPriceList.add(skuVO.getSupplyPrice());
                }
            }
        }
    }

    /**
     * 构建审核通过时的SPU更新对象
     */
    private SpuDTO buildSpuForAuditSuccess(Long spuId, List<SkuDTO> skuList,
                                           List<Money> salePriceList,
                                           List<Money> supplyPriceList) {
        SpuDTO spu = new SpuDTO();
        spu.setId(spuId);
        spu.setState(SpuEnum.State.SALE);
        spu.setAuditState(AuditEnum.State.SUCCESS);
        spu.setSkuList(skuList);

        // 设置销售价格区间 (Money 无 Comparable, 遍历取极值)
        if (ObjectUtil.isNotEmpty(salePriceList)) {
            spu.getExpand().setSalePriceBegan(CollUtil.min(salePriceList));
            spu.getExpand().setSalePriceEnd(CollUtil.max(salePriceList));
        }

        // 设置供货价格区间
        if (ObjectUtil.isNotEmpty(supplyPriceList)) {
            spu.getExpand().setSupplierPriceBegan(CollUtil.min(supplyPriceList));
            spu.getExpand().setSupplierPriceEnd(CollUtil.max(supplyPriceList));
        }

        return spu;
    }

    /**
     * 更新SPU状态（上架/下架）
     */
    private int updateSpuState(List<Long> spuIdList, SpuEnum.State targetState) {
        int result = spuRepository.editStateById(targetState, spuIdList);
        
        // 执行上架/下架后置处理
        if (SpuEnum.State.SALE == targetState) {
            spuRepository.spuUpAfter(spuIdList);
        } else {
            spuRepository.spuDownAfter(spuIdList);
        }
        
        return result;
    }

    /**
     * 构建SPU更新命令对象
     */
    private SpuDTO buildSpuUpdateCommand(OutSpuEditCommand outSpuEditCommand) {
        SpuDTO spuUpdate = new SpuDTO();
        spuUpdate.setId(outSpuEditCommand.getSpuId());
        spuUpdate.setCategoryId(outSpuEditCommand.getCategoryId());

        spuUpdate.getExpand().setCategoryName(outSpuEditCommand.getCategoryName());

        spuUpdate.setBrandId(outSpuEditCommand.getBrandId());

        spuUpdate.getExpand().setBrandName(outSpuEditCommand.getBrandName());

        spuUpdate.setAuditState(AuditEnum.State.SUCCESS);
        spuUpdate.setState(SpuEnum.State.DOWN);
        return spuUpdate;
    }

    /**
     * 构建SKU更新映射
     */
    private Map<Long, SkuDTO> buildSkuUpdateMap(OutSpuEditCommand outSpuEditCommand) {
        Map<Long, SkuDTO> skuUpdateMap = new HashMap<>();
        
        // 处理销售价 (Command 入参为分 Integer, 显式 Money.of(分) 升为值对象)
        Map<Long, Integer> skuSalePrice = outSpuEditCommand.getSkuSalePrice();
        if (skuSalePrice != null) {
            skuSalePrice.forEach((skuId, salePrice) -> {
                if (salePrice != null) {
                    SkuDTO skuDTO = skuUpdateMap.computeIfAbsent(skuId, k -> new SkuDTO());
                    skuDTO.setId(skuId);
                    skuDTO.setSalePrice(Money.of(salePrice));
                }
            });
        }

        // 处理零售价 (Command 入参为分 Integer, 显式 Money.of(分) 升为值对象)
        Map<Long, Integer> skuUnitPrice = outSpuEditCommand.getSkuUnitPrice();
        if (skuUnitPrice != null) {
            skuUnitPrice.forEach((skuId, unitPrice) -> {
                if (unitPrice != null) {
                    SkuDTO skuDTO = skuUpdateMap.computeIfAbsent(skuId, k -> new SkuDTO());
                    skuDTO.setId(skuId);
                    skuDTO.setUnitPrice(Money.of(unitPrice));
                }
            });
        }
        
        return skuUpdateMap;
    }

    /**
     * 构建SKU列表
     */
    private List<SkuDTO> buildSkuList(SpuDTO spuDTO) {
        return TransferUtils.transfers(spuDTO.getSkuList(), SkuDTO::new, (c, v) -> {
            v.setSaleAttribute(TransferUtils.transfers(c.getSaleAttribute(), SkuSaleAttributeVO::new));
            // 设置默认SKU图片
            v.setImg(StrUtil.isNotEmpty(c.getImg()) ? c.getImg() : spuDTO.getImg());
        });
    }

    /**
     * 构建SPU数据对象
     */
    private SpuDTO buildSpu(SpuDTO spuDTO, boolean isPreSave, List<SkuDTO> skuDTOList) {
        return TransferUtils.transfer(spuDTO, SpuDTO::new, (c, v) -> {
            // 设置ID
            if (!isPreSave && spuDTO.getRefresh() != Boolean.TRUE && spuDTO.getId() != null) {
                v.setId(spuDTO.getId());
            } else {
                v.setId(SnowflakeGenerator.getSnowflakeId());
            }

            // 设置审核状态
            v.setAuditState(AuditEnum.State.CUSTOM);

            // 设置商品状态
            setSpuState(v, c, isPreSave);

            // 设置规格类型
            setSpecType(v, c);

            // 设置分类ID默认值
            if (v.getCategoryId() == null) {
                v.setCategoryId(0L);
            }

            // 编码后端强制生成, 不采信入参 (外部渠道商品已带编码时保留)
            if (StrUtil.isEmpty(c.getCode())) {
                v.setCode(BusinessCodeUtil.generate(BusinessType.SPU));
            }

            // 回填分类名/品牌名 (下沉 expand, 供列表与详情平展渲染)
            fillCategoryAndBrandName(v);

            // 计算并填充SKU统计信息
            calculateSpuStatistics(v, skuDTOList);
        });
    }

    /**
     * 回填分类名与品牌名
     *
     * <p>两者仅作展示用冗余, 落库进 spu.expand JSON 列</p>
     *
     * @param spu 待回填的 SPU
     */
    private void fillCategoryAndBrandName(SpuDTO spu) {
        SpuCategoryVO category = spuCategoryRepository.category(spu.getCategoryId());
        if (category != null) {
            spu.getExpand().setCategoryName(category.getName());
        }
        BrandVO brand = brandRepository.brandById(spu.getBrandId());
        if (brand != null) {
            spu.getExpand().setBrandName(brand.getName());
        }
    }

    /**
     * 绑定 SKU 的 spuId 与编码
     *
     * <p>skuCode = spuCode + "-" + 两位序号, 保证与所属 SPU 可视关联</p>
     *
     * @param skuDTOList SKU 列表
     * @param spuId      SPU 主键
     * @param spuCode    SPU 编码
     */
    private void bindSku(List<SkuDTO> skuDTOList, Long spuId, String spuCode) {
        for (int i = 0; i < skuDTOList.size(); i++) {
            SkuDTO skuDTO = skuDTOList.get(i);
            skuDTO.setSpuId(spuId);
            skuDTO.setCode(BusinessCodeUtil.generateArgs(BusinessType.SKU, spuCode, i + 1));
        }
    }

    /**
     * 设置SPU状态
     */
    private void setSpuState(SpuDTO spu, SpuDTO command, boolean isPreSave) {
        if (isPreSave) {
            spu.setState(SpuEnum.State.INIT);
        } else {
            SpuEnum.ChannelType channelType = command.getChannelType();
            if (SpuEnum.ChannelType.SELECTION == channelType) {
                spu.setState(SpuEnum.State.STORE);
            } else if (SpuEnum.ChannelType.OUT == channelType) {
                spu.setState(SpuEnum.State.DOWN);
            }
        }
    }

    /**
     * 设置规格类型
     */
    private void setSpecType(SpuDTO spu, SpuDTO command) {
        spu.setSpecType(command.getSkuList().size() > 1
                ? SpuEnum.SpecType.MULTIPLE.name()
                : SpuEnum.SpecType.SINGLE.name());
    }

    /**
     * 根据SKU列表计算SPU统计信息
     * 使用单次遍历收集所有统计数据，提升性能
     */
    private void calculateSpuStatistics(SpuDTO spu, List<SkuDTO> skuDTOList) {
        if (ObjectUtil.isEmpty(skuDTOList)) {
            return;
        }

        // 初始化统计变量
        Money minMarketPrice = null;
        Money maxMarketPrice = null;
        Money minSupplyPrice = null;
        Money maxSupplyPrice = null;
        Money minSalePrice = null;
        Money maxSalePrice = null;
        Money minUnitPrice = null;
        Money maxProfit = null;

        // 单次遍历收集所有统计数据
        for (SkuDTO skuDTO : skuDTOList) {
            // 市场价统计
            if (isPresent(skuDTO.getMarketPrice())) {
                minMarketPrice = minMoney(minMarketPrice, skuDTO.getMarketPrice());
                maxMarketPrice = maxMoney(maxMarketPrice, skuDTO.getMarketPrice());
            }

            // 供应价统计
            if (isPresent(skuDTO.getSupplyPrice())) {
                minSupplyPrice = minMoney(minSupplyPrice, skuDTO.getSupplyPrice());
                maxSupplyPrice = maxMoney(maxSupplyPrice, skuDTO.getSupplyPrice());
            }

            // 销售价统计
            if (isPresent(skuDTO.getSalePrice())) {
                minSalePrice = minMoney(minSalePrice, skuDTO.getSalePrice());
                maxSalePrice = maxMoney(maxSalePrice, skuDTO.getSalePrice());
            }

            // 单价统计
            if (isPresent(skuDTO.getUnitPrice())) {
                minUnitPrice = minMoney(minUnitPrice, skuDTO.getUnitPrice());
            }

            // 计算最大利润（市场价 - 销售价）
            if (isPresent(skuDTO.getMarketPrice()) && isPresent(skuDTO.getSalePrice())) {
                Money profit = skuDTO.getMarketPrice().subtract(skuDTO.getSalePrice());
                maxProfit = maxMoney(maxProfit, profit);
            }

        }

        // 设置SPU统计信息
        spu.setMarketPrice(minMarketPrice);
        spu.getExpand().setMarketPriceBegan(minMarketPrice);
        spu.getExpand().setMarketPriceEnd(maxMarketPrice);

        spu.setSupplyPrice(minSupplyPrice);
        spu.getExpand().setSupplierPriceBegan(minSupplyPrice);
        spu.getExpand().setSupplierPriceEnd(maxSupplyPrice);

        spu.setSalePrice(minSalePrice);
        spu.getExpand().setSalePriceBegan(minSalePrice);
        spu.getExpand().setSalePriceEnd(maxSalePrice);

        spu.setUnitPrice(minUnitPrice);
        spu.setMaxProfit(maxProfit);
    }

    /**
     * 构建SPU属性列表
     */
    private List<SpuAttributeDTO> buildSpuAttributeList(SpuDTO spuDTO, Long spuId) {
        List<SpuAttributeDTO> spuAttributeDTOList = new ArrayList<>();

        // 销售属性
        if (ObjectUtil.isNotEmpty(spuDTO.getSpuSaleAttributeList())) {
            List<SpuAttributeDTO> saleAttributeList = TransferUtils.transfers(
                    spuDTO.getSpuSaleAttributeList(), SpuAttributeDTO::new, (c, v) -> {
                        v.setId(SnowflakeGenerator.getSnowflakeId());
                        v.setSpuId(spuId);
                        v.setType(SpuEnum.SpuAttributeType.SALE);
                    });
            spuAttributeDTOList.addAll(saleAttributeList);
        }

        // 参数属性
        if (ObjectUtil.isNotEmpty(spuDTO.getSpuParamAttributeList())) {
            List<SpuAttributeDTO> paramAttributeList = TransferUtils.transfers(
                    spuDTO.getSpuParamAttributeList(), SpuAttributeDTO::new, (c, v) -> {
                        v.setId(SnowflakeGenerator.getSnowflakeId());
                        v.setSpuId(spuId);
                        v.setType(SpuEnum.SpuAttributeType.PARAM);
                    });
            spuAttributeDTOList.addAll(paramAttributeList);
        }

        return spuAttributeDTOList;
    }

    /**
     * 处理SPU属性变更
     */
    private void handleSpuAttributeChange(SpuDTO spuDTO, SpuUpdateRes spuUpdateRes) {
        if (ObjectUtil.isEmpty(spuDTO.getSpuSaleAttributeList())) {
            return;
        }

        SpuAttributeQuery spuAttributeQuery = new SpuAttributeQuery();
        spuAttributeQuery.setSpuId(spuDTO.getId());
        spuAttributeQuery.setType(SpuEnum.SpuAttributeType.SALE);

        List<SpuAttributeVO> oldSpuSaleAttributeList = spuRepository.querySpuAttributeList(spuAttributeQuery);
        SpuAttributeDiffRes diffRes = getSpuAttributeDiff(oldSpuSaleAttributeList, spuDTO.getSpuSaleAttributeList());

        switch (diffRes.getUpdateType()) {
            case 1:
                // SPU销售属性未修改
                break;
            case 2:
                // SPU销售属性值已修改
                handleAttributeValueChange(spuDTO.getId(), diffRes, spuUpdateRes);
                break;
            case 3:
                // SPU销售属性已修改，需要覆盖更新
                handleAttributeOverwrite(spuDTO);
                break;
            default:
                break;
        }
    }
    /**
     * 比较旧spu销售属性和 新spu销售属性的差异
     * @param oldSpuSaleAttributeList
     * @param newSpuSaleAttributeList
     * @return
     */
    private SpuAttributeDiffRes getSpuAttributeDiff(List<SpuAttributeVO> oldSpuSaleAttributeList, List<SpuAttributeVO> newSpuSaleAttributeList) {
        SpuAttributeDiffRes spuAttributeDiffRes = new SpuAttributeDiffRes();
        Map<String, Set<String>> oldSpuSaleAttributeMap = new HashMap<>();
        for (SpuAttributeVO spuAttributeVO : oldSpuSaleAttributeList) {
            Set<String> spuAttributeVOValue = new HashSet<>(JSON.parseArray(spuAttributeVO.getValue(), String.class));
            oldSpuSaleAttributeMap.put(spuAttributeVO.getName(), spuAttributeVOValue);
        }
        int existNum = 0;
        int notExistNum = 0;
        Map<String, Set<String>> addSpuSaleAttributeValue = new HashMap<>();
        Map<String, Set<String>> deleteSpuSaleAttributeValue = new HashMap<>();
        for (SpuAttributeVO spuAttribute : newSpuSaleAttributeList) {
            Set<String> oldValue = oldSpuSaleAttributeMap.get(spuAttribute.getName());
            if(oldValue != null){
                Set<String> newValue = new HashSet<>(JSON.parseArray(spuAttribute.getValue(), String.class));
                HashSet<String> addValue = new HashSet<>(newValue);
                addValue.removeAll(oldValue);
                if(!addValue.isEmpty()){
                    addSpuSaleAttributeValue.put(spuAttribute.getName(), addValue);
                }
                HashSet<String> deleteValue = new HashSet<>(oldValue);
                deleteValue.removeAll(newValue);
                if(!deleteValue.isEmpty()){
                    deleteSpuSaleAttributeValue.put(spuAttribute.getName(), deleteValue);
                }
                existNum++;
            }else{
                notExistNum++;
            }
        }
        Integer updateType = null;
        if(oldSpuSaleAttributeMap.size() != existNum || notExistNum != 0){
            updateType = 3;
        }else {
            if(addSpuSaleAttributeValue.isEmpty() && deleteSpuSaleAttributeValue.isEmpty()){
                updateType = 1;
            }else{
                updateType = 2;
            }
        }
        spuAttributeDiffRes.setUpdateType(updateType);
        spuAttributeDiffRes.setAddSpuSaleAttributeValue(addSpuSaleAttributeValue);
        spuAttributeDiffRes.setDeleteSpuSaleAttributeValue(deleteSpuSaleAttributeValue);
        return spuAttributeDiffRes;
    }

    /**
     * 处理属性值变更
     */
    private void handleAttributeValueChange(Long spuId, SpuAttributeDiffRes diffRes, SpuUpdateRes spuUpdateRes) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setSpuId(spuId);
        List<SkuVO> skuList = spuRepository.skuVOList(skuQuery);

        List<Long> deleteSkuIdList = getDeleteSkuIdList(skuList, diffRes.getDeleteSpuSaleAttributeValue());
        if (ObjectUtil.isNotEmpty(deleteSkuIdList)) {
            spuUpdateRes.getDeleteSkuIdList().addAll(deleteSkuIdList);
            SkuQuery deleteQuery = new SkuQuery();
            deleteQuery.setIdList(deleteSkuIdList);
            spuRepository.skuDeleteByQuery(deleteQuery);
        }
    }

    /**
     * 处理属性覆盖更新
     */
    private void handleAttributeOverwrite(SpuDTO spuDTO) {
        // 检查SKU是否都未携带ID
        for (SkuDTO sku : spuDTO.getSkuList()) {
            if (sku.getId() != null && sku.getId() != 0) {
                throw new PlatformException(BaseErrorCode.PARAM, "覆盖更新sku不能携带ID");
            }
        }

        // 删除所有SKU
        SkuQuery deleteQuery = new SkuQuery();
        deleteQuery.setSpuId(spuDTO.getId());
        spuRepository.skuDeleteByQuery(deleteQuery);
    }

    /**
     * 根据sku列表, 待删除sp销售属性, 获取sku的ID
     *
     * <p>按 (属性名, 属性值) 逐对比对 SKU 的销售属性, 命中任一待删除项即整条 SKU 作废</p>
     *
     * @param skuList                     SKU 列表
     * @param deleteSpuSaleAttributeValue 待删除的销售属性: 属性名 → 属性值集合
     * @return 待删除的 SKU 主键列表
     */
    private List<Long> getDeleteSkuIdList(List<SkuVO> skuList, Map<String, Set<String>> deleteSpuSaleAttributeValue) {
        List<Long> deleteSkuIdList = new ArrayList<>();
        for (SkuVO skuVO : skuList) {
            if (CollUtil.isEmpty(skuVO.getSaleAttribute())) {
                continue;
            }
            boolean hit = skuVO.getSaleAttribute().stream().anyMatch(attribute -> {
                Set<String> deleteValues = deleteSpuSaleAttributeValue.get(attribute.getName());
                return deleteValues != null && deleteValues.contains(attribute.getValue());
            });
            if (hit) {
                deleteSkuIdList.add(skuVO.getId());
            }
        }
        return deleteSkuIdList;
    }

    /**
     * 更新参数属性
     */
    private void updateAttributes(SpuDTO spuDTO) {
        if (ObjectUtil.isEmpty(spuDTO.getSpuParamAttributeList())
                && ObjectUtil.isEmpty(spuDTO.getSpuSaleAttributeList())) {
            return;
        }
        SpuAttributeQuery spuAttributeQuery = new SpuAttributeQuery();
        spuAttributeQuery.setSpuId(spuDTO.getId());
        // 删除旧的参数属性
        spuRepository.attributeDeleteByQuery(spuAttributeQuery);

        // 插入新的参数属性
        List<SpuAttributeDTO> updateAttributeList = TransferUtils.transfers(
                spuDTO.getSpuParamAttributeList(), SpuAttributeDTO::new, (c, v) -> {
                    v.setType(SpuEnum.SpuAttributeType.PARAM);
                    v.setSpuId(spuDTO.getId());
                    v.setId(SnowflakeGenerator.getSnowflakeId());
                });

        // 插入新的销售属性
        updateAttributeList.addAll(TransferUtils.transfers(
                spuDTO.getSpuSaleAttributeList(), SpuAttributeDTO::new, (c, v) -> {
                    v.setType(SpuEnum.SpuAttributeType.SALE);
                    v.setSpuId(spuDTO.getId());
                    v.setId(SnowflakeGenerator.getSnowflakeId());
                }));
        spuRepository.spuAttributeListSave(updateAttributeList);
    }
    /**
     * 更新SKU列表
     *
     * <p>先查出该 SPU 现存 SKU, 前台回传的按 ID 覆盖、未回传的删除, 新增 SKU 的编码
     * 以现存最大序号为起点在内存中迭代生成</p>
     *
     * @param spuDTO       待更新 SPU (含前台 SKU 列表)
     * @param spuUpdateRes 更新结果, 回填新增/更新/删除的 SKU 主键
     */
    private void updateSkuList(SpuDTO spuDTO, SpuUpdateRes spuUpdateRes) {
        List<SkuDTO> skuDTOList = spuDTO.getSkuList();
        if (ObjectUtil.isEmpty(skuDTOList)) {
            return;
        }
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setSpuId(spuDTO.getId());
        List<SkuVO> existSkuList = spuRepository.skuVOList(skuQuery);

        int nextSeq = maxSkuCodeSeq(existSkuList) + 1;
        Set<Long> retainIdList = new HashSet<>();
        for (SkuDTO sku : skuDTOList) {
            sku.setSpuId(spuDTO.getId());
            // 设置默认SKU图片
            if (StrUtil.isEmpty(sku.getImg())) {
                sku.setImg(spuDTO.getImg());
            }
            if (sku.getId() == null || sku.getId() == 0) {
                // 新增SKU
                sku.setId(SnowflakeGenerator.getSnowflakeId());
                sku.setCode(BusinessCodeUtil.generateArgs(BusinessType.SKU, spuDTO.getCode(), nextSeq++));
                spuRepository.skuSave(TransferUtils.transfer(sku, SkuDTO::new));
                spuUpdateRes.getAddSkuIdList().add(sku.getId());
            } else {
                // 更新SKU: 编码由后端持有, 不接受入参覆盖
                sku.setCode(null);
                spuRepository.skuUpdate(TransferUtils.transfer(sku, SkuDTO::new));
                spuUpdateRes.getUpdateSkuIdList().add(sku.getId());
            }
            retainIdList.add(sku.getId());
        }

        // 前台未回传的现存SKU视为删除
        List<Long> deleteIdList = existSkuList.stream()
                .map(SkuVO::getId)
                .filter(id -> !retainIdList.contains(id))
                .collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(deleteIdList)) {
            SkuQuery deleteQuery = new SkuQuery();
            deleteQuery.setIdList(deleteIdList);
            spuRepository.skuDeleteByQuery(deleteQuery);
            spuUpdateRes.getDeleteSkuIdList().addAll(deleteIdList);
        }
    }

    /**
     * 取 SKU 编码中的最大序号后缀
     *
     * @param skuList 现存 SKU 列表
     * @return 最大序号, 无有效编码时返回 0
     */
    private int maxSkuCodeSeq(List<SkuVO> skuList) {
        int maxSeq = 0;
        for (SkuVO sku : skuList) {
            if (StrUtil.isEmpty(sku.getCode())) {
                continue;
            }
            maxSeq = Math.max(maxSeq, Convert.toInt(StrUtil.subAfter(sku.getCode(), "-", true), 0));
        }
        return maxSeq;
    }

    @Override
    public Long categorySave(SpuCategoryReq categoryReq) {
        //重复名称检查
        SpuCategoryQuery categoryQuery = new SpuCategoryQuery();
        categoryQuery.setPid(categoryReq.getPid());
        categoryQuery.setAccountId(categoryReq.getAccountId());
        categoryQuery.setName(categoryReq.getName());
        categoryQuery.setNotId(categoryReq.getId());

        Long count = spuCategoryRepository.categoryCount(categoryQuery);
        if (count > 0) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA);
        }

        if (categoryReq.getId() == null) {
            if (categoryReq.getAccountId() == null) {
                SpuCategoryQuery countQuery = new SpuCategoryQuery();
                countQuery.setAccountId(categoryReq.getAccountId());
                countQuery.setPid(categoryReq.getPid());
                countQuery.resetQueryList();
                List<Long> alreadyIdByPid = spuCategoryRepository.categoryPage(countQuery).getRecords()
                        .stream().map(SpuCategoryVO::getId).collect(Collectors.toList());
                if (alreadyIdByPid.size() > SysProperties.sameCategoryCount) {
                    throw new PlatformException(BaseErrorCode.PARAM, StrUtil.format("同级分类数量不能超过{},请删除一些", SysProperties.sameCategoryCount));
                }
                categoryReq.setId(BizUtil.nextCode(categoryReq.getPid(), alreadyIdByPid));
            }
            spuCategoryRepository.categorySave(categoryReq);
            return categoryReq.getId();
        } else {
            spuCategoryRepository.categoryEdit(categoryReq);
            return categoryReq.getId();
        }
    }

    @Override
    public void categoryDelete(List<Long> idList) {
        // 检查是否存在子分类，存在则告知用户先删除子分类
        SpuCategoryQuery childQuery = new SpuCategoryQuery();
        childQuery.setPidList(idList);
        Long childCount = spuCategoryRepository.categoryCount(childQuery);
        if (childCount > 0) {
            throw new PlatformException(BaseErrorCode.PARAM, "存在子分类，请先删除子分类");
        }
        spuCategoryRepository.categoryDelete(idList);
    }

    @Override
    public List<SpuCategoryVO> categoryList(SpuCategoryQuery categoryQuery) {
        categoryQuery.resetQueryList();
        return spuCategoryRepository.categoryPage(categoryQuery).getRecords();
    }

    @Override
    public SpuCategoryVO category(Long id) {
        return spuCategoryRepository.category(id);
    }

    @Override
    public List<SpuCategoryVO> categoryTree(SpuCategoryQuery categoryQuery) {
        return BizUtil.listToTree(categoryList(categoryQuery));
    }

}
