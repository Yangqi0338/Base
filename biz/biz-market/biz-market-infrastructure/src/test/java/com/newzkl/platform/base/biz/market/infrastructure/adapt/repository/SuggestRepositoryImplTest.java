package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.market.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.market.domain.adapt.api.UpIdRes;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketSuggestTagConfigDAO;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketSuggestTagDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketSuggestTagConfigDO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketSuggestTagDO;
import com.newzkl.platform.base.biz.market.model.suggest.req.CommitTagReq;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code SuggestRepositoryImpl} 单元测试 (DAO / 出站端口全 mock, 不连库)
 *
 * <p>静态门面 {@code SecurityUtils} 用 {@code mockStatic} 隔离。</p>
 *
 * @author KC
 */
class SuggestRepositoryImplTest {

    private static final Long ACCOUNT_ID = 1001L;

    private static final Long OPERATOR_ID = 99L;

    private MarketSuggestTagDAO marketSuggestTagDAO;

    private MarketSuggestTagConfigDAO marketSuggestTagConfigDAO;

    private AccountApi accountApi;

    private SuggestRepositoryImpl suggestRepository;

    private MockedStatic<SecurityUtils> securityUtils;

    @BeforeEach
    void setUp() {
        marketSuggestTagDAO = Mockito.mock(MarketSuggestTagDAO.class);
        marketSuggestTagConfigDAO = Mockito.mock(MarketSuggestTagConfigDAO.class);
        accountApi = Mockito.mock(AccountApi.class);
        suggestRepository = new SuggestRepositoryImpl(marketSuggestTagDAO, marketSuggestTagConfigDAO, accountApi);
        securityUtils = Mockito.mockStatic(SecurityUtils.class);
        securityUtils.when(SecurityUtils::getAccountId).thenReturn(ACCOUNT_ID);
        securityUtils.when(SecurityUtils::getUsername).thenReturn("13800000000");
    }

    @AfterEach
    void tearDown() {
        securityUtils.close();
    }

