# biz-order-application 跨域跳过说明

以下编排层文件因深度跨域耦合 (goods / finance / user / openapi 的 RPC facade) 暂未迁入 src/main/java。
应用层大量直接注入外域 facade (IDistributionRpcFacade/ISpuFacade/IStoreFacade/IBalancePayFacade/
IAccountPurseConfigFacade/IAccountFacade/IChannelFacade/IShipAddressFacade/ISupplierFacade/INotifyFacade
/ThirdPartyOrderFacade), 待各域 facade 契约稳定 + domain 端口补齐后回迁。

## service impl (核心业务编排)
- `OrderServiceImpl` / `IOrderService` — 下单主流程 (依赖 create domain + 多域 facade)。
- `QueryOrderServiceImpl` — 订单查询聚合 (17 处外域调用, 耦合最重)。
- `UpdateOrderServiceImpl` — 订单更新 (9 处外域调用)。
- `RefundServiceImpl` — 售后编排。
- `OrderDeliveryAppServiceImpl` — 发货编排。

## rpc
- `OrderFacadeImpl` — 对外 facade provider (deferred: 全 biz facade provider 均暂缓)。

## mq
- `OrderStateRecordConsumer` — 继承外域基类 `com.zkl.scm.infrastructure.mq.base.consumer.AbstractMessageMQPushConsumer`
  (API `remoteProcess`/void), 与本平台 `core-rocketmq` 的 `AbstractMQPushConsumer` (API `process`/boolean) 不兼容。
  与 finance `SkuOrderEarningsConsumer` 同因跳过, 回迁时改继承 `AbstractMQPushConsumer` 并适配 `process` 方法。

## 已迁入 (保留可编译)
- 接口: OrderDeliveryAppService / OrderStateRecordService / QueryOrderService / RefundService / UpdateOrderService (去 I 前缀)。
- 实现: `OrderStateRecordServiceImpl` (纯本域, seata @GlobalTransactional 降级为 Spring @Transactional)。
