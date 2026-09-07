package com.newzkl.platform.base.biz.account.model.res;


import com.newzkl.platform.base.biz.account.model.dto.MemberDTO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员聚合视图
 *
 * <p>{@code GET /account/member/detail} 详情出参。字段按来源分三区排列: 主数据 member 自有列
 * (继承 {@link MemberDTO})、副数据 account 列、暂无数据源的旧契约统计字段。只需会员自有列时
 * 改走 {@code GET /account/member/base}(出参 {@link MemberDTO}), 少一次 account 查询</p>
 *
 * <p>⚠️ <b>对前端的契约变更</b>(与 {@code ChannelRes} / {@code SupplierRes} 同批, 后端不做兼容映射,
 * 由前端改):</p>
 * <ul>
 *   <li>{@code gender} 由 {@code Integer} 改为枚举 —— {@code PersonalEnum.Gender} 带
 *       {@code @JsonValue}, JSON 出参仍是 0/1 数值, 前端无感</li>
 *   <li>{@code residenceProvince} / {@code residenceCity} / {@code residenceDistrict} 三字段
 *       合并为 {@code residence} 单列 —— {@code member} 表本就只有 {@code residence} 一列,
 *       三拆分字段全仓无数据源, 原本恒为 null</li>
 *   <li>{@code merchantId} / {@code pid} 删除 —— 全仓无数据源, 原本恒为 null。邀请关系改看
 *       {@code inviteId} / {@code yqm}</li>
 * </ul>
 *
 * @author fang
 * @ext 主数据 member, 副数据 account(单副, 副数据不再向下关联)。与
 *      {@code AccountController.identityDetail} 的「主 account / 副身份」方向相反, 两者不可互相替代
 */
@Data
public class MemberRes extends MemberDTO {

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
     * 邀请人账号ID, 取 account.invite_account_id
     */
    private Long inviteId;


    /**
     * 统计：成交笔数
     */
    private Integer countDealNumber;
    /**
     * 统计：成交金额 (Money, 落库 BIGINT 分)
     */
    private Money countDealAmount;
}
