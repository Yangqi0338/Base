package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.biz.account.domain.repository.DealerRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.DealerDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.DealerDO;
import com.newzkl.platform.base.biz.account.model.req.DealerQuery;
import com.newzkl.platform.base.biz.account.model.res.DealerOutRes;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 市场交易师
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class DealerRepositoryImpl implements DealerRepository {

    private final DealerDAO dealerDAO;

    @Override
    public Long dealerSave(DealerVO dealer) {
        DealerDO dealerDO = TransferUtils.transfer(dealer, DealerDO::new);
        dealerDAO.insert(dealerDO);
        return dealerDO.getId();
    }

    @Override
    public int dealerEdit(DealerVO dealer) {
        DealerDO dealerDO = TransferUtils.transfer(dealer, DealerDO::new);
        return dealerDAO.updateById(dealerDO);
    }

    @Override
    public int dealerDelete(List<Long> dealerIdList) {
        DealerQuery query = new DealerQuery();
        query.setIdList(dealerIdList);
        return dealerDAO.deleteByIds(dealerIdList);
    }

    @Override
    public void dealerEdit(List<EditColumnDTO> columnList, Long id) {
        DealerQuery query = new DealerQuery();
        query.setId(id);
//        dealerDAO.columnByQuery(columnList, query);
    }

    @Override
    public DealerVO dealer(Long dealerId) {
        DealerDO dealerDO = dealerDAO.selectById(dealerId);
        return TransferUtils.transfer(dealerDO, DealerVO::new);
    }

    @Override
    public List<DealerOutRes> dealerRpcVO(List<Long> dealerIdList) {
        if (CollectionUtil.isEmpty(dealerIdList)) {
            return Collections.emptyList();
        }

        QueryWrapper<DealerOutRes> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", dealerIdList);

        List<DealerOutRes> list = dealerDAO.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public List<DealerOutRes> dealerRpcVO() {

        QueryWrapper<DealerOutRes> queryWrapper = new QueryWrapper<>();


        List<DealerOutRes> list = dealerDAO.selectList(queryWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            return list;
        }

        return Collections.emptyList();
    }
}
