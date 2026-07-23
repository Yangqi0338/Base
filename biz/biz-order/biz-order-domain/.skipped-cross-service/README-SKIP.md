# biz-order-domain 跨域跳过说明

以下文件因深度跨域耦合 (goods / finance / user / openapi 的 RPC facade + 三方汇订货 huidinghuo)
暂未迁入 src/main/java, 待各域 facade 契约稳定后, 通过 `domain/adapt/api/` 端口 + infra stub 补齐再回迁。

## service (订单/售后编排)
- `ICreateOrderDomain` / `CreateOrderDomainImpl` — 下单编排, 依赖 finance(OrderPayReq/TradeBaseRes/ChannelNowServiceFeeRes/EarningsConfigRpcVO)、goods(DistributionDetailVO/ModelShopDataDTO)、user(ShipAddressRpcVO)。
- `RefundDomainImpl` — 售后编排, 依赖 goods(DistributionDetailVO)、openapi(NotifyEventContent)、user(AccountQuery)。

## factory (运费策略 + 三方下单策略)
- 整个 `factory/` 子系统: `FreightStrategyFactory`、`ThirdPartyOrderStrategyFactory`、`freightStrategy/**`。
- 依赖 goods(FreightTemplateRPCVO/RegionRPCVO/OrderSkuVO)、openapi(ThirdPartyOrderFacade/ThirdPartyOrderRequest)、user(ShipAddressRpcVO)
  以及 scm-common 三方汇订货模型 `com.zkl.scm.model.biz.{req,res}.huidinghuo.*` (Base 未提供)。

## 回迁建议
1. 在 `domain/adapt/api/` 定义 GoodsApi / FinanceApi / PayApi / UserApi / NotifyApi / ThirdPartyOrderApi 端口, 方法签名仅取本域实际调用点。
2. 端口入参/出参用 `model/support/api/` 本地 DTO (已建 DistributionDetailVO/StoreRPCVO/ChannelRes 等), 补齐缺失类型。
3. infra `adapt/api/` 提供 stub 默认实现 (返回安全默认值 + TODO[cross-service])。
4. huidinghuo 三方模型需在 model/support/api 本地化或引入独立三方 SDK 模块。
