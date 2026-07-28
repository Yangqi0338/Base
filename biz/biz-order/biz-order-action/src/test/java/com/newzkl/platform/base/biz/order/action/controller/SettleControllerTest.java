package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordEditReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordItemPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordItemVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordVO;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link SettleController} 端点与导出分支测试。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("结算单控制器")
class SettleControllerTest {

    @Mock
    private SettleDomain settleDomain;

    @InjectMocks
    private SettleController settleController;

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    /**
     * 构造一条待结算订单 (导出用)。
     *
     * @return 待结算订单
     */
    private SettleOrderWaitVO waitVO() {
        SettleOrderWaitVO vo = new SettleOrderWaitVO();
        vo.setSpuOrderNo("SPU202607260001");
        vo.setSpuName("测试商品");
        vo.setSkuName("[{\"name\":\"规格\",\"value\":\"红色\"}]");
        vo.setSkuCount(2);
        vo.setOrderMoney(12345);
        return vo;
    }

    /**
     * 捕获导出字节的响应桩。
     *
     * @param buffer 字节缓冲
     * @return 响应桩
     * @throws IOException 不会抛出
     */
    private HttpServletResponse responseWriting(ByteArrayOutputStream buffer) throws IOException {
        HttpServletResponse response = org.mockito.Mockito.mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                // 测试桩不需要异步写监听
            }

            @Override
            public void write(int b) {
                buffer.write(b);
            }
        });
        return response;
    }

    @Test
    @DisplayName("分页: 供应商登录态自动收窄 supplierId")
    void settlePageShouldNarrowForSupplier() {
        SecurityContextHolder.set(TokenConstants.ROLE, RoleEnum.CompanyRole.SUPPLIER.getCode().toString());
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, "7001");
        when(settleDomain.settleRecordVOList(org.mockito.ArgumentMatchers.any())).thenReturn(new Page<>());

        settleController.settlePage(new SettleRecordPageReq());

        ArgumentCaptor<SettleRecordPageReq> captor = ArgumentCaptor.forClass(SettleRecordPageReq.class);
        verify(settleDomain).settleRecordVOList(captor.capture());
        assertEquals(7001L, captor.getValue().getSupplierId());
    }

    @Test
    @DisplayName("分页: 平台登录态不注入 supplierId")
    void settlePageShouldNotNarrowForPlatform() {
        SecurityContextHolder.set(TokenConstants.ROLE, RoleEnum.CompanyRole.PLATFORM.getCode().toString());
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, "1");
        when(settleDomain.settleRecordVOList(org.mockito.ArgumentMatchers.any())).thenReturn(new Page<>());

        settleController.settlePage(new SettleRecordPageReq());

        ArgumentCaptor<SettleRecordPageReq> captor = ArgumentCaptor.forClass(SettleRecordPageReq.class);
        verify(settleDomain).settleRecordVOList(captor.capture());
        assertNull(captor.getValue().getSupplierId());
    }

    @Test
    @DisplayName("明细分页: 透传查询并返回 Page")
    void settleRecordItemPageShouldDelegate() {
        Page<SettleRecordItemVO> page = new Page<>(1, 10);
        page.setTotal(3);
        SettleRecordItemPageReq req = new SettleRecordItemPageReq();
        when(settleDomain.settleRecordItemPage(req)).thenReturn(page);

        assertEquals(3, settleController.settleRecordItemPage(req).getData().getTotal());
    }

    @Test
    @DisplayName("修改: 透传编辑入参")
    void editSettleRecordShouldDelegate() {
        SettleRecordEditReq req = new SettleRecordEditReq();
        req.setId(12L);
        req.setLabel("标签");

        assertTrue(settleController.editSettleRecord(req).getSuccess());
        verify(settleDomain).editSettleRecord(req);
    }

    @Test
    @DisplayName("结算类型明细: 透传查询")
    void settleTypeListShouldDelegate() {
        SettleTypeListReq req = new SettleTypeListReq();
        req.setSettleRecordId(13L);
        req.setType(0);
        when(settleDomain.settleTypeList(req)).thenReturn(List.of(waitVO()));

        assertEquals(1, settleController.settleTypeList(req).getData().size());
    }

    @Test
    @DisplayName("结算单VO: 透传命令体 ID")
    void settleRecordVOShouldPassId() {
        when(settleDomain.settleRecordVO(anyLong())).thenReturn(new SettleRecordVO());
        OrderCmd.ID idObj = new OrderCmd.ID();
        idObj.setId(14L);

        settleController.settleRecordVO(idObj);

        verify(settleDomain).settleRecordVO(14L);
    }

    @Test
    @DisplayName("导出: 商品类型写出 xlsx 并设置下载头")
    void exportGoodsShouldWriteExcel() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        HttpServletResponse response = responseWriting(buffer);
        SettleTypeListReq req = new SettleTypeListReq();
        req.setSettleRecordId(15L);
        req.setType(0);
        when(settleDomain.settleTypeList(req)).thenReturn(List.of(waitVO()));

        settleController.exportSettleTypeList(response, req);

        verify(response).setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        verify(response).setHeader(org.mockito.ArgumentMatchers.eq("Content-disposition"),
                org.mockito.ArgumentMatchers.contains(".xlsx"));
        assertTrue(buffer.size() > 0);
    }

    @Test
    @DisplayName("导出: 运费类型写出 xlsx")
    void exportFreightShouldWriteExcel() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        SettleTypeListReq req = new SettleTypeListReq();
        req.setSettleRecordId(16L);
        req.setType(1);
        when(settleDomain.settleTypeList(req)).thenReturn(List.of(waitVO()));

        settleController.exportSettleTypeList(responseWriting(buffer), req);

        assertTrue(buffer.size() > 0);
    }

    @Test
    @DisplayName("导出: 售后冲正类型写出 xlsx")
    void exportRefundShouldWriteExcel() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        SettleTypeListReq req = new SettleTypeListReq();
        req.setSettleRecordId(17L);
        req.setType(2);
        SettleOrderWaitVO vo = waitVO();
        vo.setRefundId(999L);
        when(settleDomain.settleTypeList(req)).thenReturn(List.of(vo));

        settleController.exportSettleTypeList(responseWriting(buffer), req);

        assertTrue(buffer.size() > 0);
    }

    @Test
    @DisplayName("导出: 未知类型不写任何内容 (旧语义空分支)")
    void exportUnknownTypeShouldWriteNothing() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        SettleTypeListReq req = new SettleTypeListReq();
        req.setSettleRecordId(18L);
        req.setType(9);
        when(settleDomain.settleTypeList(req)).thenReturn(List.of(waitVO()));

        settleController.exportSettleTypeList(responseWriting(buffer), req);

        assertEquals(0, buffer.size());
    }
}
