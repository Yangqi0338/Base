package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.ShipAddressRepository;
import com.newzkl.platform.base.biz.account.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressQuery;
import com.newzkl.platform.base.biz.account.model.address.req.ShipAddressReq;
import com.newzkl.platform.base.biz.account.model.address.res.ShipAddressRes;
import com.newzkl.platform.base.biz.account.model.address.vo.ShipAddressVO;
import com.newzkl.platform.base.biz.account.model.assembler.ShipAddressAssembler;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
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
 * @author KC
 */
@Service("accountShipAddressDomainImpl")
@RequiredArgsConstructor
public class ShipAddressDomainImpl implements ShipAddressDomain {

    private final ShipAddressRepository shipAddressRepository;
    private final ShipAddressAssembler assembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(ShipAddressReq req) {
        ShipAddressVO item = assembler.req2VO(req);
        item.setId(SnowflakeGenerator.getSnowflakeId());
        item.setAccountId(SecurityUtils.getAccountId());
        item.setIdentity(SecurityUtils.getIdentity());

        Long shipAddressId = shipAddressRepository.save(item);
        // 其他地址设为非默认
        if (isDefault(item.getIsDefault())) {
            shipAddressRepository.setOtherNotDefault(item.getIdentity(), item.getAccountId(), shipAddressId);
        }
        return shipAddressId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(Long id, ShipAddressReq req) {
        ShipAddressVO item = assembler.req2VO(req);
        item.setId(id);
        // 其他地址设为非默认
        if (isDefault(item.getIsDefault())) {
            shipAddressRepository.setOtherNotDefault(SecurityUtils.getIdentity(), SecurityUtils.getAccountId(), id);
        }
        return shipAddressRepository.edit(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return shipAddressRepository.delete(idList);
    }

    @Override
    public ShipAddressRes detail(Long id) {
        return assembler.vo2Res(shipAddressRepository.detail(id));
    }

    @Override
    public ShipAddressRes defaultShipAddress() {
        ShipAddressQuery query = new ShipAddressQuery();
        query.setIdentity(SecurityUtils.getIdentity());
        query.setAccountId(SecurityUtils.getAccountId());
        query.setIsDefault(CommonEnum.YesOrNo.YES.getCode());
        return assembler.vo2Res(shipAddressRepository.findByQuery(query));
    }

    @Override
    public Page<ShipAddressRes> pageList(ShipAddressQuery query) {
        return TransferUtils.transferPage(shipAddressRepository.pageList(query), assembler::vo2Res);
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
