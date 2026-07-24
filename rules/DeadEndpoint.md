# 死接口生命周期规则 (跨会话)

> 来源 #128 弃用接口审计。目的: 标记前后端双向零引用的死接口, 迁移/清理时跳过或删除, 不浪费精力维护死代码。

## 判定 (双向零引用)
- **确认弃用**: 前端 7 仓 (platform-admin/yys/gys/channel/decoration-admin/decoration-h5/mmt-app) grep 零命中 **且** 后端无 job/mq/facade caller。
- **疑似**: 单侧零 (前端零但后端有 caller / 前端未迁到 Base 后端 / WIP 脚手架)。不标 @Deprecated, 待复审。

## 标记规范 (仅"确认弃用")
确认弃用的 controller 方法打 `@Deprecated` + JavaDoc `@deprecated` 标注:

```java
/**
 * ... 原有 JavaDoc ...
 *
 * @deprecated [DEAD-ENDPOINT #128 审计 2026-07-24] 前端7仓零引用 + 后端无caller。
 *   待删: 若项目完成后仍未被接线调用, 则删除本方法。详见
 *   docs/planning/dead-endpoint-audit/README.md。
 */
@Deprecated
```

## 生命周期 (跨会话追踪)
1. **标记期**: 确认弃用打 @Deprecated, 保留代码 (防误判 / 待前端确认)。
2. **观察期**: 项目推进中, 若某标记接口**被重新接线调用** → 去 @Deprecated + 从 `docs/planning/dead-endpoint-audit/README.md` 确认清单移除 (转"保留")。
3. **清理期**: 项目**最终完成**时, 仍带 @Deprecated 且无调用的接口 → **删除** (方法 + 无用 domain/repo 链, 注意复用方法保留)。

## 权威清单
`docs/planning/dead-endpoint-audit/README.md` = 唯一真相源 (确认弃用 / 疑似 / 保留 + 逐 biz raw/)。
每次动某 biz 前先查此清单。新审的 biz 补入。

## 跨会话保证
本规则 = checked-in 文件, 每会话经 `CLAUDE.md` rules 表加载。审计清单落 docs (随仓)。@Deprecated 标记随代码。三者共同保证死接口状态不随会话丢失。
