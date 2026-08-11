package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CoursePurchaseRecordRepository;
import com.newzkl.platform.base.biz.course.model.purchase.entity.CoursePurchaseRecord;
import com.newzkl.platform.base.biz.course.infrastructure.dao.CoursePurchaseRecordDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CoursePurchaseRecordDO;
import com.newzkl.platform.base.common.ddd.model.enums.course.PurchasePayStateEnum;
import com.newzkl.platform.base.biz.course.model.purchase.query.UserPurchasedCoursePageReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程购买记录仓储实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.repository.CoursePurchaseRecordRepositoryImpl}。
 * 源 Assembler + XML LEFT JOIN 分页改为 {@code TransferUtils} + 单表 {@code getLw};
 * 已购列表的课程字段装配上移领域层, 本实现只做单表持久化。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class CoursePurchaseRecordRepositoryImpl implements CoursePurchaseRecordRepository {

    private final CoursePurchaseRecordDAO coursePurchaseRecordDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CoursePurchaseRecord saveRecord(CoursePurchaseRecord record) {
        CoursePurchaseRecordDO recordDO = TransferUtils.transfer(record, CoursePurchaseRecordDO::new);
        coursePurchaseRecordDAO.insertOrUpdate(recordDO);
        return TransferUtils.transfer(recordDO, CoursePurchaseRecord::new);
    }

    @Override
    public CoursePurchaseRecord findByOrderNo(Long orderNo) {
        if (orderNo == null) {
            return null;
        }
        BaseLambdaQueryWrapper<CoursePurchaseRecordDO> wrapper = new BaseLambdaQueryWrapper<CoursePurchaseRecordDO>()
                .notNullEq(CoursePurchaseRecordDO::getOrderNo, orderNo);
        return TransferUtils.transfer(
                coursePurchaseRecordDAO.selectOne(wrapper.last("limit 1")), CoursePurchaseRecord::new);
    }

    @Override
    public CoursePurchaseRecord findSuccessByUserIdAndCourseId(Long userId, Long courseId) {
        if (userId == null || courseId == null) {
            return null;
        }
        BaseLambdaQueryWrapper<CoursePurchaseRecordDO> wrapper = new BaseLambdaQueryWrapper<CoursePurchaseRecordDO>()
                .notNullEq(CoursePurchaseRecordDO::getUserId, userId)
                .notNullEq(CoursePurchaseRecordDO::getCourseId, courseId)
                .notNullEq(CoursePurchaseRecordDO::getPayState, PurchasePayStateEnum.SUCCESS.getCode());
        return TransferUtils.transfer(
                coursePurchaseRecordDAO.selectOne(wrapper.last("limit 1")), CoursePurchaseRecord::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePayState(Long orderNo, Integer payState, String payNo) {
        return coursePurchaseRecordDAO.updatePayState(orderNo, payState, payNo) > 0;
    }

    @Override
    public Page<CoursePurchaseRecord> pageUserPurchased(UserPurchasedCoursePageReq req, List<Long> courseIdFilter) {
        Page<CoursePurchaseRecordDO> doPage = coursePurchaseRecordDAO.selectPage(
                RepositorySupport.page(req), coursePurchaseRecordDAO.getLw(req, courseIdFilter));
        return TransferUtils.transferPage(doPage, CoursePurchaseRecord::new);
    }
}
