package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.api.OrderPayApi;
import com.newzkl.platform.base.biz.course.domain.adapt.api.OrderPayCommand;
import com.newzkl.platform.base.biz.course.domain.adapt.api.PayResultDTO;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CoursePurchaseRecordRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterWatchRecordRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.domain.purchase.entity.CoursePurchaseRecord;
import com.newzkl.platform.base.biz.course.domain.service.CourseDomain;
import com.newzkl.platform.base.biz.course.domain.service.CoursePurchaseRecordDomain;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.biz.course.model.purchase.enums.PurchasePayStateEnum;
import com.newzkl.platform.base.biz.course.model.purchase.query.UserPurchasedCoursePageReq;
import com.newzkl.platform.base.biz.course.model.purchase.req.CoursePurchaseReq;
import com.newzkl.platform.base.biz.course.model.purchase.res.CoursePurchaseCreateRes;
import com.newzkl.platform.base.biz.course.model.purchase.res.UserPurchasedCourseRes;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseWatchStatisticRes;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.redis.lock.impl.RedissonLockUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程购买记录领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.service.impl.CoursePurchaseRecordDomainServiceImpl}
 * + 应用服务 {@code CoursePurchaseRecordServiceImpl} 合并。异常换 {@code ThrowsException}(与
 * {@code CourseDomainImpl} 一致); 锁换 Base {@code RedissonLockUtil}(Base RedisUtil 无 tryLock);
 * 支付结果由 {@code instanceof HuiFuPayRes} 收敛为 {@code PayResultDTO#qrCode} 非空即成功;
 * 已购列表课程字段由同域 {@code CourseDomain} 逐记录装配, 观看数由观看仓储批量统计, 免联表 SQL。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoursePurchaseRecordDomainImpl implements CoursePurchaseRecordDomain {

    private final CourseRepository courseRepository;

    private final CoursePurchaseRecordRepository coursePurchaseRecordRepository;

    private final CourseChapterWatchRecordRepository courseChapterWatchRecordRepository;

    private final CourseDomain courseDomain;

    private final OrderPayApi orderPayApi;

    /**
     * 分布式锁前缀(用户+课程维度防并发下单)
     */
    private static final String PURCHASE_LOCK_PREFIX = "course:purchase:lock:";

    /**
     * 锁租约(秒)
     */
    private static final int LOCK_EXPIRE = 30;

    /**
     * 锁等待(秒)
     */
    private static final int LOCK_WAIT = 3;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CoursePurchaseCreateRes createCoursePurchaseRecord(CoursePurchaseReq req) {
        log.info("创建课程购买记录, 请求参数: {}", req);
        ThrowsException.isTrue(req.getCourseId() == null || req.getUserId() == null,
                BaseErrorCode.PARAM, "课程ID和用户ID");
        ThrowsException.isTrue(req.getPayType() == null || (req.getPayType() != 1 && req.getPayType() != 2),
                BaseErrorCode.PARAM, "支付方式仅支持微信(1)和支付宝(2)");

        String lockKey = PURCHASE_LOCK_PREFIX + req.getUserId() + ":" + req.getCourseId();
        boolean locked = RedissonLockUtil.tryLock(lockKey, LOCK_WAIT, LOCK_EXPIRE);
        ThrowsException.isTrue(!locked, BaseErrorCode.BUSY, "");
        try {
            CourseRes course = courseDomain.getById(req.getCourseId());

            CoursePurchaseRecord exist =
                    coursePurchaseRecordRepository.findSuccessByUserIdAndCourseId(req.getUserId(), req.getCourseId());
            if (exist != null) {
                ThrowsException.isTrue(PurchasePayStateEnum.SUCCESS.getCode().equals(exist.getPayState()),
                        BaseErrorCode.REPEAT, "");
            }

            long sellCent = centOf(course.getSellPrice());
            CoursePurchaseRecord record = new CoursePurchaseRecord();
            record.setOrderNo(SnowflakeIdAble.getSnowflakeId());
            record.setCourseId(course.getId());
            record.setCourseNum(course.getCourseNum());
            record.setUserId(req.getUserId());
            record.setOriginalPrice(centOf(course.getOriginalPrice()));
            record.setPayPrice(sellCent);
            record.setPayType(req.getPayType());
            record.setPayState(sellCent == 0
                    ? PurchasePayStateEnum.SUCCESS.getCode() : PurchasePayStateEnum.PENDING.getCode());

            if (sellCent != 0) {
                OrderPayCommand command = new OrderPayCommand();
                command.setOrderNo(record.getOrderNo());
                command.setConsumeType(OrderPayCommand.CONSUME_TYPE_COURSE);
                command.setOrderAmount((int) sellCent);
                command.setPayAmount((int) sellCent);
                command.setOrderInfo("购买课程：" + course.getTitle());
                command.setGoodsInfo("订单号:" + record.getOrderNo());
                command.setAccountId(SecurityUtils.getAccountId());
                command.setAccountName(SecurityUtils.getUsername());
                command.setPayType(req.getPayType());

                PayResultDTO payResult = orderPayApi.orderPay(command);
                ThrowsException.isTrue(payResult == null || payResult.getQrCode() == null,
                        BaseErrorCode.REMOTE, "创建支付订单");
                record.setPayUrl(payResult.getQrCode());
            }

            CoursePurchaseRecord saved = coursePurchaseRecordRepository.saveRecord(record);
            // 免费课创建即支付成功, 实际购买数在此递增; 付费课待支付成功回调递增
            if (sellCent == 0) {
                courseDomain.addPurchaseCount(course.getId());
            }
            log.info("创建课程购买记录成功, 订单号: {}", saved.getOrderNo());
            return toCreateRes(saved, course.getTitle());
        } finally {
            RedissonLockUtil.unlock(lockKey);
        }
    }

    @Override
    public IPage<UserPurchasedCourseRes> pageUserPurchasedCourse(UserPurchasedCoursePageReq req) {
        ThrowsException.isNull(req.getUserId(), BaseErrorCode.PARAM, "用户ID");

        List<Long> courseIdFilter = null;
        boolean needFilter = (req.getCourseTitle() != null && !req.getCourseTitle().isBlank())
                || req.getCategoryId() != null;
        if (needFilter) {
            courseIdFilter = courseRepository.listIdsByTitleAndCategory(req.getCourseTitle(), req.getCategoryId());
            if (courseIdFilter.isEmpty()) {
                return new Page<>(req.getPageNo(), req.getPageSize(), 0);
            }
        }

        Page<CoursePurchaseRecord> recordPage =
                coursePurchaseRecordRepository.pageUserPurchased(req, courseIdFilter);
        Page<UserPurchasedCourseRes> result =
                new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        result.setRecords(assemble(req.getUserId(), recordPage.getRecords()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(Long orderNo, String payNo) {
        CoursePurchaseRecord record = coursePurchaseRecordRepository.findByOrderNo(orderNo);
        ThrowsException.isNull(record, BaseErrorCode.NODATA, "订单");
        coursePurchaseRecordRepository.updatePayState(orderNo, PurchasePayStateEnum.SUCCESS.getCode(), payNo);
        courseDomain.addPurchaseCount(record.getCourseId());
    }

    /**
     * 装配已购课程列表(课程/讲师/分类字段取自同域课程视图, 观看数批量统计)
     *
     * @param userId  用户ID
     * @param records 购买记录列表
     * @return 已购课程视图列表, 永远非 null
     */
    private List<UserPurchasedCourseRes> assemble(Long userId, List<CoursePurchaseRecord> records) {
        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> courseIdList = records.stream().map(CoursePurchaseRecord::getCourseId).distinct().toList();
        List<CourseWatchStatisticRes> watchStatList =
                courseChapterWatchRecordRepository.batchStatWatchedChapter(userId, courseIdList);
        Map<Long, Integer> watchMap = new HashMap<>(watchStatList.size());
        watchStatList.forEach(item -> watchMap.put(item.getCourseId(),
                item.getWatchedChapterCount() == null ? 0 : item.getWatchedChapterCount()));

        List<UserPurchasedCourseRes> list = new ArrayList<>(records.size());
        for (CoursePurchaseRecord record : records) {
            UserPurchasedCourseRes res = new UserPurchasedCourseRes();
            res.setPurchaseRecordId(record.getId());
            res.setOrderNo(record.getOrderNo() == null ? null : String.valueOf(record.getOrderNo()));
            res.setPayState(record.getPayState());
            res.setPayPrice(record.getPayPrice());
            res.setOriginalPrice(record.getOriginalPrice());
            res.setPayTime(record.getPayTime());
            res.setPayType(record.getPayType());
            res.setCreateTime(record.getCreateTime());
            res.setCourseId(record.getCourseId());
            res.setCourseNum(record.getCourseNum());
            res.setWatchCount(watchMap.getOrDefault(record.getCourseId(), 0));
            fillCourseFields(res, record.getCourseId());
            list.add(res);
        }
        return list;
    }

    /**
     * 回填课程/讲师/分类冗余字段, 课程视图缺失时(已删)静默跳过
     *
     * @param res      已购课程视图
     * @param courseId 课程ID
     */
    private void fillCourseFields(UserPurchasedCourseRes res, Long courseId) {
        CourseRes course;
        try {
            course = courseDomain.getById(courseId);
        } catch (RuntimeException e) {
            // 课程已删除仍展示购买记录(源 LEFT JOIN 语义), 课程字段留空
            return;
        }
        res.setLecturerId(course.getLecturerId());
        res.setLecturerName(course.getLecturerName());
        res.setCourseTitle(course.getTitle());
        res.setCategoryId(course.getCategoryId());
        res.setCategoryName(course.getCategoryName());
        res.setCoverImage(course.getCoverImage());
        res.setTotalDurationCentisecond(course.getTotalDurationCentisecond());
        res.setTotalDurationSeconds(course.getTotalDurationSeconds());
        res.setChapterCount(course.getChapterCount());
        res.setIntro(course.getIntro());
    }

    /**
     * 购买记录实体转下单出参
     *
     * @param record      购买记录实体
     * @param courseTitle 课程标题
     * @return 下单出参
     */
    private CoursePurchaseCreateRes toCreateRes(CoursePurchaseRecord record, String courseTitle) {
        CoursePurchaseCreateRes res = TransferUtils.transfer(record, CoursePurchaseCreateRes::new);
        res.setCourseTitle(courseTitle);
        res.setPayStateDesc(PurchasePayStateEnum.descOf(record.getPayState()));
        return res;
    }

    /**
     * 取 Money 分值, 空视为 0
     *
     * @param money 金额
     * @return 分值
     */
    private long centOf(Money money) {
        return money == null ? 0L : money.getCent();
    }
}
