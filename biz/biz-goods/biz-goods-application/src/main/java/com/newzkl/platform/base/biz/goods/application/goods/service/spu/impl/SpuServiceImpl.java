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
import com.newzkl.platform.base.common.ddd.model.constant.SpuErrorCode;
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

    private final SpuDomain spuDomain;
    private final SpuRepository spuRepository;
    private final GoodsQueryService goodsQueryService;

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
                spuVO.getChannelType(),
                spuVO.getOutSpuId());
        if (existsSpuId != null) {
            throw new PlatformException(SpuErrorCode.EXISTS);
        }
        SpuDTO spuDTO = TransferUtils.transfer(spuVO, SpuDTO::new);
        spuDTO.setIdentity(AccountEnum.Identity.PLATFORM);
        return spuDomain.spuPreSave(spuDTO);
    }

    @Override
    public SpuVO spuVO(Long spuId, boolean needExtraInfo) {
        return goodsQueryService.spuVO(spuId, needExtraInfo);
    }
}
