---
uses: [java, ddd, git]
ddd-style: default          # 本团队方言
arch-mode: biz-system       # 各服务域独立模块(无 starter)，入口 starter 在 building-scm/building-mmt
constraints:
  - layer-dependency        # domain 不依赖 infra（依赖倒置）
  - do-not-leak             # 对外边界(facade/RPC)不暴露内部 model（防腐）
  - cross-service-facade    # 跨服务域只过 facade
---
# Architecture — Base 技术宪法

> 技术红线。校验判定源 = 上方 `uses:` 标记。validate-ddd.js 读此文件。

## 定位

Base = 跨域中台底座单仓。含两块：
- `common/` — 全项目共享底座（ddd 分层基类 + core 技术中间件）
- `biz/` — 业务服务域库（biz-system 模式：各服务域独立 Maven 模块，**不含 starter/可执行 jar**）

可执行入口 starter 在独立仓 `building-scm`(内供应链后台) / `building-mmt`(脉脉通 C 端)，依赖引入 Base 的 biz 模块组装启动。

## 架构模式：biz-system

选 biz-system 理由：双入口部署（scm 后台 + mmt C 端），业务域被多入口共享，故业务域做纯库（无 main），入口侧 starter 单独立仓按需组装。cloud 过重（当前不需每域独立 boot），single 不支持双入口。

## 中台拆细哲学（覆盖旧合并思路）

**广义上不属于商品的，全拆成独立 biz + SPI 关联**。反对 building-old 的 mega-goods / mega-user 大合并。各 biz 独立，能力经 SPI 开放，biz 自带默认实现兜底。

## common 两级结构

```
common/
  ddd/                          # DDD 分层通用基类（依赖方向 infra→domain→model, action→application→domain）
    ddd-model                   # BaseDTO/BaseVO/BaseReq/PageQuery/通用Res/BaseAssembler/ErrorCode/BizException
    ddd-domain                  # 通用 domain service 基类 / BaseRepository / BaseApi port
    ddd-application             # 通用编排基类（常空，留位）
    ddd-action                  # 全局异常 @RestControllerAdvice / 通用 controller 基类 / 通用读 Command / web config
    ddd-infrastructure          # BaseRepositoryImpl / getLw(BaseLambdaQueryWrapper) / BaseDO
  core/                         # 技术中间件（不反引 ddd；ddd 任意层可引 core）
    core-utils                  # TransferUtils / 雪花ID / 业务码生成器 / 金额 / JSON
    core-redis                  # RedisUtil 分类(Hash/List/Set/ZSet/Str/Number) + Redisson 分布式锁
    core-rocketmq               # MQUtil(原 scm-message 降级) + @MQProducer/@MQConsumer
```

**依赖规则**：core 不反引 ddd（单向）；任意 ddd 层可引 core。

## 服务域分层结构（biz-system，每服务域一组模块）

```
<biz>-<服务域>-model/            # 共享内核: Entity/VO/Req/Res/Query/DTO（Command/DO 不在此）
<biz>-<服务域>-domain/
    service/                     # 业务平铺 flat，@Transactional 默认在此
    adapt/repository/            # 持久化接口（干净名，收发 DTO）
    adapt/api/                   # outbound port 接口
<biz>-<服务域>-facade/           # 对外契约（自带 model/，物理禁引 <svc>-model）
<biz>-<服务域>-application/      # 跨业务域编排 + 分布式事务上移点（纯透传禁写类）
<biz>-<服务域>-infrastructure/
    adapt/repository/            # XxxRepositoryImpl（DO↔DTO 转换）
    adapt/api/                   # port 实现 + consumer 调远程 facade
    gateway/                     # 出站网关: http 客户端调用 / mq publisher（空域也留占位）
    entity/                      # DO（持久化对象；禁用 po/ 命名）
    dao/                         # DAO/Mapper（getLw 约定）
```
Controller/starter 不在 Base，在入口侧仓（building-scm/building-mmt）。

**infra 布局硬规则**：
- DO 目录名固定 `entity/`，**禁止 `po/`**。
- RepositoryImpl 固定 `adapt/repository/`，**禁止裸 `repository/`**。
- 出站网关(http/mq publisher)固定 `gateway/`，无实现也保留空占位目录。
- 通用持久化基座(RepositorySupport/BaseLambdaQueryWrapper/BaseQueryWrapper/分页转换)在 `common/ddd/ddd-infrastructure`，**禁止各 biz 自拷**。

## 技术红线

- domain 不得 import infra 具体类（DO/DAO/RedisTemplate/Feign 实现）
- 对外边界(facade)不得暴露内部 model；facade 模块自带 model，物理禁引 `<svc>-model`
- 跨服务域禁直连他域 `.domain.`（除 `.facade.`）
- 接口去 I 前缀，实现带 Impl；domain=`XxxDomain`、application=`XxxService`、Repository=`XxxRepository`、facade=`XxxFacade`
- 拷贝硬禁 `BeanUtils.copyProperties`，用 `TransferUtils`
- 集合返回永远非 null；单对象 infra 返 null、domain 判 null 抛 `BizException`
- 事务默认 domain service；分布式事务上移 application
- 能力开放走 SPI，biz 自带默认实现；plugin 自由组装，无父子关系

## 版本 / 构建

- Java 21 / Spring Boot 3.2.5 / Spring Cloud 2023.0.1 / Dubbo 3.3.2
- groupId `com.newzkl.platform`，包根 `com.newzkl.platform.base.*`
- flatten `${revision}` 全局版本一处改；processor 序 lombok→record-builder→mapstruct-plus（已在根 pom pluginManagement）
- Lombok：VO/Command=record+`@RecordBuilder`；DTO/Entity/facade DTO=`@Data`+`@Builder`；DO/Req/Query/Res=`@Data` 禁 @Builder
- 用 record/record pattern/pattern-matching switch；**sealed 不用、var 不用**

## 为何这样定

贫血模型 + 结构隔离（包/模块）是唯一约束手段，故分层放置严格。无充血、少用 Aggregate、业务逻辑平铺 domain service（transaction-script）。详见 ddd-architecture skill。
