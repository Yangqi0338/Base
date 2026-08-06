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

## SQL 迁移文件命名规范

变动数据库结构时，需要对原数据进行修改时，按以下规范生成 SQL 文件：

**命名格式**：
```
V{version下划线转换}_{版本类型}_{yyMMddHHmmssSSS时间戳}_{首字母}_{数据表名称}_{变动主题中文}.sql
```

**参数说明**：
- `version`：取 `application.yml` 中 `platform:sys:version` 或 `pom.xml` 中 `project.version`（点号转下划线）
- `版本类型`：`SNAPSHOT=0`，`release=1`
- `时间戳`：生成时当前时间，格式 `yyMMddHHmmssSSS`（年取后两位 + 月日时分秒毫秒）
- `首字母`：操作类型首字母（`I`=INSERT、`U`=UPDATE、`D`=DELETE、`A`=ALTER 等）
- `数据表名称`：操作的目标表名
- `变动主题中文`：简短描述本次变动内容

**存放路径**：
```
resource/sql/{表名首个下划线前英文}/
```

**示例**：
- 版本 `1.0.1-SNAPSHOT`、时间 `2026-08-06 08:58:48.058`、INSERT 操作、目标表 `account`、主题"迁移平台账号数据至account表"
- 生成文件名：`V1_0_1_0_260806085848058_I_account_迁移平台账号数据至account表.sql`
- 存放路径：`resource/sql/account/`
