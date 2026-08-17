package com.newzkl.platform.base.biz.order.action.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.model.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordItemQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordQuery;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;

import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.function.Function;

/**
 * 交易-结算单
 * @author fang
 */
@RestController
@RequestMapping("/sale/settle")
public class SettleController {

    @Autowired
    private SettleDomain settleDomain;

    /**
     * 结算单分页
     * @param settleRecordQuery
     * @return
     */
    @PostMapping("settleRecordPage")
    public PlatformResult<Page<SettleRecordVO>> settlePage(@RequestBody SettleRecordQuery settleRecordQuery) {
        if(RoleEnum.CompanyRole.SUPPLIER == SecurityUtils.getRole()){
            settleRecordQuery.setSupplierId(SecurityUtils.getAccountId());
        }
        Page<SettleRecordVO> settleRecordVOPageInfo = settleDomain.settleRecordVOList(settleRecordQuery);
        return PlatformResult.success(settleRecordVOPageInfo);
    }
    /**
     * 结算单详情列表
     */
    @PostMapping("settleRecordItemPage")
    public PlatformResult<Page<SettleRecordItemVO>> settleRecordItemPage(@Validated @RequestBody SettleRecordItemQuery settleRecordItemQuery) {
        return PlatformResult.success(settleDomain.settleRecordItemPage(settleRecordItemQuery));
    }
    /**
     * 结算类型明细
     * @Param idObj 结算单ID
     * @return
     */
    @PostMapping("settleTypeList")
    public PlatformResult<List<SettleOrderWaitVO>> settleTypeList(@Validated @RequestBody SettleTypeListReq settleTypeList) {
        return PlatformResult.success(settleDomain.settleTypeList(settleTypeList));
    }
    /**
     * 结算单VO
     * @param idObj
     * @return
     */
    @PostMapping("settleRecordVO")
    public PlatformResult<SettleRecordVO> settleRecordVO(@RequestBody IdCommand idObj) {
        return PlatformResult.success(settleDomain.settleRecordVO(idObj.getId()));
    }
    /**
     * 结算类型明细导出
     */
    @PostMapping("exportSettleTypeList")
    public void exportSpuOrder(HttpServletResponse response, @Validated @RequestBody SettleTypeListReq settleTypeList) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("结算类型明细", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<SettleOrderWaitVO> exportSettleTypeList = settleDomain.settleTypeList(settleTypeList);
        exportSpuOrderVO(response, settleTypeList, exportSettleTypeList);
    }

    private  void exportSpuOrderVO(HttpServletResponse response, SettleTypeListReq listReq, List<SettleOrderWaitVO> voList) throws IOException {
        if(listReq.getType() == 0){
            List<SettleGoodsExcelVO> goodsExcelVOList = TransferUtils.transfers(voList, c -> {
                SettleGoodsExcelVO v = new SettleGoodsExcelVO();
                v.setSpuOrderId(String.valueOf(c.getSpuOrderId()));
                v.setSpuName(c.getSpuName() + "( " + BizUtil.goodsSkuName(c.getSkuName()) + " * " + c.getSkuCount() + " )");
                v.setOrderMoney(BizUtil.excelMoney(c.getOrderMoney()));
                v.setC4("已结算");
                v.setC5(BizUtil.excelMoney(c.getOrderMoney()));
                return v;
            });
            EasyExcel.write(response.getOutputStream(), SettleGoodsExcelVO.class).sheet("模板").doWrite(goodsExcelVOList);
        }else if(listReq.getType() == 1){
            List<SettleFreightExcelVO> refundExcelVOList = TransferUtils.transfers(voList, new Function<SettleOrderWaitVO, SettleFreightExcelVO>() {
                @Override
                public SettleFreightExcelVO apply(SettleOrderWaitVO settleOrderWaitVO) {
                    SettleFreightExcelVO settleFreightExcelVO = new SettleFreightExcelVO();
                    settleFreightExcelVO.setSpuOrderId(String.valueOf(settleOrderWaitVO.getSpuOrderId()));
                    settleFreightExcelVO.setOrderMoney(BizUtil.excelMoney(settleOrderWaitVO.getOrderMoney()));
                    return settleFreightExcelVO;
                }
            });
            EasyExcel.write(response.getOutputStream(), SettleFreightExcelVO.class).sheet("模板").doWrite(refundExcelVOList);
        }else if(listReq.getType() == 2){
            List<SettleRefundExcelVO> freightExcelVOList = TransferUtils.transfers(voList, new Function<SettleOrderWaitVO, SettleRefundExcelVO>() {
                @Override
                public SettleRefundExcelVO apply(SettleOrderWaitVO settleOrderWaitVO) {
                    SettleRefundExcelVO settleRefundExcelVO = new SettleRefundExcelVO();
                    settleRefundExcelVO.setRefundId(String.valueOf(settleOrderWaitVO.getRefundId()));
                    settleRefundExcelVO.setSpuOrderId(String.valueOf(settleOrderWaitVO.getSpuOrderId()));
                    settleRefundExcelVO.setOrderMoney(BizUtil.excelMoney(settleOrderWaitVO.getOrderMoney()));
                    settleRefundExcelVO.setC4(BizUtil.excelMoney(Money.ZERO.subtract(settleOrderWaitVO.getOrderMoney())));
                    return settleRefundExcelVO;
                }
            });
            EasyExcel.write(response.getOutputStream(), SettleRefundExcelVO.class).sheet("模板").doWrite(freightExcelVOList);
        }else {

        }
    }
}
