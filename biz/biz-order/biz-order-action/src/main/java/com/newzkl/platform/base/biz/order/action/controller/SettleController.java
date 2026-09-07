package com.newzkl.platform.base.biz.order.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.service.SettleDomain;
import com.newzkl.platform.base.biz.order.model.req.SettleTypeListReq;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordItemQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SettleRecordQuery;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        if(AccountEnum.Identity.SUPPLIER == SecurityUtils.getIdentity()){
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
}
