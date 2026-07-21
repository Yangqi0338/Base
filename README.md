# Base — 跨域中台底座

平台中台底座单仓。坐标 `com.newzkl.platform:Base`，包根 `com.newzkl.platform.base.*`。

## 结构

```
Base/  (spring-boot-starter-parent, flatten ${revision})
├── bom/                内部模块版本统一管理
├── common/
│   ├── ddd/            ddd-model / ddd-domain / ddd-application / ddd-action / ddd-infrastructure
│   └── core/           core-utils / core-redis / core-rocketmq
└── biz/                业务服务域聚合 (biz-system, 无 starter; 服务域逐个加入)
```

## 关键约定

- **arch-mode**: biz-system。业务域是纯库(无 main); 入口 starter 在 `building-scm` / `building-mmt`。
- **common 两级**: `ddd/`(分层通用基类) + `core/`(技术中间件, 不反引 ddd)。
- **groupId 逐级追加**: `com.newzkl.platform:Base` → `:common` → `:ddd`/`:core` → 叶子。
- **processor 序**: lombok → record-builder(v44) → mapstruct-plus。
- **中台拆细**: 广义不属于商品的全拆独立 biz + SPI 关联。

规范见 `rules/`(Architecture/Constitution/Project) + `SPEC.md`。动手前先读 `CLAUDE.md`。

## 构建

```bash
mvn validate    # 结构校验
mvn install     # 装本地仓 (供 building-scm/building-mmt 依赖)
```

Java 21 / Spring Boot 3.2.5 / Spring Cloud 2023.0.1 / Dubbo 3.3.2 / MyBatis-Plus 3.5.15。
