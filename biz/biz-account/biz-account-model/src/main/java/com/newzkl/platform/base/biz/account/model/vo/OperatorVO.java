package com.newzkl.platform.base.biz.account.model.vo;


import cn.hutool.core.lang.Opt;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.account.model.req.web.OperatorProxySaveReq;
import lombok.Data;

/**
 * 市场运营商
 *
 * @author fang
 */
@Data
public class OperatorVO extends BaseRes {

    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 头像
     */
    private String head;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 交易师数量
     */
    private Integer dealerNumber;

    /**
     * 邀请运营商数量
     */
    private Integer inviteSupplierNumber;
    /**
     * 一级市场数量
     */
    private Integer oneMarketNumber;
    /**
     * 二级市场数量
     */
    private Integer twoMarketNumber;
    /**
     * 邀请渠道商数量
     */
    private Integer inviteChannelNumber;
    /**
     * 供应商商品数量
     */
    private Integer supplierGoodsCount;
    /**
     * 自身的订单流水 (Money, 落库 BIGINT 分)
     */
    private Money orderAmount;
    /**
     * 总订单流水 (Money, 落库 BIGINT 分)
     */
    private Money orderTotalAmount;
    /**
     * 提货积分
     */
    private Integer goodsPoints;
    /**
     * 升级进度
     */
    private Double levelUpProgress;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 服务费 (Money, 落库 BIGINT 分)
     */
    private Money serviceAmount;
    /**
     * 运营类型 0 机构 1 行业 2 区域
     */
    private OperatorEnum.Type type;
    /**
     * 行业id,区域地址编码 | (多选,拼接)
     */
    private String typeForeignId;
    /**
     * 行业名称 / 区域地址
     */
    private String typeForeignName;
    /**
     * 采购金类型 0 自营 1 合作
     */
    private Integer balanceType;
    /**
     * 杠杆比例
     */
    private Integer leverageRatio;
    /**
     * 域名
     */
    private String domain;
    /**
     * 信息
     */
    private String info;
    /**
     * 联表:实名认证信息 : 格式:NameAuthVO
     */
    private String nameAuthVO;
    /**
     * 联表:实名认证审批状态
     */
    private Integer nameAuthAuditState;
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

    public void init(OperatorProxySaveReq operatorProxySaveReq, Long accountId) {
        this.id = accountId;
        this.name = operatorProxySaveReq.getName();
        this.username = operatorProxySaveReq.getUsername();

        this.dealerNumber = 0;
        this.oneMarketNumber = 0;
        this.twoMarketNumber = 0;
        this.inviteChannelNumber = 0;
        this.supplierGoodsCount = 0;
        this.serviceAmount = Money.ZERO;
        this.orderAmount = Money.ZERO;
        this.goodsPoints = 0;
        this.levelUpProgress = 0.0;
        // TODO[cross-domain finance]: this.serviceFeeConfigVO = JSONObject.toJSONString(operatorProxySaveReq.getServiceFeeConfigVO()); (ServiceFeeConfigVO 属 finance 域, 已解耦)
        this.type = operatorProxySaveReq.getType();
        this.typeForeignId = operatorProxySaveReq.getTypeForeignId();
        this.typeForeignName = operatorProxySaveReq.getTypeForeignName();
        this.balanceType = operatorProxySaveReq.getBalanceType();
        this.leverageRatio = operatorProxySaveReq.getLeverageRatio() == null ? 0 : operatorProxySaveReq.getLeverageRatio();
        this.domain = operatorProxySaveReq.getDomain();
        this.info = operatorProxySaveReq.getInfo();
        this.phone = Opt.ofNullable(operatorProxySaveReq.getPhone()).orElse(this.username);
    }
}