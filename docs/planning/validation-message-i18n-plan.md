# 优化方案 — 校验 message 去手写 + QDox javadoc 提取 + zh_CN i18n

## 背景 / 打脸修正

用户初始前提「全局异常已经过 javadoc 提取处理」= **不成立**。核查:
- `ddd-action/GlobalExceptionHandler.java:44-59` 仅 `fieldError.getDefaultMessage()` 拼接,无 javadoc 逻辑
- 全仓无 QDox 调用于 handler、无 ValidationMessages.properties、无 MessageSource/LocaleResolver
- 参考源 adopt-chicken **有** QDox 机制(`CommonUtil.findJavaClass/findJavaField` + handler 拼 `[javadoc]defaultMsg`)

Base 已具备零件(非死代码,待接线):
- root pom 已引 qdox 2.1.0 (line 264-266) + qdox.version (line 67)
- `core-utils/CommonUtil.java` 已移植并增强 QDox 方法(findClassDirPath 自动扫模块根 / findJavaClass 带缓存 / findJavaField 递归父类 / loadJavaSource 磁盘+classpath 双路)
- 缺口: handler 未接线 QDox + root pom 未打包 .java + 无 zh_CN locale 强制

## 决策(用户已拍板)

1. **全删** 所有 `message=` 属性(含 @Size/@Min/@Max 约束值),保留 `groups=`。约束值损失可接受 — jakarta zh_CN 默认文案已含 {value}/{min}/{max}
2. **root pom 内嵌 .java** 进生产 classpath(仿 adopt-chicken `<resource> src/main/java **/*.java`)
3. **javadoc 只取首行**(切 `,`/`。`/换行前),符 java-architecture「首行只写业务语义」约定

## jakarta zh_CN 实测文案(hibernate-validator 8.0.1)

| 注解 | 默认 zh_CN | 拼字段后 |
|------|-----------|---------|
| @NotNull | 不能为null | [课程ID]不能为null |
| @NotBlank/@NotEmpty | 不能为空 | [章节标题]不能为空 |
| @Size | 个数必须在{min}和{max}之间 | [标签]个数必须在0和5之间 |
| @Min | 最小不能小于{value} | [章节数]最小不能小于1 |
| @Max | 最大不能超过{value} | ... |

## 实施步骤

### Step 1 — root pom 打包源码(生产 classpath 读 .java)
`Base/pom.xml` `<build>` 加 `<resources>`:
```xml
<resources>
    <resource><directory>src/main/resources</directory></resource>
    <resource>
        <directory>src/main/java</directory>
        <includes><include>**/*.java</include></includes>
    </resource>
</resources>
```
子模块继承。生产 jar 内嵌 .java,CommonUtil.loadJavaSource classpath 分支兜底。

### Step 2 — zh_CN locale 强制(ddd-action 新增 config)
Hibernate 默认读 `Locale.getDefault()`,多语言 Accept-Language 会飘。新增 `ValidationConfig`:
- `LocalValidatorFactoryBean` + 自定义 `MessageInterpolator` 委托 `ResourceBundleMessageInterpolator` 且 locale 钉死 `Locale.CHINA`
- 落 `common/ddd/ddd-action/.../config/ValidationConfig.java`,starter component-scan 自动装配

### Step 3 — 重写 GlobalExceptionHandler(接线 QDox)
`ddd-action/GlobalExceptionHandler.java`:
- `MethodArgumentNotValidException`: 遍历 fieldErrors → `CommonUtil.findJavaClass(target.getClass())` → `findJavaField(fieldName)` → `getComment()` 取首行 → 拼 `[首行]defaultMsg`,`;\n` 连接
- **新增** `@ExceptionHandler(ConstraintViolationException.class)`(@Validated 单参校验,55 文件用,现无处理 → 500)
- **新增** `@ExceptionHandler(BindException.class)`(表单/query 绑定)
- 删除 `.replace("?", "不能为空!")` hack(? 源于旧 encoding 问题,jakarta zh_CN 修正)
- 首行提取工具: `StrUtil.subBefore(comment, [",", "。", "\n"], false)` trim

### Step 4 — 全删 message(150 文件 701 处,并行 subagent)
正则删 `message = "..."` 保留其他属性。三形态:
- `@NotNull(message = "x")` → `@NotNull`
- `@NotNull(message = "x", groups = A.class)` → `@NotNull(groups = A.class)`
- `@Size(max = 20, message = "x")` → `@Size(max = 20)`

