package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ShipAddressRepository;
import com.newzkl.platform.base.biz.order.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.order.model.req.query.ShipAddressQuery;
import com.newzkl.platform.base.biz.order.model.req.ShipAddressReq;
import com.newzkl.platform.base.biz.order.model.res.ShipAddressRes;
import com.newzkl.platform.base.biz.order.model.vo.ShipAddressVO;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.address.service.impl.ShipAddressDomainImpl}。
 * 旧实现的充血写法 (向实体回塞 repository 引用) 已去除; 旧 {@code SecurityUtils.getIdentity()}</p>
 *
 * <p>并入订单域后 MapStruct assembler 去除, 改用 {@code TransferUtils} 同名字段拷贝, 与
 * biz-order 既有约定一致</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class ShipAddressDomainImpl implements ShipAddressDomain {

    private final ShipAddressRepository shipAddressRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(ShipAddressReq req) {
        ShipAddressVO item = TransferUtils.transfer(req, ShipAddressVO::new);
        Long shipAddressId = item.getId();
        if (item.getId() == null){
            shipAddressId = shipAddressRepository.save(item);
        }else {
            shipAddressRepository.edit(item);
        }

        // 其他地址设为非默认
        if (isDefault(item.getIsDefault())) {
            shipAddressRepository.setOtherNotDefault(item.getIdentity(), item.getAccountId(), shipAddressId);
        }

        return shipAddressId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return shipAddressRepository.delete(idList);
    }

    @Override
    public ShipAddressRes detail(Long id) {
        return TransferUtils.transfer(shipAddressRepository.detail(id), ShipAddressRes::new);
    }

    @Override
    public ShipAddressRes defaultShipAddress() {
        ShipAddressQuery query = new ShipAddressQuery();
        query.setIdentity(SecurityUtils.getIdentity());
        query.setAccountId(SecurityUtils.getAccountId());
        query.setIsDefault(CommonEnum.YesOrNo.YES.getCode());
        return TransferUtils.transfer(shipAddressRepository.findByQuery(query), ShipAddressRes::new);
    }

    @Override
    public Page<ShipAddressRes> pageList(ShipAddressQuery query) {
        return TransferUtils.transferPage(shipAddressRepository.pageList(query), ShipAddressRes::new);
    }

    /**
     * 判断是否为默认地址标记
     *
     * @param flag 是否默认标记
     * @return true 表示默认地址
     */
    private boolean isDefault(Integer flag) {
        return CommonEnum.YesOrNo.YES.getCode().equals(flag);
    }
}
