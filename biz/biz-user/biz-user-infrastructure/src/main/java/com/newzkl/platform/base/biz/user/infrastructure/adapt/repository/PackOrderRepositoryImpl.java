package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.PackOrderRepository;
import com.newzkl.platform.base.biz.user.domain.pack.entity.PackOrder;
import com.newzkl.platform.base.biz.user.infrastructure.dao.PackOrderDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.PackOrderDO;
import com.newzkl.platform.base.biz.user.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.user.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 入会礼包订单仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.PackOrderRepositoryImpl}。
 * 旧 Assembler/XML 转换 + PageHelper 分页改为 {@code TransferUtils} + MyBatis-Plus {@code Page}。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class PackOrderRepositoryImpl implements PackOrderRepository {

    private final PackOrderDAO packOrderDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long packOrderSave(PackOrder packOrder) {
        PackOrderDO doObj = TransferUtils.transfer(packOrder, PackOrderDO::new);
        if (doObj.getId() == null || doObj.getId() == 0) {
            packOrderDAO.insert(doObj);
        } else {
            packOrderDAO.updateById(doObj);
        }
        return doObj.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long packOrderCreate(PackOrder packOrder) {
        PackOrderDO doObj = TransferUtils.transfer(packOrder, PackOrderDO::new);
        packOrderDAO.insert(doObj);
        return doObj.getId();
    }

    @Override
    public PackOrder packOrder(Long packOrderId) {
        return TransferUtils.transfer(packOrderDAO.selectById(packOrderId), PackOrder::new);
    }

    @Override
    public PackOrderRes packOrderVO(Long packOrderId) {
        return TransferUtils.transfer(packOrderDAO.selectById(packOrderId), PackOrderRes::new);
    }

    @Override
    public Page<PackOrderRes> packOrderVOList(PackOrderQuery query) {
        Page<PackOrderDO> doPage = packOrderDAO.selectPage(
                RepositorySupport.page(query), packOrderDAO.getLw(query));
        return TransferUtils.transferPage(doPage, PackOrderRes.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateState(List<Long> idList, Integer fromState, Integer toState) {
        if (ObjectUtil.isEmpty(idList)) {
            return 0;
        }
        return packOrderDAO.updateState(idList, fromState, toState);
    }

    @Override
    public List<Long> packOrderIdList(PackOrderQuery query) {
        List<PackOrderDO> list = packOrderDAO.selectList(packOrderDAO.getLw(query));
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(PackOrderDO::getId).toList();
    }
}
