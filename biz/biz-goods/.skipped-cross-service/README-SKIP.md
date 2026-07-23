# goods 域跳过文件清单

本目录保存迁移时无法纳入编译的源文件, 保留原始包结构以便后续人工恢复。

## application/goods/excel/SpuImportData.java

- **跳过原因**: 依赖 `cn.afterturn.easypoi.excel.annotation.Excel`, 该 easypoi 依赖当前 Base 平台
  BOM / 各模块 pom 均未引入, 编译报 `IllegalArgumentException: The argument does not
  represent an annotation type: Excel`。
- **孤立性**: 其唯一使用方 `SpuImportService` 因跨服务耦合 (user/openapi/market) 已在 domain/application
  迁移阶段被排除, 故本 DTO 无实际消费者。
- **恢复条件**: 平台引入 easypoi 依赖 (或改用 EasyExcel / 自研导入注解) 且补齐 `SpuImportService` 后,
  可将本文件移回 `biz-goods-application/.../application/goods/excel/` 并按需重映射。

## application/goods/service/relation/{IRelationService, impl/RelationServiceImpl}.java
## application/goods/service/statistics/{IGoodsRelationOperateDataService, impl/GoodsRelationOperateDataServiceImpl}.java

- **跳过原因**: 该 4 文件属于选品市场 (market) 商品关联域, 依赖
  `domain.market.relation.*`、`model.market.dto.relation.*` 与 `rpc.model.relation.*`,
  这些包归属 biz-market 模块, 不在纯商品域 (spu/brand/freight/goodsZone/interaction/report) 迁移范围内。
- **孤立性**: 仅彼此内部互相引用, 商品域内无其它消费者。
- **恢复条件**: biz-market 模块建立、market relation 领域/DTO 契约就位后, 迁入 biz-market 而非 biz-goods。

## application/goods/service/goods/IGoodsQueryService.java (部分方法裁剪, 非跳过)

- 原接口聚合选品市场 / 汇订货 / openapi 跨服务查询, 迁移时裁剪为仅保留商品域内
  `IWorktableProcessor` 依赖的 `spuVO` 查询; 其 impl (`GoodsQueryServiceImpl`) 因跨服务耦合未迁移。
  待 biz-market / openapi 就位后补齐完整查询能力与实现。
