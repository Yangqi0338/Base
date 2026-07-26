package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.CdkRepository;
import com.newzkl.platform.base.biz.account.model.assembler.CdkAssembler;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkEditReq;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkReq;
import com.newzkl.platform.base.biz.account.model.cdk.req.ToCdkCommand;
import com.newzkl.platform.base.biz.account.model.cdk.res.CdkRes;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link CdkDomainImpl} 行为测试。
 *
 * <p>纯 JUnit 5 + Mockito, 不启动 Spring 容器, 不连数据库。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("开通码领域服务")
class CdkDomainImplTest {

    private static final Long OPERATOR_ID = 4401L;
    private static final Integer SYSTEM_TYPE = 0;

    @Mock
    private CdkRepository cdkRepository;

    private CdkDomainImpl cdkDomain;

    /**
     * 以真实转换语义替代 MapStruct 生成实现, 避免测试依赖注解处理器产物。
     */
    private final CdkAssembler assembler = new CdkAssembler() {
        @Override
        public CdkVO req2VO(CdkReq entity) {
            return TransferUtils.transfer(entity, CdkVO::new);
        }

        @Override
        public List<CdkVO> req2VO(List<CdkReq> entity) {
            return TransferUtils.transfers(entity, CdkVO::new);
        }

        @Override
        public CdkRes vo2Res(CdkVO it) {
            return TransferUtils.transfer(it, CdkRes::new);
        }
    };

    @BeforeEach
    void setUp() {
        cdkDomain = new CdkDomainImpl(cdkRepository, assembler);
    }

    @Test
    @DisplayName("保存: 领域层生成雪花主键")
    void saveShouldAssignSnowflakeId() {
        when(cdkRepository.save(any())).thenReturn(1L);
        CdkReq req = new CdkReq();
        req.setValue("abc123");

        cdkDomain.save(req);

        ArgumentCaptor<CdkVO> captor = ArgumentCaptor.forClass(CdkVO.class);
        verify(cdkRepository).save(captor.capture());
        assertNotNull(captor.getValue().getId(), "主键应由领域层生成");
        assertEquals("abc123", captor.getValue().getValue());
    }

    @Test
    @DisplayName("修改: 以入参 id 为更新目标")
    void editShouldUseGivenId() {
        when(cdkRepository.edit(any())).thenReturn(1);
        CdkReq req = new CdkReq();
        req.setId(111L);

        cdkDomain.edit(222L, req);

        ArgumentCaptor<CdkVO> captor = ArgumentCaptor.forClass(CdkVO.class);
        verify(cdkRepository).edit(captor.capture());
        assertEquals(222L, captor.getValue().getId());
    }

    @Test
    @DisplayName("ID 列表查询: 仓储返回 null 时给空集合")
    void idByQueryShouldNeverReturnNull() {
        when(cdkRepository.idByQuery(any())).thenReturn(null);

        assertTrue(cdkDomain.idByQuery(new CdkQuery()).isEmpty());
    }

    @Test
    @DisplayName("分页: 出参逐条转换")
    void pageListShouldMapRecords() {
        CdkVO vo = new CdkVO();
        vo.setId(9L);
        vo.setValue("abc123");
        Page<CdkVO> page = new Page<>();
        page.setRecords(List.of(vo));
        when(cdkRepository.pageList(any())).thenReturn(page);

        Page<CdkRes> result = cdkDomain.pageList(new CdkQuery());

        assertEquals("abc123", result.getRecords().get(0).getValue());
    }

    @Test
    @DisplayName("随机生成: 超过 1000 个抛参数异常")
    void randomCreateCdkShouldRejectOverLimit() {
        ScmException ex = assertThrows(ScmException.class,
                () -> cdkDomain.randomCreateCdk(OPERATOR_ID, 1001, SYSTEM_TYPE));

        assertTrue(ex.equalsCode(BaseErrorCode.PARAM));
        verify(cdkRepository, never()).saveBatch(any());
    }

    @Test
    @DisplayName("随机生成: 生成指定数量, 归属人角色为运营商")
    void randomCreateCdkShouldGenerateRequestedCount() {
        when(cdkRepository.existValue(anyInt(), anySet())).thenReturn(new HashSet<>());

        List<String> valueList = cdkDomain.randomCreateCdk(OPERATOR_ID, 3, SYSTEM_TYPE);

        assertEquals(3, valueList.size());
        ArgumentCaptor<List<CdkVO>> captor = ArgumentCaptor.forClass(List.class);
        verify(cdkRepository).saveBatch(captor.capture());
        List<CdkVO> saved = captor.getValue();
        assertEquals(3, saved.size());
        for (CdkVO cdk : saved) {
            assertNotNull(cdk.getId());
            assertEquals(OPERATOR_ID, cdk.getOperatorId());
            assertEquals(RoleEnum.CompanyRole.OPERATOR.getCode(), cdk.getBelowRole());
            assertEquals(SYSTEM_TYPE, cdk.getSystemType());
        }
    }

    @Test
    @DisplayName("随机生成: 命中库内重复值时递归补足")
    void randomCreateCdkShouldRetryOnCollision() {
        // 首轮把生成的值全部判为已存在, 迫使递归再生成
        when(cdkRepository.existValue(anyInt(), anySet()))
                .thenAnswer(invocation -> new HashSet<>((Set<String>) invocation.getArgument(1)))
                .thenReturn(new HashSet<>());

        List<String> valueList = cdkDomain.randomCreateCdk(OPERATOR_ID, 2, SYSTEM_TYPE);

        assertEquals(2, valueList.size());
    }

