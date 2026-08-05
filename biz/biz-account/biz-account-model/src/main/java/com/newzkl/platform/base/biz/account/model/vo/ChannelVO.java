package com.newzkl.platform.base.biz.account.model.vo;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

/**
 * 渠道商
 *
 * <p>⚠️ <b>对前端的契约变更清单</b>(2026-07-30 由 {@code tools/out-dto-diff.mjs} 机械比对旧
 * {@code com.zkl.scm.user.domain.role.model.vo.ChannelVO} 得出; 用户裁决 <b>后端不做兼容映射,
 * 由前端改</b>)。本 VO 是 {@code GET /user/channel/channelForAdmin} 出参, 调用方 yys-admin。</p>
 *
 * <p><b>改名(前端改字段名即可)</b>:</p>
 * <ul>
 *   <li>{@code realname} → {@code realName}</li>
 * </ul>
 *
 * <p><b>结构变更(不是改名, 前端需改读法)</b>:</p>
 * <ul>
 *   <li>旧 {@code roleId}(Long) + {@code roleName}(String) 两字段 → 塌缩成单个
 *       {@code role}({@code RoleEnum.CompanyRole} 枚举)。枚举取值与旧 roleId 的对应关系
 *       属业务口径, 未在本仓推定</li>
 * </ul>
 *
 * <p><b>本仓无对应字段(能力缺失, 非改名)</b>: {@code upDealerId} / {@code upDealerName} /
 * {@code upDealerUsername} / {@code upOperatorId}(上级交易师与上级运营商链)、
 * {@code jfLicense} / {@code jfPermission} / {@code mkPermission} / {@code wxMpConfig}
 * (后四个前端 0 引用)。前者 yys-admin 有引用(如 {@code channelDetails.vue:450}
 * 用 {@code upDealerId} 查上级交易师), 需业务确认该链路存废。</p>
 *
 * <p>另: 旧 {@code creator} / {@code updater} 已由 {@code ExecutorDTO} 的
 * {@code creatorId}/{@code creatorName}/{@code updaterId}/{@code updaterName} 取代,
 * 属全仓统一改造; 实测 6 个前端仓对 {@code .creator}/{@code .updater} 零引用, 无需处理。</p>
 *
 * @author fang
 */
@Data
public class ChannelVO extends BaseRes {

    /**
     * 角色ID
     *
     * <p>⚠️ 结构变更: 旧契约是 {@code roleId}(Long) + {@code roleName}(String) 两个字段,
     * 本仓塌缩为单个枚举。前端 {@code roleId} / {@code roleName} 读法失效 ——
     * 非改名, 无法靠字段映射兼容, 需按枚举 code 改读</p>
     */
    private RoleEnum.CompanyRole role;
    /**
     * 主体类型 (查询)
     */
    private AccountEnum.BodyType bodyType;
    /**
     * 状态 (查询)
     */
    private ChannelEnum.State state;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 渠道商名称 (查询) channel_name
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 企业信息
     */
    private String companyInfo;
    /**
     * 实名认证信息
     */
    private String nameAuthInfo;
    /**
     * 审批状态 (0,"待用户提交";1,"待审核";2,"通过",3,"未通过")
     */
    private AuditEnum.State auditState;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 交易师收益 (Money, 落库 BIGINT 分)
     */
    private Money dealerEarnings;
    /**
     * 市场数量
     */
    private Integer marketCount;
    /**
     * 营业执照
     */
    private String license;
    /**
     * 数字门店权限 0 无 1 有
     */
    private CommonEnum.YesOrNo storePermission;
    /**
     * 渠道商ID
     */
    private Long channelId;
    /**
     * 店铺地址，省CODE, 6位
     */
    private Integer shipProvinceCode;
    /**
     * 店铺地址，市CODE, 6位
     */
    private Integer shipCityCode;
    /**
     * 店铺地址，区CODE, 6位
     */
    private Integer shipAreaCode;
    /**
     * 联系人名称
     */
    private String contactsName;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 总订单笔数
     */
    private Integer totalOrderNumber;
    /**
     * 总订单金额 (Money, 落库 BIGINT 分)
     */
    private Money totalOrderAmount;
    /**
     * 总售后笔数
     */
    private Integer totalRefundNumber;
    /**
     * 总售后金额 (Money, 落库 BIGINT 分)
     */
    private Money totalRefundAmount;
    /**
     * 贡献金额 (Money, 落库 BIGINT 分)
     */
    private Money contributeAmount;
    /**
     * 层级贡献金额
     */
    private String contributeAmountStr;
    /**
     * 联表:实名认证信息 : 格式:NameAuthVO
     */
    private String nameAuthVO;
    /**
     * 联表:实名认证审批状态
     */
    private AuditEnum.State nameAuthAuditState;
    /**
     * 联表:邀请码
     */
    private String yqm;
    /**
     * 联表:真实姓名
     *
     * <p>⚠️ 契约改名: 旧字段名是 <b>{@code realname}</b>(全小写), 本仓改为驼峰 {@code realName}。
     * 2026-07-30 用户裁决: <b>后端不做兼容映射, 由前端改字段名</b>。
     * 已知待改调用方 —— {@code yys-admin/src/views/user/channelDetails.vue}
     * (:11 :78 :117 :123 :127 模板渲染, <b>:266 有 {@code result.realname.replace(...)},
     * 不改会抛 TypeError 整页白屏</b>)</p>
     */
    private String realName;
    /**
     * 联表:角色ID集合
     */
    private String roleIdList;
    /**
     * 联表:运营商域名
     */
    private String operatorDomain;
    /**
     * 联表:数字门店地址
     */
    private String storeUrl;
    /**
     * 联表:采购金类型 0 自营 1 合作
     */
    private Integer balanceType;
    /**
     * 渠道商贡献金额 (Money, 落库 BIGINT 分)
     */
    private Money earningContribute;
    /**
     * 联系方式
     */
    private String contactsWay;
    /**
     * 渠道商类型
     */
    private ChannelEnum.ChannelType channelType;
    /**
     * 连表: 三方账户权限
     */
    private String tripartiteAccountPermission;

    /**
     * 店铺id
     */
    public Long getStoreId() {
        return Opt.ofNullable(channelId).orElse(id);
    }

    public String getStoreUrl() {
        if (StrUtil.isNotEmpty(this.operatorDomain)) {
            if (BizUtil.stringLast(this.operatorDomain).equals("/")) {
                return this.operatorDomain + "scm/" + this.id + "/scm/";
            } else {
                return this.operatorDomain + "/scm/" + this.id + "/scm/";
            }
        } else {
            return "";
        }
    }
}