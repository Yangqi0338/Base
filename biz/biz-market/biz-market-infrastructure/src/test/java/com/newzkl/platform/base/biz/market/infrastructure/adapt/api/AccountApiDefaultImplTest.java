package com.newzkl.platform.base.biz.market.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.market.domain.adapt.api.UpIdRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code AccountApiDefaultImpl} 兜底行为测试
 *
 * @author KC
 */
class AccountApiDefaultImplTest {

    private final AccountApiDefaultImpl accountApi = new AccountApiDefaultImpl();

    @Test
    @DisplayName("兜底 upId: 返回非 null 空对象, oneId 为 null")
    void upIdReturnsEmptyResult() {
        UpIdRes res = accountApi.upId(1L);

        assertThat(res).isNotNull();
        assertThat(res.getOneId()).isNull();
    }

    @Test
    @DisplayName("兜底 upId: accountId 为 null 也不抛异常")
    void upIdTolerateNullAccountId() {
        assertThat(accountApi.upId(null)).isNotNull();
    }
}
