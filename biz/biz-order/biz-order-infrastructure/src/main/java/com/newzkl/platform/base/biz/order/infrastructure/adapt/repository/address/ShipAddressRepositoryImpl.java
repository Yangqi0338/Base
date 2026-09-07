package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.address;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ShipAddressRepository;
import com.newzkl.platform.base.biz.order.infrastructure.dao.ShipAddressDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.ShipAddressDO;
import com.newzkl.platform.base.biz.order.model.req.query.ShipAddressQuery;
import com.newzkl.platform.base.biz.order.model.vo.ShipAddressVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.infrastructure.repository.ShipAddressRepositoryImpl}。
 * 旧 mapper xml 的 {@code setOtherNotDefault} 自定义 SQL 改用 MyBatis-Plus
 * {@code LambdaUpdateWrapper} 表达, 本仓不写 mapper xml。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class ShipAddressRepositoryImpl implements ShipAddressRepository {

    private final ShipAddressDAO shipAddressDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(ShipAddressVO shipAddress) {
        ShipAddressDO shipAddressDO = TransferUtils.transfer(shipAddress, ShipAddressDO::new);
        shipAddressDAO.insert(shipAddressDO);
        return shipAddressDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(ShipAddressVO shipAddress) {
        return shipAddressDAO.updateById(TransferUtils.transfer(shipAddress, ShipAddressDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return shipAddressDAO.deleteByIds(idList);
    }

    @Override
    public ShipAddressVO detail(Long id) {
        return TransferUtils.transfer(shipAddressDAO.selectById(id), ShipAddressVO::new);
    }

    @Override
    public ShipAddressVO findByQuery(ShipAddressQuery query) {
        ShipAddressDO shipAddressDO = shipAddressDAO.selectOne(shipAddressDAO.getLw(query), false);
        return TransferUtils.transfer(shipAddressDO, ShipAddressVO::new);
    }

    @Override
    public Page<ShipAddressVO> pageList(ShipAddressQuery query) {
        Page<ShipAddressDO> pageList = shipAddressDAO.selectPage(RepositorySupport.page(query),
                shipAddressDAO.getLw(query));
        return TransferUtils.transferPage(pageList, ShipAddressVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setOtherNotDefault(AccountEnum.Identity identity, Long accountId, Long shipAddressId) {
        LambdaUpdateWrapper<ShipAddressDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ShipAddressDO::getIsDefault, CommonEnum.YesOrNo.NO.getCode())
                .eq(ShipAddressDO::getIdentity, identity)
                .eq(ShipAddressDO::getAccountId, accountId)
                .ne(ShipAddressDO::getId, shipAddressId);
        shipAddressDAO.update(null, updateWrapper);
    }
}