    @Test
    @DisplayName("直接生成: 空值列表返回空集合且不落库")
    void directCreateCdkShouldSkipEmptyList() {
        assertTrue(cdkDomain.directCreateCdk(OPERATOR_ID, SYSTEM_TYPE, new ArrayList<>()).isEmpty());

        verify(cdkRepository, never()).saveBatch(any());
    }

    @Test
    @DisplayName("直接生成: 按给定值逐条生成并返回 ID")
    void directCreateCdkShouldUseGivenValues() {
        List<Long> idList = cdkDomain.directCreateCdk(OPERATOR_ID, SYSTEM_TYPE, List.of("a", "b"));

        assertEquals(2, idList.size());
        ArgumentCaptor<List<CdkVO>> captor = ArgumentCaptor.forClass(List.class);
        verify(cdkRepository).saveBatch(captor.capture());
        assertEquals("a", captor.getValue().get(0).getValue());
        assertEquals("b", captor.getValue().get(1).getValue());
    }

    @Test
    @DisplayName("分配: 运营商给交易师, 分配状态 1 并记录交易师时间")
    void toCdkOperatorToDealer() {
        when(cdkRepository.editForToCdk(any(), any())).thenReturn(2);
        ToCdkCommand command = new ToCdkCommand();
        command.setFromRole(RoleEnum.CompanyRole.OPERATOR.getCode());
        command.setToRole(RoleEnum.CompanyRole.DEALER.getCode());
        command.setToUserId(5501L);
        command.setCdkIdList(List.of(1L, 2L));

        assertEquals(2, cdkDomain.toCdk(command));

        ArgumentCaptor<CdkEditReq> captor = ArgumentCaptor.forClass(CdkEditReq.class);
        verify(cdkRepository).editForToCdk(captor.capture(), any());
        CdkEditReq edit = captor.getValue();
        assertEquals(1, edit.getToState());
        assertEquals(RoleEnum.CompanyRole.DEALER.getCode(), edit.getBelowRole());
        assertEquals(5501L, edit.getDealerId());
        assertNotNull(edit.getToDealerTime());
    }

    @Test
    @DisplayName("分配: 交易师给渠道商, 分配状态 2")
    void toCdkDealerToChannel() {
        when(cdkRepository.editForToCdk(any(), any())).thenReturn(1);
        ToCdkCommand command = new ToCdkCommand();
        command.setFromRole(RoleEnum.CompanyRole.DEALER.getCode());
        command.setToRole(RoleEnum.CompanyRole.CHANNEL.getCode());
        command.setToUserId(6601L);
        command.setCdkIdList(List.of(1L));

        cdkDomain.toCdk(command);

        ArgumentCaptor<CdkEditReq> captor = ArgumentCaptor.forClass(CdkEditReq.class);
        verify(cdkRepository).editForToCdk(captor.capture(), any());
        assertEquals(2, captor.getValue().getToState());
        assertEquals(6601L, captor.getValue().getChannelId());
    }

    @Test
    @DisplayName("分配: 交易师不能分配给交易师")
    void toCdkDealerToDealerShouldFail() {
        ToCdkCommand command = new ToCdkCommand();
        command.setFromRole(RoleEnum.CompanyRole.DEALER.getCode());
        command.setToRole(RoleEnum.CompanyRole.DEALER.getCode());

        ScmException ex = assertThrows(ScmException.class, () -> cdkDomain.toCdk(command));

        assertTrue(ex.equalsCode(BaseErrorCode.PARAM));
        verify(cdkRepository, never()).editForToCdk(any(), any());
    }

    @Test
    @DisplayName("分配: 渠道商不能作为分配人")
    void toCdkFromChannelShouldFail() {
        ToCdkCommand command = new ToCdkCommand();
        command.setFromRole(RoleEnum.CompanyRole.CHANNEL.getCode());
        command.setToRole(RoleEnum.CompanyRole.CHANNEL.getCode());

        ScmException ex = assertThrows(ScmException.class, () -> cdkDomain.toCdk(command));

        assertTrue(ex.equalsCode(BaseErrorCode.PARAM));
    }

    @Test
    @DisplayName("状态修改: 用户已使用的开通码不能改回未使用")
    void cdkStateEditShouldRejectResetForUserUsed() {
        CdkVO cdk = new CdkVO();
        cdk.setId(1L);
        cdk.setUseType(0);
        when(cdkRepository.detail(1L)).thenReturn(cdk);

        ScmException ex = assertThrows(ScmException.class, () -> cdkDomain.cdkStateEdit(1L, 0));

        assertTrue(ex.equalsCode(BaseErrorCode.PARAM));
        verify(cdkRepository, never()).edit(any());
    }

    @Test
    @DisplayName("状态修改: 平台使用的开通码可改回未使用, 只写状态列")
    void cdkStateEditShouldAllowResetForPlatformUsed() {
        CdkVO cdk = new CdkVO();
        cdk.setId(1L);
        cdk.setUseType(1);
        when(cdkRepository.detail(1L)).thenReturn(cdk);

        cdkDomain.cdkStateEdit(1L, 0);

        ArgumentCaptor<CdkVO> captor = ArgumentCaptor.forClass(CdkVO.class);
        verify(cdkRepository).edit(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals(0, captor.getValue().getUseState());
    }

    @Test
    @DisplayName("状态修改: 开通码不存在抛记录不存在")
    void cdkStateEditShouldRejectAbsent() {
        when(cdkRepository.detail(1L)).thenReturn(null);

        ScmException ex = assertThrows(ScmException.class, () -> cdkDomain.cdkStateEdit(1L, 1));

        assertTrue(ex.equalsCode(BaseErrorCode.NODATA));
    }
}