按 biz 模块 + common 分片,并行 executor(每片自查编译)。

### Step 5 — 验证
- `mvn -pl common/ddd/ddd-action -am compile` 编译过
- 单元测试: 构造非法 Req → handler 出 `[字段中文]默认文案`
- grep 复查 `message\s*=\s*"` 归零(除白名单)
- QDox 首行提取单测(含继承字段 BaseReq)

## 风险 / 注意

- **QDox 生产读源**: 依赖 Step1 打包。若 starter(building-scm/mmt)自身未继承此 resources → 其打的 jar 需同配。**跨仓待确认** — Base 库 jar 内嵌自身 .java 即可,handler 读的是 Req 类源码(在 biz-*-model jar 内),故 Base 各 model jar 内嵌即够
- **性能**: QDox 解析带 CommonUtil 缓存(JAVA_CLASS_CACHE/CLASS_DIR_CACHE),首次解析后走缓存
- **@AssertFalse 等自定义语义**(如 ProjectSaveReq「标签每个最多四个字符」): 全删后变默认「只能为false」— 语义丢失。用户已接受「不对就不对」
- **groups 保留**: 正则须精确,勿误删 groups/其他属性

---

# Task2 — @RoleLimit 真生效(迁 new-scm AOP)

## 现状

- Base 现有 `biz-store-model/.../enums/RoleLimit.java` = **惰性标记**(TODO[auth-defer], 无 AOP, biz-store 独占, 无人 import)
- new-scm 有真生效版: `common-auth/api/RoleLimit.java` + `filter/RoleLimitAop.java`(@Around `@annotation`/`@within` → `SecurityUtils.getRole()` vs `value()` → 失败抛 NO_AUTH)
- Base `RoleEnum.CompanyRole` 已在 **ddd-model**(全局可用), `getByCode(Long)` 存在; `SecurityUtils.getRole()` 存在(core-utils)

## 决策

RoleEnum 已下沉 ddd-model(全局) → RoleLimit+AOP 提升为**全局**落 `ddd-action.auth`(与 FuncPermission 并列), 替代 biz-store 惰性标记。

## 步骤

1. `ddd-action/.../auth/RoleLimit.java`: 注解(`@Target METHOD,TYPE`, `@Retention RUNTIME`, `CompanyRole[] value()`)
2. `ddd-action/.../auth/RoleLimitAspect.java`: 仿 FuncPermissionAspect + RoleLimitAop 合并
   - `@Around("@within(...RoleLimit) || @annotation(...RoleLimit)")`
   - 取当前 `SecurityUtils.getRole()`, `ArrayUtil.contains(roleLimit.value(), role)`; 空 value()=不限制
   - 失败抛 `PlatformException(BaseErrorCode.NO_AUTH)`(1010 已存在)
   - `@Order` 与 FuncPermission 协调
3. 删 `biz-store-model/.../enums/RoleLimit.java`(无人引, 全局版替代); biz-store-action 4 controller 改 import 全局版
4. 覆盖 Architecture.md 「RoleLimit 惰性」描述 → 落 SPEC.md 已生效

## 注意

- Architecture.md 标 RoleLimit 为 `TODO[auth-defer]` 惰性 → 本任务**生效化**, 属 SPEC override Architecture(优先级 SPEC>Architecture)
- AOP 依赖 SecurityContext 已填(SecurityContextFilter 存在)

---

# Task3 — @OauthUserId 字段注解(校验前注入登录用户ID)

## 需求

field 注解, 在 validation 前, 用 `SecurityUtils.getAccountId()`(现拿 token, 后续可改 header)把用户 id 塞进字段。attr `required` 判未登录是否报错, **默认 true(报错)**。

## 机制

`RequestBodyAdvice.afterBodyRead()` — 反序列化后、`@Valid` 校验前触发。此时注入 → 后续 @NotNull 校验能过。

## 步骤

1. `ddd-action/.../auth/OauthUserId.java`:
   ```java
   @Target(FIELD) @Retention(RUNTIME)
   public @interface OauthUserId {
       boolean required() default true;  // 未登录是否报错
   }
   ```
