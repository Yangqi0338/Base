package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.finance.domain.adapt.repository.PayeeInfoRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.CashPayeeInfoDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.CashPayeeInfoDO;
import com.newzkl.platform.base.biz.finance.model.pay.req.UpdatePayeeInfoReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PayeeInfoVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 现金流收款方配置仓储实现。
 *
 * <p>迁移自 new-scm {@code PayeeInfoRepositoryImpl}。旧实现依赖三条自定义 XML SQL,
 * 此处全部改为 MyBatis-Plus 原生 API, 因此不需要 {@code CashPayeeInfoDAO.xml}。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class PayeeInfoRepositoryImpl extends RepositorySupport implements PayeeInfoRepository {

    /**
     * 收款方配置 DAO。
     */
    private final CashPayeeInfoDAO cashPayeeInfoDAO;

    /**
     * 更新收款方配置: 物理删除该消费类型的旧配置后整体重插。
     *
     * @param req 更新请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterPayeeInfo(UpdatePayeeInfoReq req) {
        cashPayeeInfoDAO.delete(cashPayeeInfoDAO.getLw(req.getConsumeType()));
        if (req.getPayeeInfoList() == null || req.getPayeeInfoList().isEmpty()) {
            return;
        }
        List<CashPayeeInfoDO> records = TransferUtils.transfers(req.getPayeeInfoList(), CashPayeeInfoDO::new,
                (vo, entity) -> entity.setConsumeType(req.getConsumeType()));
        cashPayeeInfoDAO.insert(records);
    }

    /**
     * 按消费类型查询收款方配置。
     *
     * @param consumeType 消费类型
     * @return 收款方配置列表
     */
    @Override
    public List<PayeeInfoVO> queryPayeeInfo(Integer consumeType) {
        List<CashPayeeInfoDO> records = cashPayeeInfoDAO.selectList(cashPayeeInfoDAO.getLw(consumeType));
        return TransferUtils.transfers(records, PayeeInfoVO::new);
    }
}
