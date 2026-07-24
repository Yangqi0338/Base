# CLAUDE.md — Base 项目规范入口

> Base = 跨域中台底座单仓（common + biz）。**动手前先读 `rules/`**。

## 规格大前提（@import — 每轮重载，compact 免疫）

> 下列 @import 保证 Constitution/Architecture/SPEC/Project 每轮进 context，不随 /compact 丢失。
> 优先级：技术校验 SPEC > Architecture > skill；业务正确 Constitution 独立。

@rules/Constitution.md
@rules/Architecture.md
@rules/Project.md
@SPEC.md

## rules/（长期规范，优先级高→低）

| 文件 | 作用 | 何时读 |
|------|------|--------|
| `rules/Architecture.md` | 技术红线（arch-mode=biz-system、三硬线、分层、common 两级、中台拆细哲学） | 写/改任何 `.java` 前 |
| `rules/Constitution.md` | 业务铁律 | 涉及业务逻辑时 |
| `rules/Project.md` | 项目元信息（模块清单、技术栈） | 需全局背景时 |
| `rules/DeadEndpoint.md` | 死接口生命周期（@Deprecated 标记/观察/清理，权威清单指向 docs） | 动某 biz 端点前 / 清理死代码时 |

## SPEC.md（根目录，当前 feature 动态规格）

- 当前 feature 需求/假设/验收 + 违规白名单
- 优先级：技术校验 **SPEC > Architecture > skill 默认**

## 强制校验

- 写 `.java` 走 `ddd-architecture` skill（结构）+ `java-architecture` skill（语言）
- commit 前 `ddd-commit-guard` 跑 `validate-ddd.js`，三硬线违规 exit2
- 校验判定源 = `rules/Architecture.md` 的 `uses:`/`arch-mode:`

## 关键事实（防重开丢）

- groupId `com.newzkl.platform`，包根 `com.newzkl.platform.base.common.{ddd,core}.*`
- biz-system：Base 业务域是纯库（无 starter）；入口 starter 在 `building-scm`/`building-mmt`
- common 两级：`ddd/`(model/domain/application/action/infrastructure) + `core/`(utils/redis/rocketmq)
- 中台拆细：广义不属于商品的全拆独立 biz + SPI，反 building-old 大合并
- 参考源：`/d/project/adopt-chicken-deploy`(模板) + `/d/project/zhongze/adopt-chicken`(codegraph)

## Git 提交规范

提交信息含身份 + 模型清单：
```
Author-Role: 全栈工程架构师
Author-Name: KC
Models: <本次模型清单>
```
仅在用户明确要求时才 commit。
