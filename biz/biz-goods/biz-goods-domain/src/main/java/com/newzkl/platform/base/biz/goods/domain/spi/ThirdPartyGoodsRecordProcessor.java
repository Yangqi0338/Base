package com.newzkl.platform.base.biz.goods.domain.spi;

import cn.hutool.extra.spring.SpringUtil;
import com.newzkl.platform.base.biz.goods.domain.service.ThirdPartyGoodsDomain;
import com.newzkl.platform.base.common.ddd.domain.Processor;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyGoodsResult;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/**
 * 第三方商品同步派发器 按平台类型路由到匹配策略
 *
 * <p>镜像 biz-order {@code ThirdPartyOrderProcessor} 的 ObjectProvider 派发 + 统一记录范式
 * (Task D)。派发器本身 supports 恒 false 从 strategyProvider 过滤时排除自身</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ThirdPartyGoodsRecordProcessor extends Processor implements ThirdPartyGoodsStrategy {

    /**
     * 商品同步动作名 落 ThirdPartyGoodsRecordDO.interfaceName
     */
    private static final String INTERFACE_GOODS_EVENT = "goodsEvent";

    private final ObjectProvider<ThirdPartyGoodsStrategy> strategyProvider;

    private final ThirdPartyGoodsDomain thirdPartyGoodsDomain;

    public static ThirdPartyGoodsRecordProcessor find() {
        return SpringUtil.getBean(ThirdPartyGoodsRecordProcessor.class);
    }

    /**
     * 同步第三方商品(派发器) 按平台类型路由到匹配策略 成功/失败均落追加式记录
     *
     * @param platformType 平台类型
     * @param outSpuId     外部商品ID
     * @param itemJson     第三方推送的商品数据 JSON
     * @return 同步结果 无匹配策略时返回 null
     */
    @Override
    public ThirdPartyGoodsResult sync(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String outSpuId, String itemJson) {
        if (platformType == null) {
            log.warn("商品同步平台类型为空 跳过派发 outSpuId={}", outSpuId);
            return null;
        }
        ThirdPartyGoodsStrategy strategy = matchStrategy(platformType);
        if (strategy == null) {
            log.warn("未找到匹配的三方商品同步策略 platformType={} outSpuId={}", platformType, outSpuId);
            return null;
        }
        try {
            ThirdPartyGoodsResult result = strategy.sync(platformType, outSpuId, itemJson);
            storeRecord(platformType, outSpuId, result, CommonEnum.RequestStatusEnum.SUCCESS, null);
            return result;
        } catch (RuntimeException e) {
            storeRecord(platformType, outSpuId, null, CommonEnum.RequestStatusEnum.FAILED, e.getMessage());
            throw e;
        }
    }

    /**
     * 批量处理待补偿记录(供定时任务调用)
     * <p>
     * TODO[deferred] 待补偿扫描未实现 依赖尚不存在的能力面 ThirdPartyGoodsRepository 缺按状态批量扫描 + 乐观锁状态流转 + 重试计数 且无调用方(JobHandler 未迁) 补齐后按指数退避重派发
     *
     * @param batchSize 单批处理条数
     */
    public void processPendingRecords(int batchSize) {
        log.warn("processPendingRecords 暂未实现 待补偿扫描能力面与定时任务调用方尚未迁入 batchSize={}", batchSize);
    }

    @Override
    public boolean supports(Object type) {
        // 派发器本身不参与业务 恒 false 使 strategyProvider 过滤时自动排除自身
        return false;
    }

    /**
     * 从 strategyProvider 中选出 supports(platformType) 命中的首个策略
     */
    private ThirdPartyGoodsStrategy matchStrategy(ThirdPartyOrderEnum.PlatformTypeEnum platformType) {
        return strategyProvider.stream()
                .filter(s -> s.supports(platformType))
                .findFirst()
                .orElse(null);
    }

    /**
     * 统一存储一次同步动作 从 ThirdPartyGoodsResult 取已序列化 req/res 落追加式动作日志
     *
     * <p>存储失败仅记日志不抛出 不影响三方商品同步主流程</p>
     */
    private void storeRecord(ThirdPartyOrderEnum.PlatformTypeEnum platformType, String outSpuId,
                             ThirdPartyGoodsResult result, CommonEnum.RequestStatusEnum status, String errorMessage) {
        try {
            String requestJson = result == null ? null : result.getGoodsReq();
            String responseJson = result == null ? null : result.getGoodsRes();
            String recordOutSpuId = result == null || result.getOutSpuId() == null ? outSpuId : result.getOutSpuId();
            thirdPartyGoodsDomain.recordAction(platformType, recordOutSpuId, INTERFACE_GOODS_EVENT,
                    requestJson, responseJson, status, errorMessage);
        } catch (Exception ex) {
            log.error("存储三方商品同步记录失败 platformType={} outSpuId={}", platformType, outSpuId, ex);
        }
    }
}
