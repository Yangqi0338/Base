package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.refund;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.order.SkuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.order.SpuOrderDAO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.refund.RefundDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundDO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.SkuOrderDO;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuRefundDTO;
import com.newzkl.platform.base.biz.order.model.req.query.RefundQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.SpuRefundRes;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiRefundStateEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.NotifyUtil;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEnums;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEventCommand;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
* 售后单
* @author fang
*/
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefundRepositoryImpl extends RepositorySupport implements RefundRepository {

    private final RefundDAO refundDAO;
    private final SkuOrderDAO skuOrderDAO;
    private final SpuOrderDAO spuOrderDAO;

    @Override
    public Long refundSave(RefundDTO refund) {
        refundDAO.insert(TransferUtils.transfer(refund, RefundDO.class));
        return refund.getId();
    }

    @Override
    public int refundUpdate(RefundDTO refund) {
        return refundDAO.updateById(TransferUtils.transfer(refund, RefundDO.class));
    }

    @Override
    public RefundDTO refund(Long refundId) {
        RefundDO refund = refundDAO.selectById(refundId);
        return TransferUtils.transfer(refund, RefundDTO.class);
    }

    @Override
    public RefundDTO refundBySpuOrderId(Long spuOrderId) {
        RefundQuery query = RefundQuery.builder()
                .spuOrderId(spuOrderId)
                .build();
        BaseLambdaQueryWrapper<RefundDO> queryWrapper = refundDAO.getLw(query);
        queryWrapper.orderByDesc(RefundDO::getId);
        RefundDO refund = getOne(refundDAO, queryWrapper);
        return TransferUtils.transfer(refund, RefundDTO.class);
    }

    @Override
    public Page<RefundDTO> refundPage(RefundQuery refundQuery) {
        Page<RefundDO> refundPage = refundDAO.selectPage(RepositorySupport.page(refundQuery), refundDAO.getLw(refundQuery));
        return TransferUtils.transferPage(refundPage, RefundDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(RefundDTO refundEdit, Long refundId, OrderEnum.OrderType orderType, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId) {
        RefundQuery query = new RefundQuery();
        query.setId(refundId);
        query.setRefundState(sourceState);
        int i = refundDAO.update(refundDAO.getLw(query).toUpdate()
                .append(RefundDO::getRefundStateLog, toState)
                .notEmptySet(RefundDO::getRefuseReason, refundEdit.getRefuseReason())
                .notEmptySet(RefundDO::getStateTime, refundEdit.getStateTime())
                .notEmptySet(RefundDO::getOutRefundId, refundEdit.getOutRefundId())
                .notEmptySet(RefundDO::getStoreAutoTime, refundEdit.getStoreAutoTime())
                .notEmptySet(RefundDO::getFreightExt, refundEdit.getFreightExt())
                .set(RefundDO::getRefundState, toState)
        );
        if(i > 0) {
            refundStateNotify(orderType, channelId, refundId, sourceState, toState);
        }
    }

    @Override
    public void updateStateWithFrom(OrderEnum.OrderType orderType, Long refundId, RefundEnum.State sourceState, RefundEnum.State toState, Long channelId) {
        RefundQuery query = new RefundQuery();
        query.setId(refundId);
        query.setRefundState(sourceState);
        int i = refundDAO.update(refundDAO.getLw(query).toUpdate()
                .append(RefundDO::getRefundStateLog, toState)
                .set(RefundDO::getRefundState, toState)
                .set(RefundDO::getFromOrderState, sourceState)
        );
        if(i > 0){
            refundStateNotify(orderType, channelId, refundId, sourceState, toState);
        }

    }

    @Override
    public List<ApiRefundStateVO> accountRefundState(Long accountId, List<Long> refundIdList) {
        RefundQuery query = new RefundQuery();
        query.setChannelId(accountId);
        query.setIdList(refundIdList);
        return list(refundDAO,refundDAO.getLw(query), ApiRefundStateVO.class);
    }

    @Override
    public List<SkuRefundDTO> skuRefundResList(Long spuOrderId, List<Long> skuIds) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setSpuOrderId(spuOrderId);
        query.setSkuIdList(skuIds);
        return skuOrderDAO.skuRefundResList(skuOrderDAO.getLw(query).unwrap("t"));
    }

    @Override
    public List<SpuRefundRes> spuRefundResList(Long orderId, List<Long> spuIds) {
        SpuOrderQuery query = new SpuOrderQuery();
        query.setOrderId(orderId);
        query.setSpuIdList(spuIds);
        return spuOrderDAO.spuRefundResList(spuOrderDAO.getLw(query).unwrap("t"));
    }

    @Override
    public Page<RefundDTO> page(RefundQuery refundQuery) {
        Page<RefundDO> refundList = refundDAO.selectPage(RepositorySupport.page(refundQuery), refundDAO.getLw(refundQuery));
        return TransferUtils.transferPage(refundList, RefundDTO.class);
    }

    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(Long spuOrderId, Long spuId) {
        RefundQuery query = new RefundQuery();
        query.setSpuOrderId(spuOrderId);
        query.setSpuId(spuId);
        String outRefundAddress = findOneField(refundDAO, refundDAO.getLw(query), RefundDO::getOutRefundAddress);
        return JSONUtil.toBean(outRefundAddress, ApiRefundFreightAddressVO.class);
    }

    @Override
    public Integer countTotalRefunding(SpuOrderQuery spuOrderQuery) {
        RefundQuery query = RefundQuery.builder()
                .memberId(spuOrderQuery.getMemberId())
                .storeId(spuOrderQuery.getStoreId())
                .refundStateList(CollUtil.newArrayList(
                        RefundEnum.State.CHANNEL_WAIT, RefundEnum.State.SUPPLIER_WAIT,
                        RefundEnum.State.FREIGHT_WAIT, RefundEnum.State.RECEIVE_WAIT,
                        RefundEnum.State.PLATFORM_WAIT, RefundEnum.State.MONEY_ING
                )).build();
        return refundDAO.selectCount(refundDAO.getLw(query)).intValue();
    }

    public void refundStateNotify(OrderEnum.OrderType orderType, Long channelId, Long refundId, RefundEnum.State currentState, RefundEnum.State toState) {
        //开发者通知
        if(OrderEnum.OrderType.CHANNEL == orderType){
            NotifyEventCommand notifyEventCommand = new NotifyEventCommand();
            notifyEventCommand.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
            notifyEventCommand.setBusinessType(NotifyEnums.OrderType.REFUND_STATE.getCode());
            notifyEventCommand.setEventInfo(JSONObject.toJSONString(new ApiRefundStateEvent(refundId,
                    currentState == null ? null : currentState.getCode(),
                    toState == null ? null : toState.getCode())));
            NotifyUtil.batchSend(Collections.singletonList(channelId), notifyEventCommand);
        }
    }
}
