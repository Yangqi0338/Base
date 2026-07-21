package com.newzkl.platform.base.biz.account.model.vo;

import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.SupplierEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商
 *
 * @author fang
 */
@Data
public class SupplierVO extends BaseVO {
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
    private SupplierEnum.State state;
    /**
     * 审批状态 (查询) (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
    private AuditEnum.State auditState;
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
    private AuditEnum.State promisePayAuditState;
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
     */
    private String periodSetConfig;
    /**
     * 应付保证金金额
     */
    private Integer shouldPromisePayAmount;
    /**
     * 主体类型 (查询)
     */
    private AccountEnum.BodyType bodyType;
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
     * 联表:实名认证信息 : 格式:NameAuthVO
     */
    private String nameAuthVO;
    /**
     * 联表:实名认证审批状态
     */
    private CommonEnum.YesOrNo nameAuthAuditState;
    /**
     * 联表:邀请码
     */
    private String yqm;
    /**
     * 联表:真实姓名
     */
    private String realName;
    /**
     * 联表:角色ID集合
     */
    private String roleIdList;
    /**
     * 适配: 企业名称
     */
    private String companyName;
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

    /**
     * @return 进度状态
     */
    public Integer getCompanyState() {
        //信息审核
        if (ObjectUtil.equals(1, this.auditState)) {
            return 1;
        }
        //保证金审核
        else if (ObjectUtil.equals(2, this.auditState) && (ObjectUtil.equals(3, this.promisePayAuditState) || ObjectUtil.equals(1, this.promisePayAuditState))) {
            return 2;
        }

        //审核成功
        else if (ObjectUtil.equals(2, this.auditState) && ObjectUtil.equals(2, this.promisePayAuditState)) {
            return 3;
        }
        //账户注册(都不属于以上状态)
        return 0;
    }

    public String getCompanyName() {
        return this.name;
    }
}