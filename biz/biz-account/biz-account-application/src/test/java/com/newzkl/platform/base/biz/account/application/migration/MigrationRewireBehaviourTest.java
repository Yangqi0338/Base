package com.newzkl.platform.base.biz.account.application.migration;

import com.newzkl.platform.base.biz.account.model.auth.req.AccountCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import com.newzkl.platform.base.biz.account.model.req.web.DealerProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.OperatorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.req.web.SelectorProxySaveReq;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Task 0 迁移改写点的行为锁定测试
 *
 * <p>Task 0 中若干旧写法在新结构下无法编译, 被等价改写。本测试锁定这些改写的
 * <b>语义等价性</b>, 防止后续 Task 1-7 建控制器时被无意破坏。</p>
 *
 * <p>纯 JUnit 5 单测: 不启动 Spring 容器, 不连数据库。</p>
 *
 * @author KC
 */
@DisplayName("Task 0 迁移改写点行为锁定")
class MigrationRewireBehaviourTest {

    /**
     * 旧: {@code AbsRolePolicy#doRegisterAccount(username, "147852")} (protected, 同对象内调用)
     * 新: 身份策略需跨对象调用账号策略, 故提取为静态工厂 {@code AccountCustomSaveReq#proxyRegister}。
     * 锁定点: 硬编码验证码 "147852" 必须原样保留, 否则代理注册会被验证码校验拦截。
     */
    @Test
    @DisplayName("代理注册参数工厂: 保留旧硬编码验证码 147852")
    void proxyRegisterKeepsLegacyCode() {
        AccountCustomSaveReq req = AccountCustomSaveReq.proxyRegister("13000000000");

        assertEquals("13000000000", req.getUsername(), "用户名应透传");
        assertEquals("147852", req.getCode(), "代理注册免验证码的魔法值必须与旧实现一致");
    }

    /**
     * 旧: {@code JSONObject.parseObject(registerCommand, DealerProxySaveReq.class)} (入参为 String JSON)。
     * 新: 入参改为强类型 {@code IdentityProxySaveReq}, 用 TransferUtils 做同名属性拷贝。
     * 锁定点: 同名字段必须全部拷贝到位, 否则代理注册会静默丢字段。
     */
    @Test
    @DisplayName("交易师代理注册: 强类型入参转换等价于旧 JSON 反序列化")
    void dealerProxyReqTransferCopiesSameNameFields() {
        IdentityProxySaveReq source = new IdentityProxySaveReq();
        source.setUsername("13000000000");
        source.setPassword("pwd123");
        source.setName("张三");
        source.setYqm("YQM001");
        source.setServiceRate(0.15D);

        DealerProxySaveReq target = TransferUtils.transfer(source, DealerProxySaveReq::new);

        assertNotNull(target);
        assertEquals("13000000000", target.getUsername());
        assertEquals("pwd123", target.getPassword());
        assertEquals("张三", target.getName());
        assertEquals("YQM001", target.getYqm());
        assertEquals(0.15D, target.getServiceRate());
    }

    @Test
    @DisplayName("甄选师代理注册: 强类型入参转换等价于旧 JSON 反序列化")
    void selectorProxyReqTransferCopiesSameNameFields() {
        IdentityProxySaveReq source = new IdentityProxySaveReq();
        source.setUsername("13000000001");
        source.setPassword("pwd456");
        source.setName("李四");
        source.setPhone("13000000001");
        source.setYqm("YQM002");

        SelectorProxySaveReq target = TransferUtils.transfer(source, SelectorProxySaveReq::new);

        assertNotNull(target);
        assertEquals("13000000001", target.getUsername());
        assertEquals("pwd456", target.getPassword());
        assertEquals("李四", target.getName());
        assertEquals("13000000001", target.getPhone());
        assertEquals("YQM002", target.getYqm());
    }

