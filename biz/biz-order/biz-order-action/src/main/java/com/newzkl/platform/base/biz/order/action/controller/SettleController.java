package com.newzkl.platform.base.biz.order.action.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordEditReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordItemPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleFreightExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleGoodsExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleOrderWaitVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordDetailVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordItemVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRefundExcelVO;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 交易-结算单控制器。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.SettleController},
 * 7 端点路径与 HTTP 方法保持不变。偏离说明:</p>
 * <ul>
 *   <li>旧 controller 直连 {@code ISettleRepository} (跨层), 新版改注入 {@link SettleDomain}。</li>
 *   <li>分页出参由 {@code PageInfo} 改为 MyBatis-Plus {@code Page} (前端分页壳字段变化)。</li>
 *   <li>{@code SecurityUtils.getRole()} 已不存在, 改用 {@code getRoleId()}。</li>
 *   <li>旧 {@code @Limit(FuncCons...)} 权限点不在本层声明, 鉴权切面归入口 starter。</li>
 * </ul>
 *
 * @author KC
 */
@RestController
@RequestMapping("/sale/settle")
@RequiredArgsConstructor
public class SettleController {

    /**
     * 结算类型: 商品
     */
    private static final int TYPE_GOODS = 0;

    /**
     * 结算类型: 运费
     */
    private static final int TYPE_FREIGHT = 1;

    /**
     * 结算类型: 售后冲正
     */
    private static final int TYPE_REFUND = 2;

    private final SettleDomain settleDomain;

    /**
     * 结算单分页 (供应商登录态自动收窄到自身)。
     *
     * @param settleRecordQuery 结算单查询
     * @return 结算单分页
     */
    @PostMapping("settleRecordPage")
    public ScmResult<Page<SettleRecordVO>> settlePage(@RequestBody SettleRecordPageReq settleRecordQuery) {
        if (RoleEnum.CompanyRole.SUPPLIER.getCode().equals(SecurityUtils.getRoleId())) {
            settleRecordQuery.setSupplierId(SecurityUtils.getAccountId());
        }
        return ScmResult.success(settleDomain.settleRecordVOList(settleRecordQuery));
    }

    /**
     * 结算单详情 (含明细列表)。
     *
     * @param idObj 结算单 ID
     * @return 结算单详情
     */
    @PostMapping("settleRecordDetailVO")
    public ScmResult<SettleRecordDetailVO> settleRecordDetailVO(@RequestBody OrderCmd.ID idObj) {
        return ScmResult.success(settleDomain.settleRecordDetailVO(idObj.getId()));
    }

    /**
     * 结算单明细分页。
     *
     * @param settleRecordItemQuery 结算单明细查询
     * @return 结算单明细分页
     */
    @PostMapping("settleRecordItemPage")
    public ScmResult<Page<SettleRecordItemVO>> settleRecordItemPage(
            @Validated @RequestBody SettleRecordItemPageReq settleRecordItemQuery) {
        return ScmResult.success(settleDomain.settleRecordItemPage(settleRecordItemQuery));
    }

    /**
     * 修改结算单 (标签)。
     *
     * @param settleRecordEditReq 结算单修改入参
     * @return 成功结果
     */
    @PostMapping("editSettleRecord")
    public ScmResult<Void> editSettleRecord(@RequestBody SettleRecordEditReq settleRecordEditReq) {
        settleDomain.editSettleRecord(settleRecordEditReq);
        return ScmResult.success();
    }

    /**
     * 结算类型明细 (商品 / 运费 / 售后冲正)。
     *
     * @param settleTypeList 结算类型查询
     * @return 待结算订单列表
     */
    @PostMapping("settleTypeList")
    public ScmResult<List<SettleOrderWaitVO>> settleTypeList(@Validated @RequestBody SettleTypeListReq settleTypeList) {
        return ScmResult.success(settleDomain.settleTypeList(settleTypeList));
    }

    /**
     * 结算单视图对象。
     *
     * @param idObj 结算单 ID
     * @return 结算单视图对象
     */
    @PostMapping("settleRecordVO")
    public ScmResult<SettleRecordVO> settleRecordVO(@RequestBody OrderCmd.ID idObj) {
        return ScmResult.success(settleDomain.settleRecordVO(idObj.getId()));
    }

    /**
     * 结算类型明细导出 (按 type 走 3 套模板)。
     *
     * @param response       HTTP 响应 (直接写 xlsx 流)
     * @param settleTypeList 结算类型查询
     * @throws IOException 写出流失败
     */
    @PostMapping("exportSettleTypeList")
    public void exportSettleTypeList(HttpServletResponse response,
                                     @Validated @RequestBody SettleTypeListReq settleTypeList) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("结算类型明细", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        writeSettleTypeExcel(response, settleTypeList, settleDomain.settleTypeList(settleTypeList));
    }

    /**
     * 按结算类型写出对应模板的 Excel。
     *
     * @param response HTTP 响应
     * @param listReq  结算类型查询 (取 type 分支)
     * @param voList   待结算订单列表
     * @throws IOException 写出流失败
     */
    private void writeSettleTypeExcel(HttpServletResponse response, SettleTypeListReq listReq,
                                      List<SettleOrderWaitVO> voList) throws IOException {
        Integer type = listReq.getType();
        if (type == null) {
            return;
        }
        if (type == TYPE_GOODS) {
            List<SettleGoodsExcelVO> goodsExcelVOList = TransferUtils.transfers(voList, c -> {
                SettleGoodsExcelVO v = new SettleGoodsExcelVO();
                v.setSpuOrderId(c.getSpuOrderNo());
                v.setSpuName(c.getSpuName() + "( " + ScmUtil.goodsSkuName(c.getSkuName())
                        + " * " + c.getSkuCount() + " )");
                v.setOrderMoney(ScmUtil.excelMoney(c.getOrderMoney()));
                // 旧语义: 有售后且售后状态 1 记为异常, 结算总额置 0
                if (c.getRefundId() != null && Integer.valueOf(1).equals(c.getRefundState())) {
                    v.setC4("异常");
                    v.setC5("0.00");
                } else {
                    v.setC4("已结算");
                    v.setC5(ScmUtil.excelMoney(c.getOrderMoney()));
                }
                return v;
            });
            EasyExcel.write(response.getOutputStream(), SettleGoodsExcelVO.class).sheet("模板").doWrite(goodsExcelVOList);
        } else if (type == TYPE_FREIGHT) {
            List<SettleFreightExcelVO> freightExcelVOList = TransferUtils.transfers(voList, c -> {
                SettleFreightExcelVO v = new SettleFreightExcelVO();
                v.setSpuOrderId(c.getSpuOrderNo());
                v.setOrderMoney(ScmUtil.excelMoney(c.getOrderMoney()));
                return v;
            });
            EasyExcel.write(response.getOutputStream(), SettleFreightExcelVO.class).sheet("模板")
                    .doWrite(freightExcelVOList);
        } else if (type == TYPE_REFUND) {
            List<SettleRefundExcelVO> refundExcelVOList = TransferUtils.transfers(voList, c -> {
                SettleRefundExcelVO v = new SettleRefundExcelVO();
                v.setRefundId(String.valueOf(c.getRefundId()));
                v.setSpuOrderId(c.getSpuOrderNo());
                v.setOrderMoney(ScmUtil.excelMoney(c.getOrderMoney()));
                v.setC4(ScmUtil.excelMoney(0 - c.getOrderMoney()));
                return v;
            });
            EasyExcel.write(response.getOutputStream(), SettleRefundExcelVO.class).sheet("模板")
                    .doWrite(refundExcelVOList);
        }
    }
}
