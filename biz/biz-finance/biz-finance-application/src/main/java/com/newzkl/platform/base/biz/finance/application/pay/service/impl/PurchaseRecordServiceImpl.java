package com.newzkl.platform.base.biz.finance.application.pay.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.PurchaseRecordService;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PurchaseRecordDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.SeatPackageOrderInfo;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.FinanceErrorCode;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 席位购买记录编排实现
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseRecordServiceImpl implements PurchaseRecordService {

    private final PurchaseRecordDomain purchaseRecordService;
    private final AccountPurseDomain accountPurseService;

    @Override
    public boolean seatPackageSaveOrUpdate(PurchaseRecordReq saveCommand) {
        String tradeNo = saveCommand.getTradeNo();

        if (StrUtil.isNotBlank(tradeNo)) {
            PurchaseRecordQuery query = new PurchaseRecordQuery();
            query.setTradeNo(tradeNo);
            query.setType(PurseEnum.PurchaseRecordType.SEAT_PACKAGE);
            query.resetQuerySingle();
            PurchaseRecordVO purchaseRecordVO = purchaseRecordService.queryPage(query).getRecords()
                    .stream().findFirst().orElseThrow(() -> new PlatformException(FinanceErrorCode.NOT_EXISTS));

            TransferUtils.transfer(purchaseRecordVO, saveCommand, CopyOptions.create().setOverride(false));
            saveCommand.setId(purchaseRecordVO.getId());
        }

        Long accountId = saveCommand.getAccountId();
        Money payAmount = saveCommand.getPayAmount();
        SeatPackageOrderInfo orderInfo = JSONUtil.toBean(saveCommand.getOrderInfo(), SeatPackageOrderInfo.class);
        Integer purchaseNum = orderInfo.getPurchaseNum();
        PaymentEnum.PayType payMode = saveCommand.getPayType();
        if (purchaseNum == null || purchaseNum <= 0) {
            throw new PlatformException(FinanceErrorCode.STATE_ERROR);
        }

        // 若是采购金抵扣
        if (PaymentEnum.PayType.PURCHASE == payMode) {
            // 减少采购金额度
            AccountPurseAlterRecordReq recordReq = buildChannelOperatorPurseAlterRecord(accountId, saveCommand.getId(), payAmount);
            boolean subSuccess = accountPurseService.subAmount(recordReq);
            if (!subSuccess) {
                // 采购金不充足
                log.info("channelPurchaseGoodsSeat - 采购不充足");
                throw new PlatformException(FinanceErrorCode.AMOUNT_LESS);
            }
        }

        AccountPurseAlterRecordReq recordReq = buildChannelGoodsSeatAlterRecord(accountId, saveCommand.getId(), purchaseNum);
        accountPurseService.addAmount(recordReq);

        // 生成购买记录
        if (saveCommand.getId() != null) {
            purchaseRecordService.edit(saveCommand);
            return true;
        } else {
            return purchaseRecordService.add(saveCommand) != null;
        }

    }

    private AccountPurseAlterRecordReq buildChannelOperatorPurseAlterRecord(Long accountId, Long joinRecordId, Money totalFee) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(accountId);
        req.setPurseType(PurseEnum.Type.PURCHASE);
        req.setAccountType(PurseEnum.User.CHANNEL);
        req.setAlterType(PurseEnum.AlterType.GOODS_POSITION_BUY);
        req.setAmount(totalFee);
        req.setJoinRecordId(joinRecordId);
        return req;
    }

    /**
     * 供应商商品位变动记录
     */
    private AccountPurseAlterRecordReq buildChannelGoodsSeatAlterRecord(Long accountId, Long joinRecordId, Integer num) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(accountId);
        req.setPurseType(PurseEnum.Type.GOODS_SEAT);
        req.setAccountType(PurseEnum.User.CHANNEL);
        req.setAlterType(PurseEnum.AlterType.GOODS_POSITION_BUY);
        // num 为席位数量, 存入 Money 型 amount 字段 (按分数值等值存放)
        req.setAmount(Money.of(num));
        req.setJoinRecordId(joinRecordId);
        return req;
    }
}
