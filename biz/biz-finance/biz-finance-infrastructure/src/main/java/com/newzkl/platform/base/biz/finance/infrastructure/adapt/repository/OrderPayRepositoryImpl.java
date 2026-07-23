package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.OrderPayRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.PaymentDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.PaymentDO;
import com.newzkl.platform.base.biz.finance.model.pay.req.PaymentQuery;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.pay.vo.OrderPayeeInfoVO;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PaymentVO;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2023/12/19 17:19
 */
@Repository
@RequiredArgsConstructor
public class OrderPayRepositoryImpl extends RepositorySupport implements OrderPayRepository {

    private final PaymentDAO paymentDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrderPayRecord(PaymentVO paymentVO, List<OrderPayeeInfoVO> payeeInfos) {
        // 若非json格式,强制为json格式
        PaymentDO payment = TransferUtils.transfer(paymentVO, PaymentDO::new);
        payment.setTradeNo(NumberUtil.parseLong(BusinessCodeUtil.generate(BusinessType.PAYMENT)));
        if (payeeInfos != null) {
            payment.setPayeeInfo(JSONUtil.toJsonStr(payeeInfos));
        }
        paymentDAO.insert(payment);
        return payment.getTradeNo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean alterPayState(Long tradeNo, String tripartiteTradeNo) {
        return paymentDAO.update(new LambdaUpdateWrapper<PaymentDO>()
                .set(PaymentDO::getPayState, 1)
                .set(PaymentDO::getTripartiteTradeNo, tripartiteTradeNo)
                .set(PaymentDO::getPayTime, LocalDateTime.now())
                .eq(PaymentDO::getTradeNo, tradeNo)
                .eq(PaymentDO::getPayState, 0)
        ) > 0;
    }

    @Override
    public TradeOrderInfoRes tradeOrderQuery(Long tradeNo) {
        PaymentDO payment = paymentDAO.selectById(tradeNo);
        return TransferUtils.transfer(payment, TradeOrderInfoRes::new);
    }

    @Override
    public List<PaymentVO> tradeOrderQuery(PaymentQuery query) {
        LambdaQueryWrapper<PaymentDO> queryWrapper = paymentDAO.getLw(query)
                .orderByDesc(PaymentDO::getId);
        Page<PaymentDO> pageList = paymentDAO.selectPage(RepositorySupport.page(query), queryWrapper);
        return TransferUtils.transfers(pageList.getRecords(), PaymentVO.class);
    }
}
