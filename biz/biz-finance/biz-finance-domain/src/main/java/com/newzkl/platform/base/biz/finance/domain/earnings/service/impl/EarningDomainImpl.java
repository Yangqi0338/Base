package com.newzkl.platform.base.biz.finance.domain.earnings.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.ConsumeEarningDataRepository;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningDomain;
import com.newzkl.platform.base.biz.finance.model.assembler.EarningRecordAssembler;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.*;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description: 分润记录查询实现
 * @date 2023/12/23 11:08
 */
@Service
@RequiredArgsConstructor
public class EarningDomainImpl implements EarningDomain {

    private final ConsumeEarningDataRepository consumeEarningDataRepository;
    private final EarningRecordAssembler recordAssembler;

    @Override
    public List<EarningRecordVO> queryEarningRecord(EarningRecordQuery query) {
        // 若传入了角色或消费类型且没有传入分润类型，则进行特殊处理
        List<RoleEnum.CompanyRole> roleList = query.getRoleList();
        List<EarningsEnum.ConsumeType> consumeTypeList = query.getConsumeTypeList();
        if (CollUtil.isNotEmpty(roleList) || CollUtil.isNotEmpty(consumeTypeList)) {
            if (CollUtil.isEmpty(query.getEarningTypeList())) {
                List<EarningsEnum.EarningType> earningTypeList = Arrays.stream(EarningsEnum.EarningType.values()).filter(
                        it -> CollUtil.contains(roleList, it.getUser().getRole())
                                || CollUtil.contains(consumeTypeList, it.getConsumeType())
                ).collect(Collectors.toList());
                query.setEarningTypeList(earningTypeList);
            }
        }
        return consumeEarningDataRepository.queryEarningRecord(query);
    }

    @Override
    public Integer queryAccountGoodsWaitEarningAmount(EarningRecordQuery query) {
        query.setConsumeType(EarningsEnum.ConsumeType.GOODS);
        return consumeEarningDataRepository.queryAccountWaitEarningAmount(query);
    }

    @Override
    public void alterEarningRecordState(Long skuOrderId, Integer state) {
        consumeEarningDataRepository.alterEarningRecordState(skuOrderId, state);
    }

    @Override
    public TotalEarningVO queryTotalEarning() {
        return consumeEarningDataRepository.queryTotalEarning();
    }

    @Override
    public List<AppEarningRecordRes> queryAppEarningRecord(EarningRecordQuery req) {
        List<EarningRecordVO> pageList = consumeEarningDataRepository.queryEarningRecord(req);
        // 转换为AppEarningInfoVO
        return TransferUtils.transfers(pageList, (earningInfo -> {
            // 基础信息覆盖
            AppEarningRecordRes appEarningInfoVO = recordAssembler.vo2AppVO(earningInfo);
            String goodsInfo = earningInfo.getGoodsInfo();
            if (EarningsEnum.ConsumeType.PICK_PACK == earningInfo.getEarningType().getConsumeType()) {
                appEarningInfoVO.setPackInfoVO(JSONUtil.toBean(goodsInfo, PickPackInfoVO.class));
            } else if (EarningsEnum.ConsumeType.DIVIDEND_BONUS == earningInfo.getEarningType().getConsumeType()) {
                appEarningInfoVO.setAwardInfoVO(JSONUtil.toBean(goodsInfo, AwardInfoVO.class));
            } else {
                appEarningInfoVO.setGoodsInfo(JSONUtil.toBean(goodsInfo, GoodsInfoVO.class));
            }
            return appEarningInfoVO;
        }));
    }

    @Override
    public Integer updateEarningRecord(EarningRecordVO earningInfoVO, EarningRecordQuery req) {
        return consumeEarningDataRepository.updateEarningRecord(earningInfoVO, req);
    }
}
