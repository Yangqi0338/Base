package com.newzkl.platform.base.biz.goods.model.goods.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品开发者通知 MQ 载体 (收件人未解析)
 *
 * <p>goods 域写主数据后发布本载体, 只带 spuId + 业务类型 + 事件内容 JSON, 不含收件人。
 * goods 域不依赖 market 域, 无法解析哪些渠道订阅了该 SPU, 故收件人解析下沉到 openapi 消费方,
 * 由其查订阅关系拿到 accountId 列表后, 转投既有 {@code DEVELOPER_NOTIFY_EVENT} 完成 HTTP 回调。</p>
 *
 * @author KC
 */
@Data
public class GoodsDeveloperNotifyMq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SPU 主键, openapi 侧据此反查订阅渠道
     */
    private Long spuId;

    /**
     * 业务类型
     *
     * @ext {@link com.newzkl.platform.base.biz.goods.model.enums.NotifyEnums.GoodsType} 的 code
     */
    private Integer businessType;

    /**
     * 事件内容 JSON, 结构随 businessType 而定 (ApiSpuEditEvent / ApiSkuEditEvent / ApiGoodsSaleStateEvent)
     */
    private String eventContent;
}
