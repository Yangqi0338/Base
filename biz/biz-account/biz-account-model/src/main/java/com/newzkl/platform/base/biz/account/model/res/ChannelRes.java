package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.biz.account.model.dto.ChannelDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 渠道商聚合视图
 *
 * <p>{@code GET /user/channel/channel} 出参, 调用方 yys-admin。字段按来源分三区排列:
 * 主数据 channel 自有列、副数据 account 列、暂无数据源的旧契约字段。只需渠道商自有列时改走
 * {@code GET /user/channel/base}(出参 {@code ChannelDTO}), 少一次 account 查询</p>
 *
 * <p><b>合并来源</b>: 由旧出参 {@code ChannelVO} 与本类原有字段合并。已<b>删除</b>本类 6 个全仓
 * 无数据源的自有字段 —— {@code upDealerId} / {@code upOperatorId}(上级交易师与运营商链) /
 * {@code bodyType} / {@code roleName} / {@code dealerEarnings} / {@code channelType}(自营渠道已废弃)。
 * 前两个 yys-admin 有引用(如 {@code channelDetails.vue:450} 查上级交易师), 属能力缺失待业务
 * 确认存废, 不是改名</p>
 *
 * <p>⚠️ <b>对前端的契约变更</b>(2026-07-30 用户裁决: 后端不做兼容映射, 由前端改):</p>
 * <ul>
 *   <li>{@code realname} → {@code realName} —— {@code yys-admin/src/views/user/channelDetails.vue:266}
 *       有 {@code result.realname.replace(...)}, 不改会抛 TypeError 整页白屏</li>
 *   <li>{@code roleId}(Long) + {@code roleName}(String) 双双删除 —— 本端点只服务渠道商, 身份由
 *       端点语义隐含, 无需出参携带; 角色名全仓无数据源。非改名, 前端需去掉这两个字段的读取</li>
 *   <li>{@code creator} / {@code updater} 由 {@code ExecutorDTO} 平展字段取代(全仓统一改造,
 *       实测 6 个前端仓零引用, 无需处理)</li>
 * </ul>
 *
 * @author fang
 * @ext 主数据 channel, 副数据 account(单副, 副数据不再向下关联)。与
 *      {@code AccountController.identityDetail} 的「主 account / 副身份」方向相反, 两者不可互相替代
 */
@Data
public class ChannelRes extends ChannelDTO {

    /**
     * 登录名称(手机号)
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String head;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邀请码
     */
    private String yqm;
    /**
     * 账号状态 (DISABLE 禁用 / ENABLE 启用 / DESTROY 已注销)
     */
    private AccountEnum.State accountState;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 三方账户权限
     */
    private CommonEnum.YesOrNo tripartiteAccountPermission;



    /**
     * 总订单笔数
     */
    private Integer totalOrderNumber;
    /**
     * 总售后笔数
     */
    private Integer totalRefundNumber;
    /**
     * 总售后金额
     */
    private Money totalRefundAmount;
}
