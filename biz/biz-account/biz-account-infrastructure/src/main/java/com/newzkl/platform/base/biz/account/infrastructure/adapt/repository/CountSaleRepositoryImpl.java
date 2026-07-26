package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.CountSaleRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.CountSaleDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.CountSaleDO;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.biz.account.model.res.CountSaleVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售统计仓储实现。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.infrastructure.repository.CountSaleRepositoryImpl}。
 * 旧 mapper xml 的 {@code insert} / {@code updateByPrimaryKeySelective} /
 * {@code deleteByQuery} / {@code voByQuery} / {@code listByQuery} 全部改由
 * MyBatis-Plus 通用方法与 wrapper 表达, 本仓不写 mapper xml。
 * 旧 {@code resetUserOrderCount} 会顺带清 supplier / channel 冗余计数列, 属定时任务能力, 未迁。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class CountSaleRepositoryImpl implements CountSaleRepository {

    private final CountSaleDAO countSaleDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CountSaleVO countSale) {
        CountSaleDO countSaleDO = TransferUtils.transfer(countSale, CountSaleDO::new);
        // 统计记录主键由领域层雪花生成后透传, 不能走 preInsert 清 id
        countSaleDAO.insert(countSaleDO);
        return countSaleDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(CountSaleVO countSale) {
        return countSaleDAO.updateById(TransferUtils.transfer(countSale, CountSaleDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return 0;
        }
        return countSaleDAO.deleteByIds(idList);
    }

    @Override
    public CountSaleVO detail(Long id) {
        return TransferUtils.transfer(countSaleDAO.selectById(id), CountSaleVO::new);
    }

    @Override
    public CountSaleVO findByQuery(CountSaleQuery query) {
        CountSaleDO countSaleDO = countSaleDAO.selectOne(countSaleDAO.getLw(query), false);
        return TransferUtils.transfer(countSaleDO, CountSaleVO::new);
    }

    @Override
    public List<CountSaleVO> list(CountSaleQuery query) {
        List<CountSaleDO> countSaleList = countSaleDAO.selectList(countSaleDAO.getLw(query));
        List<CountSaleVO> result = TransferUtils.transfers(countSaleList, CountSaleVO::new);
        return result == null ? new ArrayList<>() : result;
    }

    @Override
    public Page<CountSaleVO> pageList(CountSaleQuery query) {
        Page<CountSaleDO> pageList = countSaleDAO.selectPage(RepositorySupport.page(query),
                countSaleDAO.getLw(query));
        return TransferUtils.transferPage(pageList, CountSaleVO::new);
    }
}
