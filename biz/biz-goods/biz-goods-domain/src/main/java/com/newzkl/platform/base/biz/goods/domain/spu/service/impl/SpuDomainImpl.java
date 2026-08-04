package com.newzkl.platform.base.biz.goods.domain.spu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.SpuUpdateRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuSaleAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.count.GoodsCountVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SpuQuery;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import com.newzkl.platform.base.biz.goods.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.goods.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.goods.model.exception.goods.SpuErrorCode;
import com.newzkl.platform.base.common.core.utils.properties.SysProperties;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long spuPreSave(SpuDTO spuDTO) {
        // 构建并保存SKU
        List<SkuDTO> skuDTOList = buildSkuList(spuDTO);

        // 构建并保存SPU（包含统计信息）
        SpuDTO spu = buildSpu(spuDTO, true, skuDTOList);

        // 持久化
        Long spuId = spuRepository.spuSave(spu);

        // 设置SKU的spuId
        skuDTOList.forEach(skuDTO -> skuDTO.setSpuId(spuId));

        // 构建并保存属性
        List<SpuAttributeDTO> spuAttributeDTOList = buildSpuAttributeList(spuDTO, spuId);

        if (ObjectUtil.isNotEmpty(spuAttributeDTOList)) {
            spuRepository.spuAttributeListSave(spuAttributeDTOList);
        }
        if (ObjectUtil.isNotEmpty(skuDTOList)) {
            spuRepository.skuListSave(skuDTOList);
        }

        //刷新SPU
        refreshSpu(spuId);
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

        // 设置SKU的spuId
        skuDTOList.forEach(skuDTO -> skuDTO.setSpuId(spuId));

        // 构建并保存属性
        List<SpuAttributeDTO> spuAttributeDTOList = buildSpuAttributeList(spuDTO, spuId);

        if (ObjectUtil.isNotEmpty(spuAttributeDTOList)) {
            spuRepository.spuAttributeListSave(spuAttributeDTOList);
        }
        if (ObjectUtil.isNotEmpty(skuDTOList)) {
            spuRepository.skuListSave(skuDTOList);
        }

        //刷新SPU
        refreshSpu(spuId);
        return spu.getId();
    }

    /**
     * 刷新 SPU 冗余的 SKU 聚合统计列
     *
     * <p>查该 SPU 下 SKU 列表, 聚合价格区间/库存/最大利润后按 ID 更新。
     * 只写 SKU 聚合列, 不触碰 minPricingNum 等与 SKU 聚合无关的列。</p>
     *
     * @param spuId SPU 主键
     */
    private void refreshSpu(Long spuId) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setSpuId(spuId);
        List<SkuVO> skuList = spuRepository.skuVOList(skuQuery);
        if (CollUtil.isEmpty(skuList)) {
            return;
        }
        Money minMarket = null;
        Money maxMarket = null;
        Money minSupply = null;
        Money maxSupply = null;
        Money minSale = null;
        Money maxSale = null;
        Money minUnit = null;
        Money maxProfit = null;
        for (SkuVO sku : skuList) {
            if (isPresent(sku.getMarketPrice())) {
                minMarket = minMoney(minMarket, sku.getMarketPrice());
                maxMarket = maxMoney(maxMarket, sku.getMarketPrice());
            }
            if (isPresent(sku.getSupplyPrice())) {
                minSupply = minMoney(minSupply, sku.getSupplyPrice());
                maxSupply = maxMoney(maxSupply, sku.getSupplyPrice());
            }
            if (isPresent(sku.getSalePrice())) {
                minSale = minMoney(minSale, sku.getSalePrice());
                maxSale = maxMoney(maxSale, sku.getSalePrice());
            }
            if (isPresent(sku.getUnitPrice())) {
                minUnit = minMoney(minUnit, sku.getUnitPrice());
            }
            if (isPresent(sku.getMarketPrice()) && isPresent(sku.getSalePrice())) {
                Money profit = sku.getMarketPrice().subtract(sku.getSalePrice());
                maxProfit = maxMoney(maxProfit, profit);
            }
        }
        SpuDTO spu = new SpuDTO();
        spu.setId(spuId);
        spu.setMarketPrice(minMarket);
        spu.setMarketPriceBegan(minMarket);
        spu.setMarketPriceEnd(maxMarket);
        spu.setSupplyPrice(minSupply);
        spu.setSupplierPriceBegan(minSupply);
        spu.setSupplierPriceEnd(maxSupply);
        spu.setSalePrice(minSale);
        spu.setSalePriceBegan(minSale);
        spu.setSalePriceEnd(maxSale);
        spu.setUnitPrice(minUnit);
        spu.setMaxProfit(maxProfit);
        spuRepository.spuUpdate(spu);
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
        Long spuId = spuDTO.getId();
        SpuDTO spu = spuRepository.getById(spuDTO.getId());
        if (spu == null || !SpuEnum.State.STORE.getCode().equals(spu.getState())) {
            throw new PlatformException(SpuErrorCode.NOT_EXIST_OR_STATE_ERROR);
        }

        // 删除sku
        spuRepository.skuDeleteBySpuId(spuId);

        // 删除spu
        spuRepository.spuDelete(spuId);

        // 删除spuAttribute
        spuRepository.spuAttributeDeleteBySpuId(spuId);

        // 新增
        spuDTO.setRefresh(false);
        spuRepository.spuSave(TransferUtils.transfer(spuDTO, SpuDTO::new));
    }

    @Override
    public SpuDTO getById(Long id) {
        return spuRepository.getById(id);
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
        updateSkuList(spuDTO.getSkuList(), spuUpdateRes);

        // 根据SKU列表计算SPU统计信息
        calculateSpuStatistics(spuDTO, spuDTO.getSkuList());

        // 更新SPU基本信息
        spuRepository.spuUpdate(spuDTO);
        return spuUpdateRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuAuditSuccess(SpuVO spuCommand, String skuSalePriceJson) {
        // 解析并更新SKU销售价
        List<SkuDTO> skuUpdateList = new ArrayList<>();
        List<Money> salePriceList = new ArrayList<>();
        List<Money> supplyPriceList = new ArrayList<>();
        
        parseAndCollectSkuPrices(spuCommand.getSkuList(), skuSalePriceJson, 
                skuUpdateList, salePriceList, supplyPriceList);
        
        // 构建SPU更新对象并设置价格区间
        SpuDTO spu = buildSpuForAuditSuccess(spuCommand.getId(),
                skuUpdateList, salePriceList, supplyPriceList);
        
        // 执行更新
        spuRepository.spuUpdate(spu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int spuUp(Integer enable, List<Long> spuIdList) {
        return updateSpuState(enable, spuIdList, 
                SpuEnum.State.SALE.getCode(), 
                SpuEnum.State.DOWN.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int platformSpuUp(Integer enable, List<Long> spuIdList) {
        return updateSpuState(enable, spuIdList, 
                SpuEnum.State.SALE.getCode(), 
                SpuEnum.State.PLATFORM_DOWN.getCode());
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
        spuUpdate.setAuditState(AuditEnum.State.FAIL.getCode());
        spuUpdate.setLastRefuseReason(lastRefuseReason);
        spuRepository.spuUpdate(spuUpdate);
    }

    @Override
    public void spuSubmit(Long spuId, Long flowId) {
        // 状态修改
        SpuDTO spuUpdate = new SpuDTO();
        spuUpdate.setId(spuId);
        spuUpdate.setAuditState(AuditEnum.State.AUDITING.getCode());
        spuUpdate.setFlowId(flowId);
        spuRepository.spuUpdate(spuUpdate);
    }

    @Override
    public void spuAuditStop(SpuVO spuVO) {
        // 状态修改
        SpuDTO spuUpdate = new SpuDTO();
        spuUpdate.setId(spuVO.getId());
        spuUpdate.setAuditState(AuditEnum.State.STOP.getCode());
        spuRepository.spuUpdate(spuUpdate);
    }

    @Override
    public void editColumn(Long id, List<EditColumnVO> editColumnDTOS) {
        spuRepository.editColumn(id, editColumnDTOS);
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

    /*@Override
    public List<OrderGoodsInfoVO> queryOrderGoodsInfoVOList(List<GoodsVO> goods, Long channelId, Long storeId) {
        return spuRepository.queryOrderGoodsInfoVOList(goods, channelId, storeId);
    }*/

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

    /*@Override
    public OrderGoodsInfoVO queryOrderSkuInfoVOList(Long skuId) {
        return spuRepository.queryOrderSkuInfoVOList(skuId);
    }*/

    @Override
    public Page<SpuVO> querySpuPage(SpuQuery spuQuery) {
        return spuRepository.querySpuPage(spuQuery);
    }

    @Override
    public Page<SkuVO> querySkuPage(SkuQuery skuQuery) {
        return spuRepository.querySkuPage(skuQuery);
    }

    @Override
    public SpuVO voByQuery(SpuQuery spuQuery) {
        return spuRepository.voByQuery(spuQuery);
    }

    @Override
    public List<SpuAttributeVO> querySpuAttributeList(SpuAttributeQuery spuAttributeQuery) {
        return spuRepository.querySpuAttributeList(spuAttributeQuery);
    }

    @Override
    public List<SpuVO> listSelect(SpuQuery spuQuery) {
        return spuRepository.listSelect(spuQuery);
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

    @Override
    public void spuSelectorNumAdd(List<Long> spuIdList, Integer num) {
        spuRepository.spuSelectorNumAdd(spuIdList,num);
    }

    /**
     * 解析并收集SKU价格信息
     */
    private void parseAndCollectSkuPrices(List<SkuVO> skuList, String skuSalePriceJson,
                                          List<SkuDTO> skuUpdateList,
                                         List<Money> salePriceList,
                                         List<Money> supplyPriceList) {
        if (CollUtil.isEmpty(skuList) || ObjectUtil.isEmpty(skuSalePriceJson)) {
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(skuSalePriceJson);
        for (SkuVO skuVO : skuList) {
            Object priceObj = jsonObject.get(skuVO.getId().toString());
            if (priceObj != null) {
                // JSON 内嵌金额为分 (Integer 语义), 显式 Money.of(分) 升为值对象
                Money salePrice = Money.of(Integer.valueOf(String.valueOf(priceObj)));
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
        spu.setState(SpuEnum.State.SALE.getCode());
        spu.setAuditState(AuditEnum.State.SUCCESS.getCode());
        spu.setSkuList(skuList);

        // 设置销售价格区间 (Money 无 Comparable, 遍历取极值)
        if (ObjectUtil.isNotEmpty(salePriceList)) {
            spu.setSalePriceBegan(minOfList(salePriceList));
            spu.setSalePriceEnd(maxOfList(salePriceList));
        }

        // 设置供货价格区间
        if (ObjectUtil.isNotEmpty(supplyPriceList)) {
            spu.setSupplierPriceBegan(minOfList(supplyPriceList));
            spu.setSupplierPriceEnd(maxOfList(supplyPriceList));
        }

        return spu;
    }

    /**
     * 取金额列表最小值
     *
     * @param list 金额列表, 非空
     * @return 最小金额
     */
    private Money minOfList(List<Money> list) {
        Money min = null;
        for (Money m : list) {
            min = minMoney(min, m);
        }
        return min;
    }

    /**
     * 取金额列表最大值
     *
     * @param list 金额列表, 非空
     * @return 最大金额
     */
    private Money maxOfList(List<Money> list) {
        Money max = null;
        for (Money m : list) {
            max = maxMoney(max, m);
        }
        return max;
    }

    /**
     * 更新SPU状态（上架/下架）
     */
    private int updateSpuState(Integer enable, List<Long> spuIdList, 
                              Integer onlineState, Integer offlineState) {
        Integer targetState = CommonEnum.YesOrNo.YES.getCode().equals(enable)
                ? onlineState : offlineState;
        
        int result = spuRepository.editStateById(targetState, spuIdList);
        
        // 执行上架/下架后置处理
        if (CommonEnum.YesOrNo.YES.getCode().equals(enable)) {
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
        spuUpdate.setCategoryName(outSpuEditCommand.getCategoryName());
        spuUpdate.setBrandId(outSpuEditCommand.getBrandId());
        spuUpdate.setBrandName(outSpuEditCommand.getBrandName());
        spuUpdate.setAuditState(AuditEnum.State.SUCCESS.getCode());
        spuUpdate.setState(SpuEnum.State.DOWN.getCode());
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
                v.setId(SnowflakeIdAble.getSnowflakeId());
            }

            // 设置审核状态
            v.setAuditState(AuditEnum.State.CUSTOM.getCode());

            // 设置商品状态
            setSpuState(v, c, isPreSave);

            // 设置规格类型
            setSpecType(v, c);

            // 设置分类ID默认值
            if (v.getCategoryId() == null) {
                v.setCategoryId(0L);
            }

            // 设置品牌名称 OPTIMIZE
//            if (v.getBrandId() != null) {
//                v.setBrandName(brandRepository.brandById(v.getBrandId()).getName());
//            }

            // 计算并填充SKU统计信息
            calculateSpuStatistics(v, skuDTOList);
        });
    }

    /**
     * 设置SPU状态
     */
    private void setSpuState(SpuDTO spu, SpuDTO command, boolean isPreSave) {
        if (isPreSave) {
            spu.setState(SpuEnum.State.INIT.getCode());
        } else {
            SpuEnum.ChannelType channelType = command.getChannelType();
            if (SpuEnum.ChannelType.CUSTOM == channelType ||
                    SpuEnum.ChannelType.SELECTION == channelType) {
                spu.setState(SpuEnum.State.STORE.getCode());
            } else if (SpuEnum.ChannelType.OUT == channelType) {
                spu.setState(SpuEnum.State.DOWN.getCode());
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
        Double minWeight = null;

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

            // 最小重量统计
            if (skuDTO.getWeight() != null) {
                minWeight = (minWeight == null) ? skuDTO.getWeight() : Math.min(minWeight, skuDTO.getWeight());
            }
        }

        // 设置SPU统计信息
        spu.setMarketPrice(minMarketPrice);
        spu.setMarketPriceBegan(minMarketPrice);
        spu.setMarketPriceEnd(maxMarketPrice);

        spu.setSupplyPrice(minSupplyPrice);
        spu.setSupplierPriceBegan(minSupplyPrice);
        spu.setSupplierPriceEnd(maxSupplyPrice);

        spu.setSalePrice(minSalePrice);
        spu.setSalePriceBegan(minSalePrice);
        spu.setSalePriceEnd(maxSalePrice);

        spu.setUnitPrice(minUnitPrice);
        spu.setMaxProfit(maxProfit);
        spu.setMinPricingNum(minWeight);
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
                        v.setId(SnowflakeIdAble.getSnowflakeId());
                        v.setSpuId(spuId);
                        v.setType(SpuEnum.SpuAttributeType.SALE.getCode());
                    });
            spuAttributeDTOList.addAll(saleAttributeList);
        }

        // 参数属性
        if (ObjectUtil.isNotEmpty(spuDTO.getSpuParamAttributeList())) {
            List<SpuAttributeDTO> paramAttributeList = TransferUtils.transfers(
                    spuDTO.getSpuParamAttributeList(), SpuAttributeDTO::new, (c, v) -> {
                        v.setId(SnowflakeIdAble.getSnowflakeId());
                        v.setSpuId(spuId);
                        v.setType(SpuEnum.SpuAttributeType.PARAM.getCode());
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
        spuAttributeQuery.setType(SpuEnum.SpuAttributeType.SALE.getCode());

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
     */
    private List<Long> getDeleteSkuIdList(List<SkuVO> skuList, Map<String, Set<String>> deleteSpuSaleAttributeValue) {
        List<Long> deleteSkuIdList = new ArrayList<>();
        Set<String> valueLike = new HashSet<>();
        for (String s : deleteSpuSaleAttributeValue.keySet()) {
            for (String ss : deleteSpuSaleAttributeValue.get(s)) {
                valueLike.add(s + "\",\"value\":\"" + ss);
            }
        }
        for (SkuVO skuDO : skuList) {
            for (String s : valueLike) {
                if(skuDO.getSaleAttributeJson().contains(s)){
                    deleteSkuIdList.add(skuDO.getId());
                }
            }
        }
        return deleteSkuIdList;
    }

    /**
     * 更新参数属性
     */
    private void updateAttributes(SpuDTO spuDTO) {
        if (ObjectUtil.isEmpty(spuDTO.getSpuParamAttributeList())) {
            return;
        }
        SpuAttributeQuery spuAttributeQuery = new SpuAttributeQuery();
        spuAttributeQuery.setSpuId(spuDTO.getId());
        // 删除旧的参数属性
        spuRepository.attributeDeleteByQuery(spuAttributeQuery);

        // 插入新的参数属性
        List<SpuAttributeDTO> updateAttributeList = TransferUtils.transfers(
                spuDTO.getSpuParamAttributeList(), SpuAttributeDTO::new, (c, v) -> {
                    v.setType(SpuEnum.SpuAttributeType.PARAM.getCode());
                    v.setSpuId(spuDTO.getId());
                    v.setId(SnowflakeIdAble.getSnowflakeId());
                });

        // 插入新的销售属性
        updateAttributeList.addAll(TransferUtils.transfers(
                spuDTO.getSpuSaleAttributeList(), SpuAttributeDTO::new, (c, v) -> {
                    v.setType(SpuEnum.SpuAttributeType.SALE.getCode());
                    v.setSpuId(spuDTO.getId());
                    v.setId(SnowflakeIdAble.getSnowflakeId());
                }));
        spuRepository.spuAttributeListSave(updateAttributeList);
    }
    /**
     * 更新SKU列表
     */
    private void updateSkuList(List<SkuDTO> skuVOList, SpuUpdateRes spuUpdateRes) {
        if (ObjectUtil.isEmpty(skuVOList)) {
            return;
        }

        for (SkuDTO sku : skuVOList) {
            if (sku.getId() == null || sku.getId() == 0) {
                // 新增SKU
                // 设置默认SKU图片
                if (StrUtil.isEmpty(sku.getImg())) {
                    String img = StrUtil.isNotEmpty(sku.getImg())
                            ? sku.getImg()
                            : spuRepository.getById(sku.getSpuId()).getImg();
                    sku.setImg(img);
                }
                sku.setId(SnowflakeIdAble.getSnowflakeId());
                spuRepository.skuSave(TransferUtils.transfer(sku, SkuDTO::new));
            } else {
                // 更新SKU
                spuRepository.skuUpdate(TransferUtils.transfer(sku, SkuDTO::new));
                spuUpdateRes.getUpdateSkuIdList().add(sku.getId());
            }
        }
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
