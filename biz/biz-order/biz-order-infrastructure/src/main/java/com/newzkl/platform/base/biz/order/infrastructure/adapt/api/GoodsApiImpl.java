package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.goods.facade.ISpuFacade;
import com.newzkl.platform.base.biz.goods.facade.model.SkuQuery;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsApi;
import com.newzkl.platform.base.common.ddd.facade.DistributionDetailVO;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;

import com.newzkl.platform.base.biz.order.model.support.api.order.*;
import com.newzkl.platform.base.common.ddd.facade.*;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.StoreDistributionDetailOutVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.extern.slf4j.Slf4j;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code OperatorApi} 的跨域实现
 *
 * <p>经 {@code OperatorFacade} 调 biz-account 的运营商能力。对等旧
 * {@code @DubboReference IOperatorFacade}: Base 当前为单体, facade 实现
 * ({@code OperatorFacadeProvider}) 与本类同上下文, 直接按接口注入即可;
 * 将来拆服务时改为远程 consumer, 本类与领域层零改动。</p>
 *
 * @author KC
 */
@Slf4j
@Component("orderGoodsApi")
public class GoodsApiImpl implements GoodsApi {

    @RpcReference
    private ISpuFacade spuFacade;

    @Override
    public List<ApiSkuVO> querySkuIdListByOutId(List<String> skuIdList) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setIdList(skuIdList.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList()));
        return TransferUtils.transfers(spuFacade.skuVOList(skuQuery), ApiSkuVO::new);
    }

    @Override
    public List<ApiSpuVO> apiSpuVOList(Long accountId, List<Long> spuIdList) {
        // provider 缺口: biz-goods ISpuFacade 仅迁 SKU 级 skuVOList, 未迁 SPU 级 apiSpuVOList
        // 见 rebuild/docs/planning/deferred-issues.md; biz-goods 补齐后接回真实调用
        throw new UnsupportedOperationException(
                "GoodsSpuApi.apiSpuVOList 待 biz-goods 域补齐 ISpuFacade SPU 级查询后实现");
    }

    @Override
    public List<DistributionDetailVO> queryDistributionDetailByIds(List<Long> distributionIds) {
        return List.of();
    }

    @Override
    public StoreDistributionDetailOutVO selectBySkuId(Long channelId, Long storeId, Long skuId) {
        return null;
    }

    @Override
    public PlatformResult<OrderGoodsCheckRes> orderCheck(OrderGoodsCheckReq checkReq, List<GoodsVO> goodsList) {
//        Map<Long, Integer> skuNum = goods.stream().collect(Collectors.toMap(GoodsVO::getSkuId, GoodsVO::getNum));
//        List<OrderGoodsInfoVO> orderGoodsInfos = new ArrayList<>();
//        // 1、查询选品或铺货
//        if(goods.size() == 1){
//            addOrderGoodsInfoVO(orderGoodsInfos, goods.get(0), req.getChannelId(), req.getStoreId());
//        }else if(goods.size() == 2){
//            addOrderGoodsInfoVO(orderGoodsInfos, goods.get(0), req.getChannelId(), req.getStoreId());
//            addOrderGoodsInfoVO(orderGoodsInfos, goods.get(1), req.getChannelId(), req.getStoreId());
//        }else {
//            orderGoodsInfos = spuDomain.queryOrderGoodsInfoVOList(goods, req.getChannelId(), req.getStoreId());
//        }
//        // 2、校验选品或铺货
//        if(goods.size() != orderGoodsInfos.size()){
//            ThrowsException.exception(BaseErrorCode.PARAM, "SKU异常");
//        }
//        // 3、是否有未上架的商品
//        Optional<OrderGoodsInfoVO> first = orderGoodsInfos.stream().filter(x -> x.getSpuState() != 2).findFirst();
//        first.ifPresent(orderGoodsInfoVO -> ThrowsException.exception(BaseErrorCode.PARAM.getCode(), "商品已下架,spuId为：" + orderGoodsInfoVO.getSpuId()));
//        // 设置购买数量并区分本地和外部商品
//        List<OrderSkuVO> localGoods = new ArrayList<>();
//        List<OrderSkuVO> outGoods = new ArrayList<>();
//        Map<Long, OrderGoodsInfoVO> skuOrderGoodsMap = new HashMap<>();
//        orderGoodsInfos.forEach(x->{
//            x.setNum(skuNum.get(x.getSkuId()));
//            // 区分外部订单商品和本地商品(非本地商品)
//            if (x.getSupplierId() <= 10){
//                outGoods.add(new OrderSkuVO(x.getSpuId(),x.getOutSpuId(),x.getSkuId(),x.getOutSkuId(),x.getNum(),x.getSupplierId()));
//            }else {
//                localGoods.add(new OrderSkuVO(x.getSpuId(),null,x.getSkuId(),null,x.getNum(),x.getSupplierId()));
//            }
//            //
//            skuOrderGoodsMap.put(x.getSkuId(), x);
//        });
//        // 外部商品校验销售范围
//        // todo 待完善 需要下单前检验
////        if (outGoods.size() > 0){
////            List<CheckSkuItemReq> goodsList = new ArrayList<>();
////            for (OrderSkuVO obj : outGoods) {
////                goodsList.add(new CheckSkuItemReq(obj.getCount(),obj.getOutId()));
////                OrderGoodsInfoVO orderGoodsInfoVO = skuOrderGoodsMap.get(obj.getLocalId());
////                if(orderGoodsInfoVO != null
////                        && orderGoodsInfoVO.getBuyStartQty() != null
////                        && obj.getCount() < orderGoodsInfoVO.getBuyStartQty()){
////                    ThrowsException.exception(SpuErrorCode.BUY_START_QTY);
////                }
////            }
////            YytApiUtil.checkSku(new CheckSkuReq(goodsList,String.valueOf(req.getShipAreaCode())));
////        }
//        // 4、计算运费
//        // 定义商品运费集合
//        Map<Long,Integer> goodsFreight = new HashMap<>(8);
//        // 商品维度累计数量
//        // 商品购买数量
//        //Map<Long,Integer> goodsNum = orderGoodsInfos.stream().collect(Collectors.groupingBy(OrderGoodsInfoVO::getSpuId,Collectors.summingInt(OrderGoodsInfoVO::getNum)));
//        // 商品运费模板
//        Map<Long,Long> goodsTemplate = orderGoodsInfos.stream().collect(Collectors.toMap(OrderGoodsInfoVO::getSpuId,OrderGoodsInfoVO::getFreightTemplateId,(k, v) -> k));
//        // 商品重量
//        //Map<Long, Double> goodsWeight = orderGoodsInfos.stream().collect(Collectors.groupingBy(OrderGoodsInfoVO::getSpuId,Collectors.summingDouble(OrderGoodsInfoVO::getWeight)));
//        // 按商品统计运费相关数据
//        // 将sku数据按spu分组
//        Map<Long, GoodsFreightDataVO> goodsFreightDataMap =
//                orderGoodsInfos.parallelStream().collect(Collectors.groupingBy(OrderGoodsInfoVO::getSpuId
//                        , Collectors.collectingAndThen(Collectors.toList(), m -> {
//                            OrderGoodsInfoVO orderGoodsInfoVO = m.parallelStream().findFirst().get();
//                            BigDecimal goodsWeight = m.parallelStream().map(OrderGoodsInfoVO::getWeight).reduce(BigDecimal.ZERO,BigDecimal::add);
//                            BigDecimal goodsVolume = m.parallelStream().map(OrderGoodsInfoVO::getVolume).reduce(BigDecimal.ZERO,BigDecimal::add);
//                            Integer num = m.parallelStream().mapToInt(OrderGoodsInfoVO::getNum).sum();
//                            JSONObject object = JSONObject.parseObject(orderGoodsInfoVO.getExpand());
//                            String channelType = "";
//                            String itemCode = "";
//                            if (Objects.nonNull(object)){
//                                channelType = object.getString("channelType");
//                                itemCode = object.getString("itemCode");
//                            }
//
//                            return new GoodsFreightDataVO(num, goodsWeight, goodsVolume,orderGoodsInfoVO.getOutSpuId(),orderGoodsInfoVO.getOutSkuId(),channelType,itemCode,orderGoodsInfoVO.getSupplierId());
//                        })));
//        // 商品体积
//        //Map<Long,Double> goodsVolume = orderGoodsInfos.stream().collect(Collectors.groupingBy(OrderGoodsInfoVO::getSpuId,Collectors.summingDouble(OrderGoodsInfoVO::getVolume)));
//        // 遍历计算各商品运费
//        goodsFreightDataMap.forEach( (k,v)->{
//            Long templateId = goodsTemplate.get(k);
//            if (templateId == 1){
//                // 调用第三方供应链查询运费
//                List<FreightItemReq> spuInfoList = new ArrayList<>();
//                spuInfoList.add(new FreightItemReq(v.getNum(),v.getOutSpuId()));
//                FreightReq freightReq = new FreightReq(spuInfoList,String.valueOf(req.getShipCityCode()));
//                goodsFreight.put(k, YytApiUtil.freight(freightReq));
//                return;
//            }
//            if (templateId == 2) {// 会订货
//                HuiDingHuoGetExpressFeeReq huiDingHuoGetExpressFeeReq = new HuiDingHuoGetExpressFeeReq();
//                // todo 待完善
//                setAreaInfo(req.getShipArea(), huiDingHuoGetExpressFeeReq);
//
//                List<HuiDingHuoGetExpressFeeReq.SkuItem> skuList = new ArrayList<>();
//                skuList.add(new HuiDingHuoGetExpressFeeReq.SkuItem(v.getOutSpuId(),v.getOutSkuId(),v.getChannelType(),v.getOutItemCode(),v.getNum()));
//                huiDingHuoGetExpressFeeReq.setSkuList(skuList);
//                HuiDingHuoGetExpressFeeRes expressFee = HuiDingHuoApiUtils.getExpressFee(huiDingHuoGetExpressFeeReq);
//                goodsFreight.put(k,expressFee.getData().getExpAmount().multiply(new BigDecimal(100)).intValue());
//                return;
//            }
//            String redisFreightTemplate = RedisEnum.Key.FREIGHT_TEMPLATE.getCode(templateId.toString());
//            FreightTemplate freightTemplate = redisClient.getCacheObject(redisFreightTemplate);
//            if(freightTemplate == null){
//                freightTemplate = freightDomain.freightTemplate(templateId);
//                redisClient.setCacheObject(redisFreightTemplate, freightTemplate);
//            }
//            if (ObjectUtil.isNull(freightTemplate)){
//                ThrowsException.exception(BaseErrorCode.PARAM, "运费模板异常");
//            }
//            if (freightTemplate.getFreePost() == 1){
//                // 免运费
//                goodsFreight.put(k,0);
//            }else {
//                //匹配地址
//                RegionVO regionVO = matchRegion(freightTemplate.getRegionSpec(), req);
//                if(regionVO == null || regionVO.getType() == 2){
//                    ThrowsException.exception(BaseErrorCode.PARAM.getCode(), "商品不支持配送,spuId为：" + k);
//                }
//                //获取运费
//                goodsFreight.put(k,calculate(freightTemplate.getPricingManner(), regionVO, v));
//            }
//        } );
//        return ScmResult.success(new OrderGoodsCheckRes(orderGoodsInfos,goodsFreight,localGoods,outGoods));
        return null;
    }

    @Override
    public PlatformResult<OrderGoodsCheckV2Res> orderCheckV2(OrderGoodsCheckReq checkReq, List<GoodsVO> goodsList) {
//        log.info("订单商品校验开始orderGoodsCheckV2,请求参数:{}", req);
//        Map<Long, Integer> skuNum = goods.stream().collect(Collectors.toMap(GoodsVO::getStoreDistributionId, GoodsVO::getNum));
//        List<Long> idList = goods.stream().map(GoodsVO::getStoreDistributionId).collect(Collectors.toList());
//        List<StoreDistributionDetailRpcVO> storeGoods = skuDomainService.queryStoreDistributionDetailByIdList(idList);
////        List<OrderGoodsInfoVO> orderGoodsInfos = spuDomain.queryOrderGoodsInfoVOList(goods, req.getChannelId(), req.getStoreId());
//
//        // 2、校验选品或铺货
//        if(goods.size() != storeGoods.size()){
//            log.info("goods信息:{},orderGoodsInfos信息:{}", goods,storeGoods);
//            ThrowsException.exception(BaseErrorCode.PARAM, "SKU异常");
//        }
//        // 3、是否有未上架的商品
//        Optional<StoreDistributionDetailRpcVO> first = storeGoods.stream().filter(x -> x.getSpuState() != 2).findFirst();
//        first.ifPresent(orderGoodsInfoVO -> ThrowsException.exception(BaseErrorCode.PARAM.getCode(), "商品已下架,spuId为：" + orderGoodsInfoVO.getGoodsId()));
//        // 设置购买数量并区分本地和外部商品
//        List<OrderSkuVO> localGoods = new ArrayList<>();
//        List<OrderSkuVO> outGoods = new ArrayList<>();
//        Map<Long, StoreDistributionDetailRpcVO> skuOrderGoodsMap = new HashMap<>();
//        storeGoods.forEach(x->{
//            //库存校验
//            if (x.getInventory() < skuNum.get(x.getId())){
//                ThrowsException.exception(BaseErrorCode.PARAM, x.getSpuName()+":库存不足");
//            }
//            x.setBugNum(skuNum.get(x.getId()));
//            // 区分外部订单商品和本地商品(非本地商品)
//            if (x.getSupplierId() <= 10){
//                outGoods.add(new OrderSkuVO(x.getGoodsId(),x.getOutSpuId(),x.getSkuId(),x.getOutSkuId(),skuNum.get(x.getId()),x.getSupplierId()));
//            }else {
//                localGoods.add(new OrderSkuVO(x.getGoodsId(),null,x.getSkuId(),null,skuNum.get(x.getId()),x.getSupplierId()));
//            }
//            //
//            skuOrderGoodsMap.put(x.getSkuId(), x);
//        });
//        // 外部商品校验销售范围
//        // todo 待完善 需要下单前检验
////        if (outGoods.size() > 0){
////            List<CheckSkuItemReq> goodsList = new ArrayList<>();
////            for (OrderSkuVO obj : outGoods) {
////                goodsList.add(new CheckSkuItemReq(obj.getCount(),obj.getOutId()));
////                OrderGoodsInfoVO orderGoodsInfoVO = skuOrderGoodsMap.get(obj.getLocalId());
////                if(orderGoodsInfoVO != null
////                        && orderGoodsInfoVO.getBuyStartQty() != null
////                        && obj.getCount() < orderGoodsInfoVO.getBuyStartQty()){
////                    ThrowsException.exception(SpuErrorCode.BUY_START_QTY);
////                }
////            }
////            YytApiUtil.checkSku(new CheckSkuReq(goodsList,String.valueOf(req.getShipAreaCode())));
////        }
//        // 4、计算运费
//        // 定义商品运费集合
//        Map<Long,Integer> goodsFreight = new HashMap<>(8);
//        // 商品维度累计数量
//        // 商品购买数量
//        //Map<Long,Integer> goodsNum = orderGoodsInfos.stream().collect(Collectors.groupingBy(OrderGoodsInfoVO::getSpuId,Collectors.summingInt(OrderGoodsInfoVO::getNum)));
//        // 商品运费模板
//        Map<Long,Long> goodsTemplate = storeGoods.stream().collect(Collectors.toMap(StoreDistributionDetailRpcVO::getGoodsId,StoreDistributionDetailRpcVO::getFreightTemplateId,(k, v) -> k));
//        // 商品重量
//        //Map<Long, Double> goodsWeight = orderGoodsInfos.stream().collect(Collectors.groupingBy(OrderGoodsInfoVO::getSpuId,Collectors.summingDouble(OrderGoodsInfoVO::getWeight)));
//        // 按商品统计运费相关数据
//        // 将sku数据按spu分组
//        Map<Long, GoodsFreightDataVO> goodsFreightDataMap =
//                // 串行流（避免并行流线程安全/异常排查问题）
//                storeGoods.stream()
//                        .collect(Collectors.groupingBy(StoreDistributionDetailRpcVO::getGoodsId,
//                                Collectors.collectingAndThen(Collectors.toList(), m -> {
//                                    StoreDistributionDetailRpcVO orderGoodsInfoVO = m.stream().findFirst().orElseThrow(() -> new IllegalArgumentException("SPU分组下无商品数据"));
//
//                                    BigDecimal goodsWeight = m.stream().map(StoreDistributionDetailRpcVO::getWeight).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//                                    BigDecimal goodsVolume = m.stream().map(StoreDistributionDetailRpcVO::getVolume).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//                                    Integer num = m.stream().map(StoreDistributionDetailRpcVO::getBugNum).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
//
//
//                                    String expand = orderGoodsInfoVO.getExpand();
//                                    String channelType = "";
//                                    String itemCode = "";
//                                    if (StringUtils.isNotBlank(expand)) {
//                                        try {
//                                            JSONObject object = JSONObject.parseObject(expand);
//                                            channelType = StringUtils.defaultString(object.getString("channelType"), "");
//                                            itemCode = StringUtils.defaultString(object.getString("itemCode"), "");
//                                        }
//                                        catch (Exception e) {
//                                            log.warn("解析商品expand字段失败，spuId={}", orderGoodsInfoVO.getGoodsId(), e);
//                                        }
//                                    }
//
//                                    return new GoodsFreightDataVO(num, goodsWeight, goodsVolume, orderGoodsInfoVO.getOutSpuId(),
//                                            orderGoodsInfoVO.getOutSkuId(), channelType, itemCode, orderGoodsInfoVO.getSupplierId());
//                                })));
//        // 商品体积
//        //Map<Long,Double> goodsVolume = orderGoodsInfos.stream().collect(Collectors.groupingBy(OrderGoodsInfoVO::getSpuId,Collectors.summingDouble(OrderGoodsInfoVO::getVolume)));
//        // 遍历计算各商品运费
//        goodsFreightDataMap.forEach( (k,v)->{
//            Long templateId = goodsTemplate.get(k);
//            if (templateId == 1){
//                // 调用第三方供应链查询运费
//                List<FreightItemReq> spuInfoList = new ArrayList<>();
//                spuInfoList.add(new FreightItemReq(v.getNum(),v.getOutSpuId()));
//                FreightReq freightReq = new FreightReq(spuInfoList,String.valueOf(req.getShipCityCode()));
//                goodsFreight.put(k, YytApiUtil.freight(freightReq));
//                return;
//            }
//            if (templateId == 2) {// 会订货
//                HuiDingHuoGetExpressFeeReq huiDingHuoGetExpressFeeReq = new HuiDingHuoGetExpressFeeReq();
//                // todo 待完善
//                setAreaInfo(req.getShipArea(), huiDingHuoGetExpressFeeReq);
//
//                List<HuiDingHuoGetExpressFeeReq.SkuItem> skuList = new ArrayList<>();
//                skuList.add(new HuiDingHuoGetExpressFeeReq.SkuItem(v.getOutSpuId(),v.getOutSkuId(),v.getChannelType(),v.getOutItemCode(),v.getNum()));
//                huiDingHuoGetExpressFeeReq.setSkuList(skuList);
//                HuiDingHuoGetExpressFeeRes expressFee = HuiDingHuoApiUtils.getExpressFee(huiDingHuoGetExpressFeeReq);
//                goodsFreight.put(k,expressFee.getData().getExpAmount().multiply(new BigDecimal(100)).intValue());
//                return;
//            }
//            String redisFreightTemplate = RedisEnum.Key.FREIGHT_TEMPLATE.getCode(templateId.toString());
//            FreightTemplate freightTemplate = redisClient.getCacheObject(redisFreightTemplate);
//            if(freightTemplate == null){
//                freightTemplate = freightDomain.freightTemplate(templateId);
//                redisClient.setCacheObject(redisFreightTemplate, freightTemplate);
//            }
//            if (ObjectUtil.isNull(freightTemplate)){
//                ThrowsException.exception(BaseErrorCode.PARAM, "运费模板异常");
//            }
//            if (freightTemplate.getFreePost() == 1){
//                // 免运费
//                goodsFreight.put(k,0);
//            }else {
//                //匹配地址
//                RegionVO regionVO = matchRegion(freightTemplate.getRegionSpec(), req);
//                log.info("地区运费规则为orderGoodsCheckV2：{}", freightTemplate.getRegionSpec());
//                if(regionVO == null || regionVO.getType() == 2){
//
//                    ThrowsException.exception(BaseErrorCode.PARAM.getCode(), "商品不支持配送,spuId为：" + k);
//                }
//                //获取运费
//                goodsFreight.put(k,calculate(freightTemplate.getPricingManner(), regionVO, v));
//            }
//        } );
//        return ScmResult.success(new OrderGoodsCheckV2Res(storeGoods,goodsFreight,localGoods,outGoods));
        return null;
    }

    @Override
    public PlatformResult<OrderGoodsCheckRes> checkShip(OrderGoodsCheckReq checkReq, List<GoodsVO> goodsList) {
        return null;
    }

    @Override
    public List<StoreRPCVO> batchQueryStoreInfo(List<Long> storeIdList) {
        return List.of();
    }

}
