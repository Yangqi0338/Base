package com.newzkl.platform.base.biz.store.application.service.impl;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.store.application.service.SeatPackageService;
import com.newzkl.platform.base.biz.store.domain.adapt.api.AccountPurseReq;
import com.newzkl.platform.base.biz.store.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.PurseApi;
import com.newzkl.platform.base.biz.store.domain.adapt.api.PurseAmountRes;
import com.newzkl.platform.base.biz.store.domain.store.repository.SeatPackageRepository;
import com.newzkl.platform.base.biz.store.model.enums.DictEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.FinanceEnum;
import com.newzkl.platform.base.biz.store.model.store.req.SeatPackagePageReq;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageChannelRes;
import com.newzkl.platform.base.biz.store.model.store.res.SeatPackageResponse;
import com.newzkl.platform.base.biz.store.model.store.vo.ChannelConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 席位套餐查询服务实现
 *
 * @author muc_fang
 */
@Service
@RequiredArgsConstructor
public class SeatPackageServiceImpl implements SeatPackageService {

    private final SeatPackageRepository repository;
    private final PurseApi purseApi;
    private final DictApi dictApi;

    @Override
    public SeatPackageChannelRes seatPackageStoreVO(Long accountId) {
        SeatPackageChannelRes channelVO = new SeatPackageChannelRes();

        SeatPackagePageReq seatPackagePageReq = new SeatPackagePageReq();
        seatPackagePageReq.setState(1);
        List<SeatPackageResponse> seatPackageList = repository.seatPackageList(seatPackagePageReq);
        channelVO.setSeatPackageList(seatPackageList);

        // 查询渠道商钱包席位
        AccountPurseReq accountPurseReq = new AccountPurseReq();
        accountPurseReq.setAccountId(accountId);
        accountPurseReq.setAccountType(FinanceEnum.FinanceUser.CHANNEL.getType());
        accountPurseReq.setPurseType(FinanceEnum.PurseType.GOODS_SEAT.getType());

        List<PurseAmountRes> goodsSeatList = purseApi.queryPurse(accountPurseReq);
        int usedSeatNum = 0;
        int totalSeatNum = 0;
        for (PurseAmountRes it : goodsSeatList) {
            usedSeatNum += (it.getTotalEarnings() - it.getEarnings());
            totalSeatNum += it.getTotalEarnings();
        }
        channelVO.setUsedSeatNum(usedSeatNum);
        channelVO.setTotalSeatNum(totalSeatNum);

        // 席位原价 / 最低购买数量 取自字典配置
        ChannelConfigVO channelConfigVO = JSONUtil.toBean(dictApi.get(DictEnum.Key.CHANNEL_CONFIG.getCode()), ChannelConfigVO.class);
        channelVO.setSeatOriginalPrice(channelConfigVO.getSeatOriginalPrice());
        channelVO.setPurchaseMinimumNum(channelConfigVO.getPurchaseMinimumNum());
        return channelVO;
    }
}
