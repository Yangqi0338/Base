package com.newzkl.platform.base.biz.goods.application.goods.service.virtual.impl;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.goods.application.goods.service.virtual.SeatPackageService;
import com.newzkl.platform.base.biz.goods.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.goods.domain.adapt.api.PurseAmountRes;
import com.newzkl.platform.base.biz.goods.domain.adapt.api.PurseApi;
import com.newzkl.platform.base.biz.goods.domain.virtual.repository.SeatPackageRepository;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtual.SeatPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageChannelRes;
import com.newzkl.platform.base.biz.goods.model.goods.res.virtual.SeatPackageRes;
import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
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

        SeatPackageQuery seatPackagePageReq = new SeatPackageQuery();
        seatPackagePageReq.setState(1);
        List<SeatPackageRes> seatPackageList = repository.seatPackageList(seatPackagePageReq);
        channelVO.setSeatPackageList(seatPackageList);

        // 查询渠道商钱包席位
        AccountPurseReq accountPurseReq = new AccountPurseReq();
        accountPurseReq.setAccountId(accountId);
        accountPurseReq.setAccountType(PurseEnum.User.CHANNEL.getCode());
        accountPurseReq.setPurseType(PurseEnum.Type.GOODS_SEAT.getCode());

        List<PurseAmountRes> goodsSeatList = purseApi.queryPurse(accountPurseReq);
        int usedSeatNum = 0;
        int totalSeatNum = 0;
        for (PurseAmountRes it : goodsSeatList) {
            usedSeatNum += (it.getTotalAmount() - it.getAmount());
            totalSeatNum += it.getTotalAmount();
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
