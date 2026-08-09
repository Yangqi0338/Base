package com.newzkl.platform.base.biz.course.model.purchase.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程购买记录域内实体
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.entity.CoursePurchaseRecord}。
 * 源 {@code isDeleted/createBy/updateBy} 去除(逻辑删除/操作人由 {@code BaseDO} 承接);
 * 主键雪花由 DO 落库时 {@code IdType.ASSIGN_ID} 生成, 订单号仍显式雪花。价格单位分。</p>
 *
 * @author KC
 */
@Data
public class CoursePurchaseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单编号(唯一)
     */
    private Long orderNo;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程编码
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
     * 支付链接/二维码
     */
    private String payUrl;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