2. `ddd-action/.../config/OauthUserIdInjectAdvice.java implements RequestBodyAdvice`:
   - `supports()`: true(afterBodyRead 内再反射判有无 @OauthUserId 字段, 省开销可缓存 class→fields)
   - `afterBodyRead(body,...)`: 反射遍历 body 字段带 @OauthUserId → `Long uid = SecurityUtils.getAccountId()` → uid==null 且 required → 抛 `PlatformException(USER_NOT_LOGIN)`; 否则 `field.set(body, uid)`
   - 字段类型校验: 仅 Long/String 赋值(getAccountId 返 Long)
   - `@ControllerAdvice` 注册(与 GlobalExceptionHandler 同 config 包)
3. 缓存: `ConcurrentHashMap<Class,List<Field>>` 避免每次反射全扫

## 注意

- `@RequestBody` 走 RequestBodyAdvice; `@ModelAttribute`(query/form)不走 → 若需覆盖 form 另需 HandlerMethodArgumentResolver。**先只覆盖 @RequestBody**(主流 JSON 入参), form 场景待确认
- required=true 未登录 → USER_NOT_LOGIN(611 已存在)
- 注入后字段可省 @NotNull(值已填), 但保留无害

---

# 依赖补充(共用)

- `ddd-action/pom.xml` 加 `spring-boot-starter-validation`(hibernate-validator impl; Boot 2.3+ web starter 不再含)

# 执行顺序

1. 依赖补充(pom validation)
2. Task1 Step1-3(pom resources + ValidationConfig + handler QDox) → 编译验证
3. Task2(RoleLimit 全局+AOP)
4. Task3(@OauthUserId + advice)
5. Task1 Step4(并行删 701 message) — 放最后, 前面机制就绪才删
6. 全量编译 + 单测 + SPEC.md 落约定

## 落点覆盖

完成后三机制(QDox 校验提取 / @RoleLimit / @OauthUserId) = 项目约定 → 写入 `SPEC.md`「项目约定」节(自定义机制、减明面代码量,约定大于配置)。

---

# 执行结果 / 决策修正(2026-08-05 完成)

## 打脸修正(覆盖上文旧决策)

- **Task1 Step4「全删 701 message」= 取消**。用户后续细化:「如果手写了,就用手写的」。QDox 机制对手写 message 与 i18n 默认文案**同样拼接** `[javadoc首行]{msg}`,无需删——手写的保留,没写的吃 zh_CN 默认。故批量删除多余且有损。
- **Task3 机制从 `RequestBodyAdvice` 改为纯 validation 绑定**。用户细化:「只字段级,纯 validation 绑定」。改用类级 constraint `@OauthUserInjection`(标在 `BaseReq`,子类继承)+ `OauthUserInjectionValidator`(class-level `ConstraintValidator` 才拿得到 bean 实例,field-level 只拿字段值)。`@Valid` 触发即注入。
- **模块环规避**:`ddd-model` 不能依赖 `core-utils`(`SecurityUtils` 引 `RoleEnum` 反向成环)→ 引入 `CurrentUserProvider` 接口(ddd-model)+ `CurrentUserProviderImpl`(ddd-action 桥接 `SecurityUtils.getAccountId()`)。

## 落地文件

- root `pom.xml` — `<resources>` 内嵌 `src/main/java/**/*.java`
- `ddd-action/pom.xml` — `spring-boot-starter-validation`
- `ddd-action/config/ValidationConfig.java`(新) — 钉 `Locale.CHINA`
- `ddd-action/config/GlobalExceptionHandler.java`(重写) — QDox 拼接 + ConstraintViolation/BindException
- `ddd-action/auth/RoleLimit.java` + `RoleLimitAspect.java`(新) — 全局 AOP 鉴权
- `ddd-model/auth/{OauthUserId,OauthUserInjection,OauthUserInjectionValidator,CurrentUserProvider}.java`(新)
- `ddd-action/auth/CurrentUserProviderImpl.java`(新)
- `ddd-model/req/BaseReq.java` — 标 `@OauthUserInjection`
- `biz-store-model/enums/RoleLimit.java`(删) → biz-store-action 4 controller 改 import 全局版

## 验证

- ddd-action + downstream 编译 exit=0
- 单测 6/6 过(GlobalExceptionHandlerTest 3 + OauthUserInjectionValidatorTest 3)
- 约定落 `SPEC.md`「项目约定」节

## 遗留(非本任务引入)

- `CodeReq.java`/`VerificationCodeReq.java` 缺 `SmsEnum` — 会话开始前既存 WIP(git status 标 M),非本次改动,超范围未动。
