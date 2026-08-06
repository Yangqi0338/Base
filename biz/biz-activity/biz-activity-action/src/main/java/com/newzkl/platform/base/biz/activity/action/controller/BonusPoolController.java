package com.newzkl.platform.base.biz.activity.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.application.bonus.draw.BonusInterface;
import com.newzkl.platform.base.biz.activity.application.bonus.draw.BonusPoolAction;
import com.newzkl.platform.base.biz.activity.model.bonus.req.AlterCustomBonusReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ConfirmSettleReq;
import com.newzkl.platform.base.biz.activity.model.event.req.QueryBonusDetailsReq;
import com.newzkl.platform.base.biz.activity.model.event.req.SettleDetailReq;
import com.newzkl.platform.base.biz.activity.model.event.req.UpdateDividendReq;
import com.newzkl.platform.base.biz.activity.model.event.res.BonusDetailsRes;
import com.newzkl.platform.base.biz.activity.model.event.res.HistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.res.SettleHistoryBonusPoolReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.HistoryBonusPoolDataVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleDetailDataListVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleDetailDataVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleHistoryBonusPoolDataListExcelVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleHistoryBonusPoolDataListVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.SettleHistoryBonusPoolDataVO;
import com.newzkl.platform.base.common.core.office.EasyExcelUtil;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 奖金池操作接口
 *
 * @author niu
 */
@RestController
@RequestMapping("/bonusPool")
@RequiredArgsConstructor
public class BonusPoolController {

    private final BonusPoolAction bonusPoolAction;

    private final BonusInterface bonusInterface;

    /**
     * 查询历史奖金池
     *
     * @param req 历史奖金池查询请求
     * @return 历史奖金池分页
     */
    @PostMapping("/queryHistory")
    public PlatformResult<Page<HistoryBonusPoolDataVO>> queryHistory(@RequestBody HistoryBonusPoolReq req) {
        return bonusPoolAction.queryHistory(req);
    }

    /**
     * 查询分红结算单历史
     *
     * @param req 结算历史查询请求
     * @return 结算历史数据
     */
    @GetMapping("/settlePageList")
    public PlatformResult<SettleHistoryBonusPoolDataVO> querySettleHistory(@ModelAttribute SettleHistoryBonusPoolReq req) {
        return PlatformResult.success(bonusPoolAction.querySettleHistory(req));
    }

    /**
     * 确认结算
     *
     * @param req 确认结算请求
     * @return 处理结果
     */
    @PostMapping("/confirmSettle")
    public PlatformResult<Object> confirmSettle(@RequestBody ConfirmSettleReq req) {
        bonusPoolAction.confirmSettle(req);
        return PlatformResult.success();
    }

    /**
     * 删除结算
     *
     * @param req 确认结算请求
     * @return 处理结果
     */
    @PostMapping("/deleteSettle")
    public PlatformResult<Object> deleteSettle(@RequestBody ConfirmSettleReq req) {
        bonusPoolAction.deleteSettle(req);
        return PlatformResult.success();
    }

    /**
     * 结算单详情
     *
     * @param req 结算明细查询请求
     * @return 结算明细数据
     */
    @GetMapping("/settleDetail")
    public PlatformResult<SettleDetailDataVO> settleDetail(@ModelAttribute SettleDetailReq req) {
        return PlatformResult.success(bonusPoolAction.settleDetail(req));
    }

    /**
     * 更新实际分红比例和分红金额
     *
     * @param req 更新分红请求
     * @return 处理结果
     */
    @PostMapping("/updateDividend")
    public PlatformResult<Object> updateDividend(@RequestBody UpdateDividendReq req) {
        bonusPoolAction.updateDividend(req);
        return PlatformResult.success();
    }

    /**
     * 查询奖金池详情
     *
     * @param req 奖金池详情查询请求
     * @return 奖金池详情
     */
    @PostMapping("/queryBonusDetails")
    public PlatformResult<BonusDetailsRes> queryBonusDetails(@RequestBody QueryBonusDetailsReq req) {
        return bonusPoolAction.queryBonusDetails(req);
    }

    /**
     * 作废当前奖金池
     *
     * @return 处理结果
     */
    @PostMapping("/invalidNowBonusPool")
    public PlatformResult<Object> invalidNowBonusPool() {
        bonusPoolAction.invalidNowBonusPool();
        return PlatformResult.success();
    }

