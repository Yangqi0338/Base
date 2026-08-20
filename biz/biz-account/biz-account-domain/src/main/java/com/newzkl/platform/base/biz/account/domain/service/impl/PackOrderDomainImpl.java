package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.PackOrderRepository;
import com.newzkl.platform.base.biz.account.domain.pack.entity.PackOrder;
import com.newzkl.platform.base.biz.account.domain.service.PackOrderDomain;
import com.newzkl.platform.base.common.ddd.model.enums.account.PackEnum;
import com.newzkl.platform.base.biz.account.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.account.model.pack.req.PackOrderDeliverCommand;
import com.newzkl.platform.base.biz.account.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.biz.account.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 入会礼包订单领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packorder.service.impl.PackOrderDomainImpl}。
 * 状态机与发货逻辑照源保留，异常换 {@code PlatformException}，状态用 {@code PackOrderStateEnum}。</p>
 *
 * @author KC
 */
@Service("packOrderDomainImpl")
@RequiredArgsConstructor
public class PackOrderDomainImpl implements PackOrderDomain {

    private final PackOrderRepository packOrderRepository;

    @Override
    public PackOrder packOrderCreate(PackOrderCommand command, ShipAddressRes shipAddressVO, List<PackGoodsRes> packGoodsList) {
        PackOrder packOrder = TransferUtils.transfer(command, PackOrder::new);
        packOrder.init(command, shipAddressVO, packGoodsList);
        return packOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void packOrderCreate(PackOrder packOrder) {
        packOrderRepository.packOrderCreate(packOrder);
    }

    @Override
    public PackOrderRes packOrderVO(Long packOrderId) {
        PackOrderRes res = packOrderRepository.packOrderVO(packOrderId);
        if (res == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "礼包订单");
        }
        return res;
    }

    @Override
    public Page<PackOrderRes> packOrderVOList(PackOrderQuery query) {
        return packOrderRepository.packOrderVOList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void packOrderDeliver(PackOrderDeliverCommand command) {
        PackOrder packOrder = packOrderRepository.packOrder(command.getPackOrderId());
        if (packOrder == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "礼包订单");
        }
        Integer state = packOrder.getState();
        if (Objects.equals(PackEnum.PackOrderStateEnum.SUCCESS.getCode(), state)
                || Objects.equals(PackEnum.PackOrderStateEnum.DOWN_RECEIVE.getCode(), state)
                || Objects.equals(PackEnum.PackOrderStateEnum.WAIT_RECEIVE.getCode(), state)) {
            packOrder.setFreightCompany(command.getFreightCompany());
            packOrder.setFreightCode(command.getFreightCode());
        } else {
            packOrder.packOrderDeliver(command);
        }
        packOrderRepository.packOrderSave(packOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(Long orderId) {
        packOrderRepository.updateState(Collections.singletonList(orderId),
                PackEnum.PackOrderStateEnum.WAIT_PAY.getCode(), PackEnum.PackOrderStateEnum.WAIT_DELIVERY.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payTimeOut(List<Long> idList) {
        packOrderRepository.updateState(idList,
                PackEnum.PackOrderStateEnum.WAIT_PAY.getCode(), PackEnum.PackOrderStateEnum.CLOSE.getCode());
    }

    @Override
    public List<Long> packOrderIdList(PackOrderQuery query) {
        return packOrderRepository.packOrderIdList(query);
    }
}
