package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程购买记录(course_purchase_record)持久化对象
 *
 * <p>迁移自 {@code com.zkl.scm.user.infrastructure.entity.CoursePurchaseRecordDO}。
 * {@code @TableName} 不写显式表名, 中台 {@code DynamicTableNameInnerInterceptor} 按类名去
 * {@code DO} 后缀推表名({@code CoursePurchaseRecordDO} → {@code course_purchase_record});
 * 主键雪花与 createTime/updateTime/delFlag/executor 由 {@code BaseDO} 承接,
 * 源手写 id/isDeleted/createBy/updateBy 去除。价格单位分。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class CoursePurchaseRecordDO extends BaseDO {

    /**
     * 订单编号(唯一)
     */
    private Long orderNo;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程编码(冗余)
     */
    private String courseNum;

    /**
     * 购买用户ID
     */
    private Long userId;

    /**
     * 课程原价(分)
     */
    private Long originalPrice;

    /**
     * 实际支付金额(分)
     */
    private Long payPrice;

    /**
     * 支付状态: 0-待支付, 1-支付成功, 2-支付失败
     */
    private Integer payState;

    /**
     * 支付完成时间
     */
    private LocalDateTime payTime;

    /**
     * 支付方式: 1-微信支付, 2-支付宝支付
     */
    private Integer payType;

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
