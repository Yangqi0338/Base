package com.newzkl.platform.base.biz.goods.application.goods.service.spu.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuService;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.exception.goods.SpuErrorCode;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SupplierSpuStatisticsVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuSaleAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuStateVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuSaleAttributeVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuAttributeVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuDetailVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.ddd.facade.SupplierSpuStatisticsQuery;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品应用服务实现
 *
 * <p>职责为编排, 落库与状态流转委托 {@code SpuDomain}, 查询走 {@code SpuRepository} 端口。</p>
 *
 * <p><b>缺口清单 (TODO[capability-gap])</b></p>
 * <ul>
 *   <li>{@code supplierSpuStatistics} —— <b>抛异常</b>。需 user 域 {@code supplierFacade#getSupplierVO}
 *       填充供应商主体信息; 且源端点已标注 {@code @Deprecated}</li>
 *   <li>{@code validateSupplierSpu} —— <b>抛异常</b>。new-scm 全仓无对等方法 (Base 新造接口),
 *       语义推测对应源 {@code SpuController#spuCreate} 的供应商校验 (供货价非空 + 退货地址),
 *       后者需 user 域 {@code supplierFacade#supplierRefundVO}</li>
 *   <li>{@code spuUp} —— 平台上下架已实现, 但源在其后<b>通知供应商</b>
 *       ({@code supplierFacade#upDownEvent}) 的跨域副作用缺失, 不影响主流程</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpuServiceImpl implements SpuService {

    /**
     * 缺口说明统一前缀
     */
    private static final String GAP = "TODO[capability-gap]: ";

    private final SpuDomain spuDomain;
    private final SpuRepository spuRepository;
    private final GoodsQueryService goodsQueryService;

    /**
     * 商品上下架 (平台侧)
     *
     * @param enable    1 上架, 0 下架
     * @param spuIdList 商品主键列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spuUp(Integer enable, List<Long> spuIdList) {
        spuDomain.platformSpuUp(enable, spuIdList);
        log.warn(GAP + "平台上下架后未通知供应商, 缺 user 域 supplierFacade#upDownEvent, spuIdList={}", spuIdList);
    }

    /**
     * 外部供应链商品同步
     *
     * @param spuDTO 商品操作对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outGoodsSync(SpuDTO spuDTO) {
        spuDomain.spuCreate(spuDTO);
    }

    /**
     * 移动 APP 供应商商品统计
     *
     * @param query 统计查询
     * @return 供应商商品统计
     * @throws UnsupportedOperationException 缺供应商主体查询端口
     */
    @Override
    public SupplierSpuStatisticsVO supplierSpuStatistics(SupplierSpuStatisticsQuery query) {
        throw new UnsupportedOperationException(GAP
                + "供应商商品统计需 user 域 supplierFacade#getSupplierVO 填充供应商主体信息, 且源端点已 @Deprecated");
    }

    /**
     * 货盘选择商品
     *
     * <p>按外部商品 ID 查重, 未重复则落为商品草稿 (归属平台)。</p>
     *
     * @param spuVO 商品视图对象
     * @return 商品主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long palletSelectGoods(SpuVO spuVO) {
        Long existsSpuId = spuRepository.spuId(
                spuVO.getChannelType() == null ? null : spuVO.getChannelType().getCode(),
                spuVO.getOutSpuId());
        if (existsSpuId != null) {
            throw new PlatformException(SpuErrorCode.EXISTS);
        }
        SpuDTO spuDTO = TransferUtils.transfer(spuVO, SpuDTO::new);
        spuDTO.setIdentity(AccountEnum.Identity.PLATFORM);
        return spuDomain.spuPreSave(spuDTO);
    }

    /**
     * 验证供应商商品
     *
     * @param spuDTO    商品操作对象
     * @param accountId 供应商账号主键
     * @throws UnsupportedOperationException 无对等源实现且缺供应商退货地址查询端口
     */
    @Override
    public void validateSupplierSpu(SpuDTO spuDTO, Long accountId) {
        throw new UnsupportedOperationException(GAP
                + "new-scm 无对等方法; 推测对应 SpuController#spuCreate 的供应商校验 (供货价非空 + 退货地址), "
                + "后者需 user 域 supplierFacade#supplierRefundVO");
    }

    /**
     * API-SPU 售卖状态列表
     *
     * @param accountId 调用方账号主键
     * @param spuIdList 商品主键列表
     * @return SPU 售卖状态列表
     */
    @Override
    public List<ApiSpuStateVO> apiSpuState(Long accountId, List<Long> spuIdList) {
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setIdList(spuIdList);
        List<SpuStateVO> spuStateVOList = spuRepository.spuStateList(spuQuery);
        return TransferUtils.transfers(spuStateVOList, (SpuStateVO spuStateVO) -> {
            ApiSpuStateVO apiSpuStateVO = new ApiSpuStateVO();
            apiSpuStateVO.setSpuId(spuStateVO.getId());
            apiSpuStateVO.setSaleState(toSaleState(spuStateVO.getState()));
            return apiSpuStateVO;
        });
    }

    /**
     * API-SPU 列表
     *
     * @param accountId 调用方账号主键
     * @param spuIdList 商品主键列表
     * @return SPU 列表
     */
    @Override
    public List<ApiSpuVO> apiSpuVOList(Long accountId, List<Long> spuIdList) {
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setIdList(spuIdList);
        List<SpuVO> spuVOList = spuDomain.listSelect(spuQuery);
        return TransferUtils.transfers(spuVOList, this::toApiSpuVO);
    }

    /**
     * API-SKU 列表
     *
     * @param accountId 调用方账号主键
     * @param spuIdList 商品主键列表
     * @return SKU 列表
     */
    @Override
    public List<ApiSkuVO> apiSkuVOList(Long accountId, List<Long> spuIdList) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setSpuIdList(spuIdList);
        List<SkuVO> skuVOList = spuDomain.skuVOList(skuQuery);
        return TransferUtils.transfers(skuVOList, this::toApiSkuVO);
    }

    /**
     * API-SPU 详情
     *
     * @param accountId 调用方账号主键
     * @param spuId     商品主键
     * @return SPU 详情 (含 SKU 列表)
     */
    @Override
    public ApiSpuDetailVO apiSpuDetail(Long accountId, Long spuId) {
        SpuVO spuVO = goodsQueryService.spuVO(spuId, false);
        ApiSpuDetailVO apiSpuDetailVO = new ApiSpuDetailVO();
        apiSpuDetailVO.setSpu(toApiSpuVO(spuVO));
        apiSpuDetailVO.setSkuList(TransferUtils.transfers(spuVO.getSkuList(), this::toApiSkuVO));
        return apiSpuDetailVO;
    }

    /**
     * SPU 状态映射为对外售卖状态
     *
     * @param state SPU 状态码
     * @return 1 上架, 0 下架
     */
    private Integer toSaleState(Integer state) {
        return SpuEnum.State.SALE.getCode().equals(state)
                ? CommonEnum.YesOrNo.YES.getCode()
                : CommonEnum.YesOrNo.NO.getCode();
    }

    /**
     * SPU 视图对象转对外 API 视图对象
     *
     * @param spuVO SPU 视图对象
     * @return 对外 SPU 视图对象
     */
    private ApiSpuVO toApiSpuVO(SpuVO spuVO) {
        // 对外 RPC 契约保持分 Integer, Money 字段显式降为分 (Hutool 不做 Money↔Integer 转换)
        ApiSpuVO apiSpuVO = TransferUtils.transfer(spuVO, ApiSpuVO::new, (s, v) -> {
            v.setUnitPrice(toCent(s.getUnitPrice()));
            v.setSupplierPriceBegan(toCent(s.getExpand().getSupplierPriceBegan()));
        }, ignoreMoney("unitPrice"));
        apiSpuVO.setSaleAttributeList(toApiAttributeList(spuVO.getSpuSaleAttributeList()));
        apiSpuVO.setParamAttributeList(toApiAttributeList(spuVO.getSpuParamAttributeList()));
        apiSpuVO.setSaleState(toSaleState(spuVO.getState()));
        return apiSpuVO;
    }

    /**
     * SPU 属性列表转对外属性列表
     *
     * @param attributeList SPU 属性列表
     * @return 对外属性列表
     */
    private List<ApiSpuAttributeVO> toApiAttributeList(List<SpuAttributeVO> attributeList) {
        if (CollUtil.isEmpty(attributeList)) {
            return null;
        }
        return TransferUtils.transfers(attributeList, ApiSpuAttributeVO.class);
    }

    /**
     * SKU 视图对象转对外 API 视图对象
     *
     * @param skuVO SKU 视图对象
     * @return 对外 SKU 视图对象
     */
    private ApiSkuVO toApiSkuVO(SkuVO skuVO) {
        // 对外 RPC 契约保持分 Integer, Money 字段显式降为分 (Hutool 不做 Money↔Integer 转换)
        ApiSkuVO apiSkuVO = TransferUtils.transfer(skuVO, ApiSkuVO::new, (s, v) -> {
            v.setMarketPrice(toCent(s.getMarketPrice()));
            v.setSalePrice(toCent(s.getSalePrice()));
            v.setSupplyPrice(toCent(s.getSupplyPrice()));
            v.setUnitPrice(toCent(s.getUnitPrice()));
        }, ignoreMoney("marketPrice", "salePrice", "supplyPrice", "unitPrice"));
        List<SkuSaleAttributeVO> saleAttribute = skuVO.getSaleAttribute();
        apiSkuVO.setSaleAttribute(CollUtil.isEmpty(saleAttribute)
                ? null
                : TransferUtils.transfers(saleAttribute, ApiSkuSaleAttributeVO.class));
        return apiSkuVO;
    }

    /**
     * Money 显式降为分 (对外 RPC 契约边界), null 或 Money NULL 语义返回 null
     *
     * @param money 金额值对象
     * @return 分, 无值返回 null
     */
    private static Integer toCent(Money money) {
        return (money == null || money.isNull()) ? null : (int) money.getCent();
    }

    /**
     * 构建排除 Money 属性的拷贝选项 (交由自定义器显式转分)
     *
     * @param props 需排除的 Money 属性名
     * @return 拷贝选项
     */
    private static CopyOptions ignoreMoney(String... props) {
        return CopyOptions.create().setIgnoreProperties(props);
    }
}
