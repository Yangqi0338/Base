package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.PackOrderRepository;
import com.newzkl.platform.base.biz.user.domain.pack.entity.PackOrder;
import com.newzkl.platform.base.biz.user.domain.service.PackOrderDomain;
import com.newzkl.platform.base.biz.user.model.pack.enums.PackOrderStateEnum;
import com.newzkl.platform.base.biz.user.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderDeliverCommand;
import com.newzkl.platform.base.biz.user.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.biz.user.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.biz.user.model.relation.vo.ShipAddressVO;
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
    public PackOrder packOrderCreate(PackOrderCommand command, ShipAddressVO shipAddressVO, List<PackGoodsRes> packGoodsList) {
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
        if (Objects.equals(PackOrderStateEnum.SUCCESS.getCode(), state)
                || Objects.equals(PackOrderStateEnum.DOWN_RECEIVE.getCode(), state)
                || Objects.equals(PackOrderStateEnum.WAIT_RECEIVE.getCode(), state)) {
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
                PackOrderStateEnum.WAIT_PAY.getCode(), PackOrderStateEnum.WAIT_DELIVERY.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payTimeOut(List<Long> idList) {
        packOrderRepository.updateState(idList,
                PackOrderStateEnum.WAIT_PAY.getCode(), PackOrderStateEnum.CLOSE.getCode());
    }

    @Override
    public List<Long> packOrderIdList(PackOrderQuery query) {
        return packOrderRepository.packOrderIdList(query);
    }
}
