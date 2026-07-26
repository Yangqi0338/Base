package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.RoleRepository;
import com.newzkl.platform.base.biz.account.model.assembler.RoleAssembler;
import com.newzkl.platform.base.biz.account.model.req.RoleQuery;
import com.newzkl.platform.base.biz.account.model.role.req.RoleReq;
import com.newzkl.platform.base.biz.account.model.role.res.RoleRes;
import com.newzkl.platform.base.biz.account.model.role.vo.RoleVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link RoleDomainImpl} 行为测试。
 *
 * <p>纯 JUnit 5 + Mockito, 不启动 Spring 容器, 不连数据库。</p>
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("角色领域服务")
class RoleDomainImplTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleDomainImpl roleDomain;

    /**
     * 以真实转换语义替代 MapStruct 生成实现, 避免测试依赖注解处理器产物。
     */
    private final RoleAssembler assembler = new RoleAssembler() {
        @Override
        public RoleVO req2VO(RoleReq entity) {
            return TransferUtils.transfer(entity, RoleVO::new);
        }

        @Override
        public List<RoleVO> req2VO(List<RoleReq> entity) {
            return TransferUtils.transfers(entity, RoleVO::new);
        }

        @Override
        public RoleRes vo2Res(RoleVO it) {
            return TransferUtils.transfer(it, RoleRes::new);
        }
    };

    @BeforeEach
    void setUp() {
        roleDomain = new RoleDomainImpl(roleRepository, assembler);
    }

    @Test
    @DisplayName("保存: 领域层生成雪花主键, 资料组以强类型透传")
    void saveShouldAssignSnowflakeId() {
        when(roleRepository.save(any())).thenReturn(1L);
        RoleReq req = new RoleReq();
        req.setName("供应商");
        req.setDataGroupIdList(List.of(11L, 22L));

        roleDomain.save(req);

        ArgumentCaptor<RoleVO> captor = ArgumentCaptor.forClass(RoleVO.class);
        verify(roleRepository).save(captor.capture());
        assertNotNull(captor.getValue().getId(), "主键应由领域层生成");
        assertEquals("供应商", captor.getValue().getName());
        assertEquals(List.of(11L, 22L), captor.getValue().getDataGroupIdList());
    }

    @Test
    @DisplayName("修改: 以入参 id 为更新目标, 忽略请求体内的 id")
    void editShouldUseGivenId() {
        when(roleRepository.edit(any())).thenReturn(1);
        RoleReq req = new RoleReq();
        req.setId(111L);

        roleDomain.edit(222L, req);

        ArgumentCaptor<RoleVO> captor = ArgumentCaptor.forClass(RoleVO.class);
        verify(roleRepository).edit(captor.capture());
        assertEquals(222L, captor.getValue().getId());
    }

    @Test
    @DisplayName("列表: pageNo 大于 0 时走分页查询")
    void listShouldPageWhenPageNoPositive() {
        RoleVO vo = new RoleVO();
        vo.setName("渠道商");
        Page<RoleVO> page = new Page<>();
        page.setRecords(List.of(vo));
        when(roleRepository.pageList(any())).thenReturn(page);
        RoleQuery query = new RoleQuery();
        query.setPageNo(1);
        query.setPageSize(10);

        List<RoleRes> result = roleDomain.list(query);

        assertEquals("渠道商", result.get(0).getName());
        verify(roleRepository, never()).list(any());
    }

    @Test
    @DisplayName("列表: 保留旧语义 —— pageNo 为 0 时整表返回, 不分页")
    void listShouldNotPageWhenPageNoZero() {
        RoleVO vo = new RoleVO();
        vo.setName("运营商");
        when(roleRepository.list(any())).thenReturn(List.of(vo));

        List<RoleRes> result = roleDomain.list(new RoleQuery());

        assertEquals(1, result.size());
        verify(roleRepository, never()).pageList(any());
    }

    @Test
    @DisplayName("列表: 无数据返回空集合而不是 null")
    void listShouldNeverReturnNull() {
        when(roleRepository.list(any())).thenReturn(null);

        assertTrue(roleDomain.list(new RoleQuery()).isEmpty());
    }

    @Test
    @DisplayName("分页: 出参逐条转换")
    void pageListShouldMapRecords() {
        RoleVO vo = new RoleVO();
        vo.setId(9L);
        vo.setTotalUserNum(3);
        Page<RoleVO> page = new Page<>();
        page.setRecords(List.of(vo));
        when(roleRepository.pageList(any())).thenReturn(page);

        Page<RoleRes> result = roleDomain.pageList(new RoleQuery());

        assertEquals(3, result.getRecords().get(0).getTotalUserNum());
    }

    @Test
    @DisplayName("详情: 仓储无数据时出参为 null")
    void detailShouldTolerateAbsent() {
        when(roleRepository.detail(1L)).thenReturn(null);

        assertEquals(null, roleDomain.detail(1L));
    }

    @Test
    @DisplayName("计数: 透传角色 ID 给仓储自增")
    void addCountShouldDelegate() {
        when(roleRepository.addCount(5L)).thenReturn(1);

        assertEquals(1, roleDomain.addCount(5L));
        verify(roleRepository).addCount(5L);
    }
}
