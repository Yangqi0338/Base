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

### 校验文案免写机制（QDox javadoc 提取）
- 校验注解**不必手写** `message`：靠 Jakarta/Hibernate Validator 自带 zh_CN 默认文案（`@NotNull`→"不能为空"）。
- `GlobalExceptionHandler` 运行时用 **QDox 读 .java 源码**取字段 javadoc **首行**（切逗号/句号/分号/换行前），拼成 `[字段业务名]默认文案`（如 `[课程ID]不能为空`）。
- **手写 message 优先**：字段若手写了 `message`，仍走同一拼接（`[javadoc首行]手写message`）。非黑即白删除——手写的保留，没写的吃默认。
- **前置基建（已就位）**：① root `pom.xml` `<resources>` 把 `src/main/java/**/*.java` 打进 jar（生产 classpath 可读源码）；② `ValidationConfig` 固定 `Locale.CHINA`（不随请求 Accept-Language 漂）；③ ddd-action 引 `spring-boot-starter-validation`。
- 复用 `CommonUtil.findJavaClass/findJavaField`（core-utils，QDox 解析+缓存 `JAVA_CLASS_CACHE`）。
- 覆盖异常：`MethodArgumentNotValidException`/`ConstraintViolationException`/`BindException` 三类。
- **字段 javadoc 首行 = 业务语义名**（本身即 java-architecture 硬约束），机制依赖之。

### @OauthUserId / @OauthRole 登录信息注入（字段级, 纯 validation 绑定）
- 字段标 `@OauthUserId`/`@OauthRole`（**ddd-model/auth**），`@Valid` 触发时由类级校验器 `OauthUserInjectionValidator`（**ddd-utils/auth**）扫描回填当前登录用户ID/角色。
- 触发条件：宿主类须带 `@OauthUserInjection`（类级 constraint）。**`BaseReq` 已标**，所有子类自动继承生效。
- **`@OauthUserId`**：字段类型仅 `Long`/`String`（回填账号ID）。
- **`@OauthRole`**：字段类型 `Long`/`String`（回填角色ID）或 `RoleEnum.CompanyRole`（回填角色枚举）。
- 两注解共 `required` 属性：`required=true`(默认) 取不到值判校验失败，`required=false` 放行不回填。
- 取值链：`OauthUserInjectionValidator` 直连 `SecurityUtils.getAccountId()`/`getRoleId()`（同在 ddd-utils，无跨模块桥接）。
- **禁忌**：`@OauthUserId`/`@OauthRole` 字段**不要**再叠 `@NotNull`（值来自 token 非客户端，且校验顺序无保证——注入须先于 NotNull 检查）。

#### 反依赖解法（注解在 ddd-model, 校验器在 ddd-utils, ddd-model 不反依赖 ddd-utils）
- `@OauthUserInjection` 的 `@Constraint(validatedBy = {})` **留空**，ddd-model 零 validator 引用。
- validator 经 ddd-utils **两份 XML 声明式绑定**（Bean Validation 规范强制两套 schema，不可合一）：
  - `META-INF/validation.xml`（bootstrap, 全局唯一）→ `<constraint-mapping>` 引用 mapping 文件。
  - `META-INF/oauth-injection-constraints.xml`（constraint-mapping）→ `<constraint-definition>` 绑 `OauthUserInjection` → `OauthUserInjectionValidator`。
- 后续若有别的 ddd-model 注解要绑 ddd-utils validator，往 `oauth-injection-constraints.xml` 追加 `<constraint-definition>` 复用，不新增文件。
- **依赖补充**：ddd-model 引 `jakarta.validation-api`（`@Constraint` meta-annotation 注解处理器需真实解析）；ddd-utils 引 `jakarta.servlet-api`(provided) + `spring-security-crypto`（`SecurityUtils` 用）。

### @RoleLimit 角色鉴权（全局 AOP）
- `@RoleLimit`(ddd-action/auth) 可标 `METHOD`/`TYPE`，`value()` = 允许的 `RoleEnum.CompanyRole[]`（空=放行）。
- `RoleLimitAspect`(`@Around @within||@annotation`, `@Order(1)`) 取 `SecurityUtils.getRole()` 比对，不匹配抛 `PlatformException(BaseErrorCode.NO_AUTH)`。
- **全局唯一**：biz-store 旧 `enums.RoleLimit` 已删，统一用 ddd-action 版。

## 违规白名单

<!-- 空 -->