    @Test
    @DisplayName("saveTagConfig: 无 id 走新增并回填 accountId; nowTagSelect 保留旧行为取 tagConfigSelect")
    void saveTagConfigInsertWhenIdAbsent() {
        TagConfigReq req = new TagConfigReq();
        req.setTagConfig(Arrays.asList("A", "B"));
        req.setNowTag(List.of("C"));
        req.setTagConfigSelect(2);
        req.setNowTagSelect(7);
        req.setMinNum(1);

        suggestRepository.saveTagConfig(req);

        ArgumentCaptor<MarketSuggestTagConfigDO> captor = ArgumentCaptor.forClass(MarketSuggestTagConfigDO.class);
        verify(marketSuggestTagConfigDAO).insert(captor.capture());
        MarketSuggestTagConfigDO saved = captor.getValue();
        assertThat(saved.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(saved.getTagConfigSelect()).isEqualTo(2);
        // 旧行为: nowTagSelect 取 req.tagConfigSelect (2), 而非 req.nowTagSelect (7)
        assertThat(saved.getNowTagSelect()).isEqualTo(2);
        assertThat(saved.getTagConfig()).contains("A").contains("B");
        assertThat(saved.getNowTag()).contains("C");
        verify(marketSuggestTagConfigDAO, Mockito.never()).updateById(any(MarketSuggestTagConfigDO.class));
    }

    @Test
    @DisplayName("saveTagConfig: 带 id 走更新, 不改 accountId")
    void saveTagConfigUpdateWhenIdPresent() {
        TagConfigReq req = new TagConfigReq();
        req.setId(55L);
        req.setTagConfig(List.of("A"));

        suggestRepository.saveTagConfig(req);

        ArgumentCaptor<MarketSuggestTagConfigDO> captor = ArgumentCaptor.forClass(MarketSuggestTagConfigDO.class);
        verify(marketSuggestTagConfigDAO).updateById(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(55L);
        assertThat(captor.getValue().getAccountId()).isNull();
        verify(marketSuggestTagConfigDAO, Mockito.never()).insert(any(MarketSuggestTagConfigDO.class));
    }

    @Test
    @DisplayName("commitTag: opeartorId 取自 AccountApi 返回的上级运营商ID")
    void commitTagFillsOperatorIdFromAccountApi() {
        UpIdRes upIdRes = new UpIdRes();
        upIdRes.setOneId(OPERATOR_ID);
        when(accountApi.upId(ACCOUNT_ID)).thenReturn(upIdRes);
        CommitTagReq req = new CommitTagReq();
        req.setTagConfig(List.of("A"));
        req.setNowTag(List.of("B"));
        req.setRemark("备注");

        suggestRepository.commitTag(req);

        ArgumentCaptor<MarketSuggestTagDO> captor = ArgumentCaptor.forClass(MarketSuggestTagDO.class);
        verify(marketSuggestTagDAO).insert(captor.capture());
        MarketSuggestTagDO saved = captor.getValue();
        assertThat(saved.getOpeartorId()).isEqualTo(OPERATOR_ID);
        assertThat(saved.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(saved.getMobile()).isEqualTo("13800000000");
        assertThat(saved.getRemark()).isEqualTo("备注");
    }

    @Test
    @DisplayName("commitTag: 端口兜底返回空对象时 opeartorId 为 null")
    void commitTagTolerateEmptyUpId() {
        when(accountApi.upId(ACCOUNT_ID)).thenReturn(new UpIdRes());

        suggestRepository.commitTag(new CommitTagReq());

        ArgumentCaptor<MarketSuggestTagDO> captor = ArgumentCaptor.forClass(MarketSuggestTagDO.class);
        verify(marketSuggestTagDAO).insert(captor.capture());
        assertThat(captor.getValue().getOpeartorId()).isNull();
    }

    @Test
    @DisplayName("queryTagConfig(false): 交易师视角改用上级运营商ID查询")
    void queryTagConfigForTraderUsesUpperAccount() {
        UpIdRes upIdRes = new UpIdRes();
        upIdRes.setOneId(OPERATOR_ID);
        when(accountApi.upId(ACCOUNT_ID)).thenReturn(upIdRes);
        MarketSuggestTagConfigDO configDO = new MarketSuggestTagConfigDO();
        configDO.setId(7L);
        configDO.setTagConfig("[\"A\",\"B\"]");
        configDO.setNowTag(null);
        configDO.setTagConfigSelect(1);
        configDO.setNowTagSelect(1);
        configDO.setMinNum(3);
        when(marketSuggestTagConfigDAO.selectOne(any())).thenReturn(configDO);

        TagConfigVO vo = suggestRepository.queryTagConfig(false);

        verify(accountApi).upId(ACCOUNT_ID);
        assertThat(vo).isNotNull();
        assertThat(vo.getId()).isEqualTo(7L);
        assertThat(vo.getTagConfig()).containsExactly("A", "B");
        // 集合恒非空: JSON 为 null 时返回空集合
        assertThat(vo.getNowTag()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("queryTagConfig(true): 运营商视角不走出站端口")
    void queryTagConfigForOperatorSkipsAccountApi() {
        when(marketSuggestTagConfigDAO.selectOne(any())).thenReturn(null);

        assertThat(suggestRepository.queryTagConfig(true)).isNull();
        Mockito.verifyNoInteractions(accountApi);
    }

    @Test
    @DisplayName("queryCommitTag: 保留旧行为取列表最后一条 (create_time DESC 下即最早一条)")
    void queryCommitTagReturnsLastElement() {
        MarketSuggestTagDO newest = new MarketSuggestTagDO();
        newest.setId(2L);
        newest.setCreateTime(LocalDateTime.now());
        MarketSuggestTagDO oldest = new MarketSuggestTagDO();
        oldest.setId(1L);
        oldest.setCreateTime(LocalDateTime.now().minusDays(1));
        when(marketSuggestTagDAO.selectList(any())).thenReturn(Arrays.asList(newest, oldest));

        CommitTagVO vo = suggestRepository.queryCommitTag();

        assertThat(vo).isNotNull();
        assertThat(vo.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("queryCommitTag: 无记录返回 null")
    void queryCommitTagReturnsNullWhenEmpty() {
        when(marketSuggestTagDAO.selectList(any())).thenReturn(List.of());

        assertThat(suggestRepository.queryCommitTag()).isNull();
    }

    @Test
    @DisplayName("queryOperatorAllSuggest: 无记录返回空集合 (旧实现返回 null)")
    void queryOperatorAllSuggestReturnsEmptyList() {
        when(marketSuggestTagDAO.selectList(any())).thenReturn(null);

        assertThat(suggestRepository.queryOperatorAllSuggest("138")).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("queryOperatorAllSuggest: 逐条转视图")
    void queryOperatorAllSuggestMapsAllRows() {
        MarketSuggestTagDO tagDO = new MarketSuggestTagDO();
        tagDO.setId(3L);
        tagDO.setTagConfig("[\"X\"]");
        tagDO.setNowConfig("[\"Y\"]");
        tagDO.setRemark("r");
        tagDO.setState(1);
        tagDO.setMobile("13800000000");
        when(marketSuggestTagDAO.selectList(any())).thenReturn(List.of(tagDO));

        List<CommitTagVO> list = suggestRepository.queryOperatorAllSuggest(null);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getTagConfig()).containsExactly("X");
        assertThat(list.get(0).getNowTag()).containsExactly("Y");
        assertThat(list.get(0).getState()).isEqualTo(1);
    }
}
