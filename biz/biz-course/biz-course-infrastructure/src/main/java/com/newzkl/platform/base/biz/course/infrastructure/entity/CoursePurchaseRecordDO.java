package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCode;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程购买记录(course_purchase_record)持久化对象
 *
 * <p>价格单位分。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CoursePurchaseRecordDO extends BaseDO {

    /**
     * 订单编号
     * @ext 唯一
     */
    @BusinessCode(BusinessType.ORDER_COURSE)
    private String orderNo;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程编码
     * @ext 冗余
     */
    private String courseNum;

    /**
     * 购买用户ID
     */
    private Long userId;

    /**
     * 课程原价
     * @ext 单位分
     */
    private Long originalPrice;

    /**
     * 实际支付金额
     * @ext 单位分
     */
    private Long payPrice;

    /**
     * 支付状态
     * @ext 0-待支付, 1-支付成功, 2-支付失败
     */
    private Integer payState;

    /**
     * 支付完成时间
     */
    private LocalDateTime payTime;

    /**
     * 支付方式
     * @ext 1-微信支付, 2-支付宝支付
     */
    private PaymentEnum.PayType payType;

    /**
     * 第三方支付流水号
     */
    private String payNo;

    /**
     * 支付链接/二维码
     */
    private String payUrl;

    /**
     * 订单过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 订单取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 备注
     */
    private String remark;
}
