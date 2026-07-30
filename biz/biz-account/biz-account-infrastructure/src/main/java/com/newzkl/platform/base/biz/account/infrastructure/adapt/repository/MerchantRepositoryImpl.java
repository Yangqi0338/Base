package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.MerchantRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.MerchantDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.MerchantDO;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.biz.account.model.merchant.vo.MerchantVO;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.infrastructure.repository.MerchantRepositoryImpl}。
 * 旧 {@code updateByPrimaryKeySelective} / {@code deleteByQuery} 自定义 SQL 改用
 * MyBatis-Plus 通用方法; 旧 {@code wxMpConfig} 单列查询改为按主键取实体后读取该列
 * (JSON 列由 {@code JacksonTypeHandler} 反序列化)。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class MerchantRepositoryImpl implements MerchantRepository {

    private final MerchantDAO merchantDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(MerchantVO merchant) {
        MerchantDO merchantDO = TransferUtils.transfer(merchant, MerchantDO::new);
        // 商户主键与账号 ID 同值, 不能走 preInsert 清 id
        merchantDAO.insert(merchantDO);
        return merchantDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(MerchantVO merchant) {
        return merchantDAO.updateById(TransferUtils.transfer(merchant, MerchantDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return merchantDAO.deleteByIds(idList);
    }

    @Override
    public MerchantVO detail(Long id) {
        return TransferUtils.transfer(merchantDAO.selectById(id), MerchantVO::new);
    }

    @Override
    public Page<MerchantVO> pageList(MerchantQuery query) {
        Page<MerchantDO> pageList = merchantDAO.selectPage(RepositorySupport.page(query), merchantDAO.getLw(query));
        return TransferUtils.transferPage(pageList, MerchantVO::new);
    }

    @Override
    public WxMpConfigVO wxMpConfig(Long merchantId) {
        MerchantDO merchantDO = merchantDAO.selectById(merchantId);
        return merchantDO == null ? null : merchantDO.getWxMpConfig();
    }
}
