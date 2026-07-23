package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.ConsumeEarningDataRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.EarningRecordDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.EarningRecordDO;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.PackOrderRpcVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.TotalEarningVO;
import com.newzkl.platform.base.biz.finance.model.event.SkuOrderWaitEarningVO;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.finance.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.finance.model.enums.RedisEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author niu
 * @description:
 * @date 2023/12/20 17:20
 */
@Repository
@RequiredArgsConstructor
public class ConsumeEarningDataRepositoryImpl implements ConsumeEarningDataRepository {

    private final EarningRecordDAO earningRecordDAO;

    private final AccountApi accountFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEarningRecord(List<EarningRecordVO> earningInfos) {
        List<EarningRecordDO> list = TransferUtils.transfers(earningInfos, EarningRecordDO.class);
        earningRecordDAO.insertOrUpdate(list);
    }

    @Override
    public List<EarningRecordVO> queryEarningRecord(EarningRecordQuery query) {
        LambdaQueryWrapper<EarningRecordDO> queryWrapper = earningRecordDAO.getLw(query)
                .orderByDesc(EarningRecordDO::getEarningTime);
        Page<EarningRecordDO> pageList = earningRecordDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transfers(pageList.getRecords(), EarningRecordVO.class);
    }

    @Override
    public List<AppEarningRecordRes> queryAppEarningRecord(EarningRecordQuery query) {
        LambdaQueryWrapper<EarningRecordDO> queryWrapper = earningRecordDAO.getLw(query)
                .orderByDesc(EarningRecordDO::getEarningTime);
        Page<EarningRecordDO> pageList = earningRecordDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transfers(pageList.getRecords(), AppEarningRecordRes::new);
    }

    @Override
    public Integer queryAccountWaitEarningAmount(EarningRecordQuery query) {
        Long accountId = query.getAccountId();

        String key = RedisEnum.Key.WAIT_EARNING_AMOUNT.getCode(accountId);
        Integer amount = RedisUtil.get(key);
        if (amount == null) {
            query.setState(EarningsEnum.State.SETTLE);

            LambdaQueryWrapper<EarningRecordDO> queryWrapper = earningRecordDAO.getLw(query)
                    .orderByDesc(EarningRecordDO::getEarningTime)
                    .select(EarningRecordDO::getAmount);
            List<EarningRecordDO> earningInfos = earningRecordDAO.selectList(queryWrapper);

            // TODO 做个groupBy
            amount = earningInfos.stream().mapToInt(EarningRecordDO::getAmount).sum();

            RedisUtil.set(key, amount, 5L, TimeUnit.MINUTES);
        }
        return amount;
    }

    @Override
    public TotalEarningVO queryTotalEarning() {
        TotalEarningVO totalEarningVO = RedisUtil.get(RedisEnum.Key.TOTAL_EARNING_AMOUNT.getCode());
        if (totalEarningVO != null) {
            return totalEarningVO;
        }
        totalEarningVO = new TotalEarningVO();

        totalEarningVO.setTotalEarning(earningRecordDAO.queryTotalEarning(1));
        totalEarningVO.setWaitEarning(earningRecordDAO.queryTotalEarning(0));
        RedisUtil.set(RedisEnum.Key.TOTAL_EARNING_AMOUNT.getCode(), totalEarningVO, 5L, TimeUnit.HOURS);
        return totalEarningVO;
    }

    @Override
    public PackOrderRpcVO queryPickPackOrder(Long orderNo) {
        return null;
    }

    @Override
    public List<PackOrderRpcVO> queryPickPackOrder(List<Long> orderNo) {
        return Collections.emptyList();
    }

    @Override
    public UpIdRes upId(CommonEnum.Client client, Long accountId) {
        return accountFacade.upId(client, accountId);
    }

    @Override
    public void sendMessage(SkuOrderWaitEarningVO skuOrderWaitEarningVO) {
//        MQUtil.send(MQUtil.getDefaultTopic(), MQUtil.getDefaultTag(), null, skuOrderWaitEarningVO, null);
    }

    @Override
    public void alterEarningRecordState(Long skuOrderId, Integer state) {
        earningRecordDAO.alterEarningRecordState(skuOrderId, state);
        RedisUtil.hDel(RedisEnum.Key.TOTAL_EARNING_AMOUNT.getCode());
    }

    @Override
    public Integer updateEarningRecord(EarningRecordVO earningInfoVO, EarningRecordQuery req) {
        return earningRecordDAO.update(null, earningRecordDAO.getLw(req));
    }
}
