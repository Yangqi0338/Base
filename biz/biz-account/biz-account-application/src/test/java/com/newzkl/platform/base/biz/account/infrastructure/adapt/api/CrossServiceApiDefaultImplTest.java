package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;
import com.newzkl.platform.base.common.ddd.facade.ChargeConfigChannelReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.DeveloperInitReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinanceConfigApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.IncomeQuery;
import com.newzkl.platform.base.common.ddd.facade.InitFinanceReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.OpenapiDeveloperApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.SmsApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreAccountCreateReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreRegisterReq;
import com.newzkl.platform.base.biz.account.model.support.CodeReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 跨域出站端口默认兜底实现的契约测试
 *
 * <p>Task 0 把 7 个跨域调用点从直连 Dubbo facade 改为出站端口 + 兜底实现。
 * 兜底实现的契约是: <b>永不抛异常</b>, 且集合返回值 <b>恒非 null</b> (空集合而非 null),
 * 以保证入口 starter 尚未注入真实 consumer 时账户域仍可编排通过。</p>
 *
 * <p>本测试为纯 JUnit 5 单测: 不启动 Spring 容器, 不连数据库, 直接 new 出实现类。</p>
 *
 * @author KC
 */
@DisplayName("跨域出站端口兜底实现契约")
class CrossServiceApiDefaultImplTest {

    @Test
    @DisplayName("DictApi 兜底: 取字典值恒返回 null 且不抛异常")
    void dictApiReturnsNull() {
        DictApi api = new DictApiImpl();
        assertNull(api.get(1001L), "兜底字典应返回 null");
        assertNull(api.get(null), "入参为 null 时兜底字典仍不应抛异常");
    }

    @Test
    @DisplayName("FinanceConfigApi 兜底: 写操作空实现, 查服务费返回 null")
    void financeConfigApiIsNeutral() {
        FinanceConfigApi api = new FinanceConfigApiImpl();
        assertDoesNotThrow(() -> api.saveChannelChargeConfig(new ChargeConfigChannelReq()));
        assertDoesNotThrow(() -> api.saveChannelChargeConfig(null));
        assertNull(api.queryChannelNowServiceFee(5501L), "兜底服务费查询应返回 null");
    }


    @Test
    @DisplayName("FinancePurseApi 兜底: 初始化钱包空实现, 查钱包返回空 List")
    void financePurseApiReturnsEmptyList() {
        FinancePurseApi api = new FinancePurseApiDefaultImpl();
        assertDoesNotThrow(() -> api.initFinance(new InitFinanceReq()));
        assertDoesNotThrow(() -> api.initFinance(null));

        assertNotNull(api.queryPurse(new AccountPurseReq()), "钱包查询恒非 null");
        assertTrue(api.queryPurse(new AccountPurseReq()).isEmpty(), "兜底钱包查询应为空 List");
    }

    @Test
    @DisplayName("GoodsStoreApi 兜底: 查门店返回 null, 开店/建关联空实现")
    void goodsStoreApiIsNeutral() {
        GoodsStoreApi api = new GoodsStoreApiDefaultImpl();
        assertNull(api.storeByChannelId(5501L), "兜底门店查询应返回 null");
        assertDoesNotThrow(() -> api.createStoreAccount(new StoreAccountCreateReq()));
        assertDoesNotThrow(() -> api.openStore(new StoreRegisterReq(5501L)));
        assertDoesNotThrow(() -> api.openStore(null));
    }

    @Test
    @DisplayName("OpenapiDeveloperApi 兜底: 初始化开发者为空实现")
    void openapiDeveloperApiIsNoop() {
        OpenapiDeveloperApi api = new OpenapiDeveloperApiDefaultImpl();
        assertDoesNotThrow(() -> api.initDeveloper(new DeveloperInitReq()));
        assertDoesNotThrow(() -> api.initDeveloper(null));
    }

    @Test
    @DisplayName("SmsApi 兜底: 发送验证码仅记日志, 不抛异常")
    void smsApiIsNoop() {
        SmsApi api = new SmsApiDefaultImpl();
        CodeReq codeReq = new CodeReq();
        codeReq.setPhone("13000000000");
        assertDoesNotThrow(() -> api.sendCode(codeReq));
        assertDoesNotThrow(() -> api.sendCode(null));
    }
}
