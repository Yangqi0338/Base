package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商
 *
 * @author fang
 */
@Data
public class SupplierRes extends BaseRes {

    /**
     * 账号名称 (查询)
     */
    private String username;
    /**
     * 企业名称 (查询)
     */
    private String name;

    /**
     * 状态 (查询)
     */
    private Integer state;
    /**
     * 审批状态 (查询) (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
    private Integer auditState;
    /**
     * 企业区域
     */
    private String companyAreaCode;
    /**
     * 企业信息
     */
    private String companyInfo;
    /**
     * 是否缴纳保证金 (查询)
     */
    private CommonEnum.YesOrNo promisePayState;
    /**
     * 保证金金额
     */
    private Integer promisePayAmount;
    /**
     * 保证金审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
    private Integer promisePayAuditState;
    /**
     * 保证金缴纳配置 promise_pay_config
     * 0 即时 1 延迟
     */
    private Integer promisePayConfig;
    /**
     * 是否设置账期 (查询)
     */
    private CommonEnum.YesOrNo periodSetState;
    /**
     * 账期配置JSON
     * orderType 0 订单完成 1 收货完成
     * dataType 0 每月固定 1 商品审核
     * dataOne 每月固定日期
     * dataTwo 商品审核周期
     */
    private String periodSetConfig;
    /**
     * 应付保证金金额
     */
    private Integer shouldPromisePayAmount;
    /**
     * 主体类型 (查询)
     */
    private Integer bodyType;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 行业ID集合
     */
    private String industryIdList;
    /**
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 商品总数
     */
    private Integer goodsTotalCount;
    /**
     * 售卖中的商品
     */
    private Integer goodsOnSaleCount;
    /**
     * 总金额
     */
    private Integer goodsSaleAmount;
    /**
     * 月金额
     */
    private Integer monthGoodsSaleAmount;
    /**
     * 总销量
     */
    private Integer goodsSaleCount;
    /**
     * 月销量
     */
    private Integer monthGoodsSaleCount;
    /**
     * 总成交销量
     */
    private Integer goodsDealCount;
    /**
     * 待售卖的商品
     */
    private Integer goodsNotSaleCount;
    /**
     * 结算配置
     */
    private String settlementConfigVO;
    /**
     * 收货地址
     */
    private String receiveAddress;
    /**
     * 入驻时间
     */
    private LocalDateTime inTime;
    /**
     * 总成交笔数
     */
    private Integer totalOrderNumber;
    /**
     * 总售后笔数
     */
    private Integer totalRefundNumber;
    /**
     * 总订单金额
     */
    private Integer totalOrderAmount;
    /**
     * 总售后金额
     */
    private Integer totalRefundAmount;
}