package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.biz.account.model.dto.EmpDTO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 员工聚合视图
 *
 * <p>{@code GET /user/emp/detail} 详情出参。字段按来源分两区排列: 主数据 emp 自有列
 * (继承 {@link EmpDTO}, 只有 {@code type} 一列)、副数据 account 列。只需员工类型时改走
 * {@code GET /user/emp/base}(出参 {@link EmpDTO}), 少一次 account 查询</p>
 *
 * <p>⚠️ <b>对前端的契约变更</b>(与 {@code ChannelRes} / {@code SupplierRes} / {@code MemberRes}
 * 同批, 后端不做兼容映射, 由前端改)。本类原先全仓零调用({@code EmpAssembler.vo2Res} 无调用方),
 * 故实际无存量前端受影响:</p>
 * <ul>
 *   <li>{@code accountId} 更名为 {@code pid} —— 原字段注释即「父id」, 取 {@code account.pid},
 *       统一沿用 account 侧原名</li>
 *   <li>{@code jobId} / {@code jobName} 删除 —— {@code emp} 表无岗位列, 全仓无数据源,
 *       原本恒为 null。且 {@code jobName} 声明为 {@code Long}, 本身即缺陷</li>
 *   <li>{@code roleIdList} 删除 —— 角色绑定收敛到 account 侧关联表, 员工出参不再冗余携带,
 *       与 {@code ChannelRes} / {@code SupplierRes} 口径一致</li>
 * </ul>
 *
 * @author fang
 * @ext 主数据 emp, 副数据 account(单副, 副数据不再向下关联)。与
 *      {@code AccountController.identityDetail} 的「主 account / 副身份」方向相反, 两者不可互相替代
 */
@Data
public class EmpRes extends EmpDTO {

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
     * 上级账号ID, 取 account.pid
     */
    private Long pid;
}
