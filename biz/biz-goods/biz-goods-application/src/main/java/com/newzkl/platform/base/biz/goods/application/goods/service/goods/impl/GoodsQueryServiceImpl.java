package com.newzkl.platform.base.biz.goods.application.goods.service.goods.impl;

import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.domain.video.service.ShortVideoDomain;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuAttributeQuery;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 商品查询应用服务实现
 *
 * <p>SPU 主体 / SKU / 属性组装全部走 {@code SpuDomain}, 商品视频走 {@code ShortVideoDomain},
 * 本层只做编排与视图拼装。</p>
 *
 * <p><b>缺口清单 (TODO[capability-gap])</b> —— 仅影响 {@code needExtraInfo = true} 的附加字段,
 * 不影响 SPU 主体查询, 故不抛异常:</p>
 * <ul>
 *   <li><b>行业名称</b> ({@code industryName}) —— 需 user 域 {@code supplierFacade#getSupplierVO}
 *       取供应商所属行业 ID 串再查行业名, Base 无对等 port, 当前置 "暂无"</li>
 *   <li><b>是否已铺货</b> ({@code choose}) —— 需 market 域
 *       {@code distributionRpcFacade#checkGoodsIsDistributed}, Base 无对等 port, 保留默认 false</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsQueryServiceImpl implements GoodsQueryService {

    /**
     * 行业名称缺省值 (行业查询能力缺失时占位)
     */
    private static final String INDUSTRY_NAME_NONE = "暂无";

    private final SpuDomain spuDomain;
    private final ShortVideoDomain shortVideoDomain;

    /**
     * 查询 spu 详情
     *
     * <p>组装顺序: SPU 主体 → SKU → 销售属性 → 参数属性 → 商品视频</p>
     *
     * @param spuId         spu 主键
     * @param needExtraInfo 是否需要额外信息 (行业名称 / 铺货标记, 当前为能力缺口)
     * @return spu 视图对象
     */
    @Override
    public SpuVO spuVO(Long spuId, Boolean needExtraInfo) {
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setId(spuId);
        SpuVO spuVO = spuDomain.voByQuery(spuQuery);
        if (spuVO == null) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "记录不存在:" + spuId);
        }

        // 组装 sku
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setSpuId(spuId);
        spuVO.setSkuList(spuDomain.skuVOList(skuQuery));

        // 组装销售属性 / 参数属性
        spuVO.setSpuSaleAttributeList(listSpuAttribute(spuId, SpuEnum.SpuAttributeType.SALE));
        spuVO.setSpuParamAttributeList(listSpuAttribute(spuId, SpuEnum.SpuAttributeType.PARAM));

        if (Boolean.TRUE.equals(needExtraInfo)) {
            spuVO.setIndustryName(INDUSTRY_NAME_NONE);
            log.warn("TODO[capability-gap]: spu 附加信息缺行业名称与铺货标记 "
                    + "(需 user 域 supplierFacade / market 域 distributionRpcFacade), spuId={}", spuId);
        }

        // 填充商品视频
        spuVO.setVideoList(listVideo(spuId));
        return spuVO;
    }

    /**
     * 查询 spu 属性列表
     *
     * @param spuId spu 主键
     * @param type  属性类型 (0 销售属性, 1 参数属性)
     * @return 属性列表
     */
    private List<SpuAttributeVO> listSpuAttribute(Long spuId, SpuEnum.SpuAttributeType type) {
        SpuAttributeQuery spuAttributeQuery = new SpuAttributeQuery();
        spuAttributeQuery.setSpuId(spuId);
        spuAttributeQuery.setType(type);
        spuAttributeQuery.resetQueryList();
        return spuDomain.querySpuAttributeList(spuAttributeQuery);
    }

    /**
     * 查询 spu 关联视频列表
     *
     * @param spuId spu 主键
     * @return 视频列表
     */
    private List<SpuVO.VideoVO> listVideo(Long spuId) {
        ShortVideoQuery shortVideoQuery = new ShortVideoQuery();
        shortVideoQuery.setSpuIdList(Collections.singletonList(spuId));
        shortVideoQuery.resetQueryList();
        return TransferUtils.transfers(shortVideoDomain.page(shortVideoQuery).getRecords(), SpuVO.VideoVO::new);
    }
}
