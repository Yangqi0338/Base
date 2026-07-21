---
type: spec
scope: feature
priority: overrides-architecture
---
# SPEC — 当前 feature 规格

> 优先级：技术校验 SPEC > Architecture > skill 默认。

## 需求

首轮：Base 单仓 DDD 骨架初始化（§3.5 只搭骨架不写业务）。
Base + bom + common(ddd 5 + core 3) + biz(空聚合)。

## 假设清单

- groupId `com.newzkl.platform`，包根 `com.newzkl.platform.base.common.{ddd,core}.*`（已确认）
- common 走 DDD v2 两级 ddd/core（已确认）
- arch-mode biz-system（已确认）
- 版本对齐 adopt-chicken（Java21/Boot3.2.5），processor 补 record-builder v44（skill 要求，模板缺）
- core 首轮 3 模块（utils/redis/rocketmq）；core-im 待 plugin-im 落地再加
- biz modules 空，服务域边界 grill 未完（activity/裂变待答）

## 验收标准

- `mvn -N validate` 根 pom 解析通过
- 13 pom 结构完整（root+bom+common+ddd聚合+core聚合+5 ddd叶+3 core叶+biz）
- 包目录 `com/newzkl/platform/base/common/{ddd,core}/*` 就位
- 无业务代码（骨架 only）

## 项目约定

### getLw 约定
- 每 DAO 至少一 `getLw(XxxQuery)`，返回 `BaseLambdaQueryWrapper<T>`（ddd-infrastructure 增强类）
- Repository 实现调 getLw 建条件再操作 DAO

## 违规白名单

<!-- 空 -->
