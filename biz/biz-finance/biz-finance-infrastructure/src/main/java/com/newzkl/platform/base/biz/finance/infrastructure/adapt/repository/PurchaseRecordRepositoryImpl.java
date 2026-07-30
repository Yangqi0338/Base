package com.newzkl.platform.base.biz.finance.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.PurchaseRecordRepository;
import com.newzkl.platform.base.biz.finance.infrastructure.dao.PurchaseRecordDAO;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.PurchaseRecordDO;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordQuery;
import com.newzkl.platform.base.biz.finance.model.pay.res.SeatPackageOrderInfo;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购买记录 (PurchaseRecord)存储实现
 *
 * @author kc
 * @since 2025-11-25 17:24:23
 */
@Repository
@RequiredArgsConstructor
public class PurchaseRecordRepositoryImpl extends RepositorySupport implements PurchaseRecordRepository {

    private final PurchaseRecordDAO purchaseRecordDAO;

    /**
     * 详情
     *
     * @param id 主键
     * @return 详情
     */
    @Override
    public PurchaseRecordVO detail(Long id) {
        PurchaseRecordDO purchaseRecordDO = purchaseRecordDAO.selectById(id);
        PurchaseRecordVO purchaseRecord = TransferUtils.transfer(purchaseRecordDO, PurchaseRecordVO::new);
        if (purchaseRecord != null) {
            fillSeatPackageOrderInfo(purchaseRecord);
        }
        return purchaseRecord;
    }

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public List<PurchaseRecordVO> queryList(PurchaseRecordQuery query) {
        List<PurchaseRecordDO> doList = purchaseRecordDAO.selectList(purchaseRecordDAO.getLw(query));
        List<PurchaseRecordVO> result = TransferUtils.transfers(doList, PurchaseRecordVO.class);
        result.forEach(this::fillSeatPackageOrderInfo);
        return result;
    }

    /**
     * 查询分页
     *
     * @param query 查询条件
     * @return 购买记录分页
     */
    @Override
    public Page<PurchaseRecordVO> queryPage(PurchaseRecordQuery query) {
        Page<PurchaseRecordDO> doList = purchaseRecordDAO.selectPage(RepositorySupport.page(query), purchaseRecordDAO.getLw(query));
        Page<PurchaseRecordVO> result = TransferUtils.transferPage(doList, PurchaseRecordVO.class);
        result.getRecords().forEach(this::fillSeatPackageOrderInfo);
        return result;
    }

    /**
     * 从 orderInfo JSON 字符串反序列化填充 seatPackageOrderInfo
     *
     * <p>purchase_record.order_info 存储的是 {@code SeatPackageOrderInfo} 的 JSON 序列化，
     * 查询时需手动解析后写入 VO，前端通过 {@code seatPackageOrderInfo.purchaseNum} 等读取
     *
     * @param vo 待填充的购买记录 VO
     */
    private void fillSeatPackageOrderInfo(PurchaseRecordVO vo) {
        if (StrUtil.isNotBlank(vo.getOrderInfo())) {
            try {
                vo.setSeatPackageOrderInfo(JSONUtil.toBean(vo.getOrderInfo(), SeatPackageOrderInfo.class));
            } catch (Exception ignored) {
                // orderInfo 格式异常时跳过，不影响其他字段
            }
        }
    }

    /**
     * 新增数据
     *
     * @param purchaseRecord 新增实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(PurchaseRecordVO purchaseRecord) {
        PurchaseRecordDO purchaseRecordDO = TransferUtils.transfer(purchaseRecord, PurchaseRecordDO::new);
        purchaseRecordDAO.insert(purchaseRecordDO);
        return purchaseRecordDO.getId();
    }

    /**
     * 修改数据
     *
     * @param purchaseRecord 编辑实体
     * @param query          编辑查询
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(PurchaseRecordVO purchaseRecord, PurchaseRecordQuery query) {
        BaseLambdaQueryWrapper<PurchaseRecordDO> ew = purchaseRecordDAO.getLw(query);
        if (purchaseRecordDAO.exists(ew)) {
            throw new PlatformException(BaseErrorCode.INVALID_UPDATE);
        }

        PurchaseRecordDO purchaseRecordDO = TransferUtils.transfer(purchaseRecord, PurchaseRecordDO::new);
        boolean flag = purchaseRecordDAO.updateById(purchaseRecordDO) > 0;

        // 修改成功
        if (flag) {

        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        purchaseRecordDAO.deleteById(id);
    }
}

