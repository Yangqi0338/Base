package com.newzkl.platform.base.biz.account.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.CountSaleRepository;
import com.newzkl.platform.base.biz.account.domain.service.CountSaleDomain;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.biz.account.model.req.CountSaleReq;
import com.newzkl.platform.base.biz.account.model.res.CountSaleVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 销售统计领域服务实现。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.count.service.impl.CountSaleDomainImpl}:
 * 主键仍由领域层雪花生成后透传仓储 (故仓储走 {@code insert} 而非 upsert)。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class CountSaleDomainImpl implements CountSaleDomain {

    private final CountSaleRepository countSaleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CountSaleReq req) {
        CountSaleVO item = TransferUtils.transfer(req, CountSaleVO::new);
        item.setId(SnowflakeIdAble.getSnowflakeId());
        return countSaleRepository.save(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(Long id, CountSaleReq req) {
        CountSaleVO item = TransferUtils.transfer(req, CountSaleVO::new);
        item.setId(id);
        return countSaleRepository.edit(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return countSaleRepository.delete(idList);
    }

    @Override
    public CountSaleVO detail(Long id) {
        return countSaleRepository.detail(id);
    }

    @Override
    public CountSaleVO findByQuery(CountSaleQuery query) {
        return countSaleRepository.findByQuery(query);
    }

    @Override
    public Page<CountSaleVO> pageList(CountSaleQuery query) {
        return countSaleRepository.pageList(query);
    }
}