    @Test
    @DisplayName("运营商代理注册: 强类型入参转换等价于旧 JSON 反序列化")
    void operatorProxyReqTransferCopiesSameNameFields() {
        IdentityProxySaveReq source = new IdentityProxySaveReq();
        source.setUsername("13000000002");
        source.setPassword("pwd789");
        source.setName("王五");
        source.setPhone("13000000002");
        source.setRegisterDomain("shop.example.com");

        OperatorProxySaveReq target = TransferUtils.transfer(source, OperatorProxySaveReq::new);

        assertNotNull(target);
        assertEquals("13000000002", target.getUsername());
        assertEquals("pwd789", target.getPassword());
        assertEquals("王五", target.getName());
        assertEquals("13000000002", target.getPhone());
        // registerDomain 与 OperatorProxySaveReq#domain 不同名, 旧 JSON 反序列化同样不会命中,
        // 故此处 domain 保持为 null 属行为等价, 不是回归。
        assertEquals(null, target.getDomain(), "异名字段旧实现也不映射, 保持 null");
        assertEquals(null, target.getType(), "未赋值的枚举字段应为 null");
    }

    @Test
    @DisplayName("运营商代理注册: 品牌类型枚举可正常参与分支判断")
    void operatorTypeEnumSurvivesTransfer() {
        OperatorProxySaveReq req = new OperatorProxySaveReq();
        req.setType(OperatorEnum.Type.BRAND);
        assertTrue(OperatorEnum.Type.BRAND.equals(req.getType()),
                "品牌类型分支是开店逻辑的开关, 必须可判定");
    }

    /**
     * 旧: {@code BizUtil.generateCode(32)} 生成 openapi 开发者密钥。
     * 新: 该方法在通用层拆分时被误删, Task 0 已按原实现补回。
     * 锁定点: 必须支持 length &gt; 16 (兄弟方法 generateDiffCode 基于 MD5 16 位摘要, 32 位会越界)。
     */
    @Test
    @DisplayName("随机码工具: generateCode 支持 32 位, 且字符集为数字+小写字母")
    void generateCodeSupportsThirtyTwoLength() {
        String secret = BizUtil.generateCode(32);

        assertNotNull(secret);
        assertEquals(32, secret.length(), "openapi 密钥长度必须为 32");
        assertTrue(secret.matches("[a-z0-9]{32}"), "字符集应为数字与小写字母: " + secret);
    }

    @Test
    @DisplayName("随机码工具: generateCode 每次调用结果不同")
    void generateCodeIsRandom() {
        assertFalse(BizUtil.generateCode(32).equals(BizUtil.generateCode(32)),
                "两次生成的密钥重复概率极低, 相同视为实现退化");
    }

    /**
     * 旧: {@code BizUtil.findClientRoleIdList(client)} (通用层)。
     * 新: 角色语义随业务枚举下沉, 改调 {@code RoleEnumUtil.findClientRoleIdList(client)}。
     * 锁定点: 端 -> 角色码列表的映射必须仍然可用且非空。
     */
    @Test
    @DisplayName("角色工具下沉: 按端查角色码列表仍可用")
    void roleEnumUtilResolvesClientRoleIds() {
        List<Long> roleIdList = RoleEnumUtil.findClientRoleIdList(CommonEnum.Client.OPERATOR);

        assertNotNull(roleIdList, "角色码列表恒非 null");
        assertFalse(roleIdList.isEmpty(), "运营商端应至少有一个角色");
    }

    /**
     * 旧: {@code SecurityUtils.getRole()} 直接返回业务枚举。
     * 新: 通用层仅保留 {@code getRoleId()}, 业务层做 码 -&gt; 枚举 转换。
     * 锁定点: 转换必须往返一致, 否则登录态角色判断会全线失效。
     */
    @Test
    @DisplayName("角色码转枚举: getByCode 与枚举 code 往返一致")
    void roleCodeRoundTripsToEnum() {
        RoleEnum.CompanyRole dealer = RoleEnum.CompanyRole.DEALER;
        assertEquals(dealer, RoleEnum.CompanyRole.getByCode(dealer.getCode()));

        RoleEnum.CompanyRole channel = RoleEnum.CompanyRole.CHANNEL;
        assertEquals(channel, RoleEnum.CompanyRole.getByCode(channel.getCode()));

        RoleEnum.CompanyRole operator = RoleEnum.CompanyRole.OPERATOR;
        assertEquals(operator, RoleEnum.CompanyRole.getByCode(operator.getCode()));
    }
}