    /**
     * 更新自定义奖金
     *
     * @param req 自定义奖金请求
     * @return 处理结果
     */
    @PostMapping("/alterCustomBonus")
    public PlatformResult<Object> alterCustomBonus(@RequestBody AlterCustomBonusReq req) {
        bonusInterface.alterCustomBonus(req);
        return PlatformResult.success();
    }

    // TODO[#178-limit]: 原 scm 两导出端点带 @Limit(code=FuncCons.order..., level=get) 权限拦截,
    // Base 尚未迁 Limit 注解 + 切面(属 #178 RoleLimit/Limit 拦截切面迁移)。恢复后补回。已登 deferred-issues。

    /**
     * 导出分红记录
     *
     * @param req 结算明细查询请求
     * @throws IOException 导出流异常
     */
    @PostMapping("/exportDividendRecord")
    public void exportDividendRecord(SettleDetailReq req) throws IOException {
        List<SettleDetailDataListVO> list = bonusPoolAction.exportSelect(req);
        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-dividend";
        EasyExcelUtil.export(list, name);
    }

    /**
     * 导出结算记录
     *
     * @param req 结算历史查询请求
     * @throws IOException 导出流异常
     */
    @PostMapping("/exportSettleRecord")
    public void exportSettleRecord(SettleHistoryBonusPoolReq req) throws IOException {
        List<SettleHistoryBonusPoolDataListVO> list = bonusPoolAction.exportSettleRecord(req);
        List<SettleHistoryBonusPoolDataListExcelVO> exportList = new ArrayList<>();
        for (SettleHistoryBonusPoolDataListVO element : list) {
            SettleHistoryBonusPoolDataListExcelVO excelVO = new SettleHistoryBonusPoolDataListExcelVO();
            excelVO.setBonusPoolId(element.getBonusPoolId());
            excelVO.setActivityId(element.getActivityId());
            excelVO.setActivityName(element.getActivityName());
            excelVO.setSettlementId(element.getSettlementId());
            excelVO.setSettlementName(element.getSettlementName());
            excelVO.setOrderCount(Optional.ofNullable(element.getOrderInfoObject())
                    .map(obj -> obj.get("orderCount"))
                    .map(Object::toString)
                    .map(Integer::new)
                    .orElse(null));
            excelVO.setTotalOrderAmount(Optional.ofNullable(element.getOrderInfoObject())
                    .map(obj -> obj.get("totalOrderAmount"))
                    .map(Object::toString)
                    .map(BigDecimal::new)
                    .orElse(null));
            excelVO.setEstimatedAmount(Optional.ofNullable(element.getEstimatedDividendObject())
                    .map(obj -> obj.get("estimatedAmount"))
                    .map(Object::toString)
                    .map(BigDecimal::new)
                    .orElse(null));
            excelVO.setEstimatedPercent(Optional.ofNullable(element.getEstimatedDividendObject())
                    .map(obj -> obj.get("estimatedPercent"))
                    .map(Object::toString)
                    .orElse(null));
            excelVO.setActualPercent(Optional.ofNullable(element.getActualDividendObject())
                    .map(obj -> obj.get("actualPercent"))
                    .map(Object::toString)
                    .orElse(null));
            excelVO.setActualAmount(Optional.ofNullable(element.getActualDividendObject())
                    .map(obj -> obj.get("actualAmount"))
                    .map(Object::toString)
                    .map(BigDecimal::new)
                    .orElse(null));
            excelVO.setDealer(Optional.ofNullable(element.getPoolParticipant())
                    .map(obj -> obj.get("dealer"))
                    .map(Object::toString)
                    .map(Integer::new)
                    .orElse(null));
            excelVO.setOperator(Optional.ofNullable(element.getPoolParticipant())
                    .map(obj -> obj.get("operator"))
                    .map(Object::toString)
                    .map(Integer::new)
                    .orElse(null));
            excelVO.setSelector(Optional.ofNullable(element.getPoolParticipant())
                    .map(obj -> obj.get("selector"))
                    .map(Object::toString)
                    .map(Integer::new)
                    .orElse(null));
            excelVO.setDividendMethod(element.getDividendMethodDesc());
            excelVO.setDividendCycle(element.getDividendCycleDesc());
            excelVO.setConfirmTime(element.getConfirmTime());
            excelVO.setState(element.getStateDesc());
            exportList.add(excelVO);
        }
        String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-settle";
        EasyExcelUtil.export(exportList, name);
    }
}
