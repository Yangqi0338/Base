package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CoursePurchaseRecordDO;
import com.newzkl.platform.base.biz.course.model.purchase.query.UserPurchasedCoursePageReq;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程购买记录 Mapper
 *
 * @author KC
 */
@Mapper
public interface CoursePurchaseRecordDAO extends BaseMapper<CoursePurchaseRecordDO> {

    /**
     * 组装已购课程分页查询条件(单表, 课程属性过滤由 courseIdFilter 收窄)
     *
     * @param req            分页查询条件
     * @param courseIdFilter 预解析课程ID集合, null/空表示不收窄
     * @return 条件构造器
     */
    default BaseLambdaQueryWrapper<CoursePurchaseRecordDO> getLw(UserPurchasedCoursePageReq req, List<Long> courseIdFilter) {
        BaseLambdaQueryWrapper<CoursePurchaseRecordDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notNullEq(CoursePurchaseRecordDO::getUserId, req.getUserId())
                .notNullEq(CoursePurchaseRecordDO::getPayState, req.getPayState());
        if (courseIdFilter != null) {
            lw.notEmptyIn(CoursePurchaseRecordDO::getCourseId, courseIdFilter);
        }
        lw.orderByDesc(CoursePurchaseRecordDO::getCreateTime);
        return lw;
    }

    /**
     * 支付成功回写(状态 + 流水号 + 支付时间)
     *
     * @param orderNo  订单编号
     * @param payState 目标支付状态
     * @param payNo    第三方支付流水号
     * @return 更新条数
     */
    default int updatePayState(Long orderNo, Integer payState, String payNo) {
        LambdaUpdateWrapper<CoursePurchaseRecordDO> uw = Wrappers.<CoursePurchaseRecordDO>lambdaUpdate()
                .set(CoursePurchaseRecordDO::getPayState, payState)
                .set(CoursePurchaseRecordDO::getPayNo, payNo)
                .set(CoursePurchaseRecordDO::getPayTime, LocalDateTime.now())
                .eq(CoursePurchaseRecordDO::getOrderNo, orderNo);
        return this.update(null, uw);
    }
}
