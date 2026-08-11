package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataSpuRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.AuditDataSpuDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.AuditDataSpuDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataSpu;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataSpuQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataSpuVO;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 商品上传审核数据
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class AuditDataSpuRepositoryImpl implements AuditDataSpuRepository {

    private final AuditDataSpuDAO auditDataSpuDAO;

    @Override
    public AuditDataSpu voToAuditDataSpu(AuditDataSpuVO auditDataSpuVO) {
        return TransferUtils.transfer(auditDataSpuVO, AuditDataSpu::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long auditDataSpuSave(AuditDataSpu auditDataSpu) {
        AuditDataSpuDO auditDataSpuDO = TransferUtils.transfer(auditDataSpu, AuditDataSpuDO::new);
        if (auditDataSpuDO.getId() == null || auditDataSpuDO.getId() == 0) {
            auditDataSpuDO.setId(SnowflakeGenerator.getSnowflakeId());
            auditDataSpuDAO.insert(Collections.singletonList(auditDataSpuDO));
        } else {
            auditDataSpuDAO.updateById(auditDataSpuDO);
        }
        return auditDataSpuDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditDataSpuDelete(List<Long> idList) {
        auditDataSpuDAO.deleteByIds(idList);
    }

    @Override
    public void auditDataSpuUpdateByQuery(AuditDataSpu auditDataSpu, AuditDataSpuQuery auditDataSpuQuery) {
        auditDataSpuDAO.update(TransferUtils.transfer(auditDataSpu, AuditDataSpuDO::new), auditDataSpuDAO.buildQueryWrapper(auditDataSpuQuery));
    }

    @Override
    public AuditDataSpu auditDataSpu(Long id) {
        return TransferUtils.transfer(auditDataSpuDAO.selectById(id), AuditDataSpu::new);
    }

    @Override
    public AuditDataSpuVO auditDataSpuVO(Long id) {
        return TransferUtils.transfer(auditDataSpuDAO.selectById(id), AuditDataSpuVO::new);
    }

    @Override
    public List<AuditDataSpu> auditDataSpuList(AuditDataSpuQuery auditDataSpuQuery) {
        return TransferUtils.transfers(this.auditDataSpuPageVOList(auditDataSpuQuery).getRecords(), AuditDataSpu::new);
    }

    @Override
    public List<AuditDataSpuVO> auditDataSpuVOList(AuditDataSpuQuery auditDataSpuQuery) {
        return this.auditDataSpuPageVOList(auditDataSpuQuery).getRecords();
    }

    @Override
    public Page<AuditDataSpuVO> auditDataSpuPageVOList(AuditDataSpuQuery auditDataSpuQuery) {
        return TransferUtils.transferPage(auditDataSpuDAO.selectPage(RepositorySupport.page(auditDataSpuQuery), auditDataSpuDAO.buildQueryWrapper(auditDataSpuQuery)), AuditDataSpuVO::new);
    }
}
