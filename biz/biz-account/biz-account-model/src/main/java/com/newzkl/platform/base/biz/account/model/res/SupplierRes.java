package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.biz.account.model.dto.SupplierDTO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商聚合视图
 *
 * <p>{@code GET /user/supplier/supplier} 详情出参与 {@code supplierPage} 分页出参共用。字段按来源
 * 分三区排列: 主数据 supplier 自有列 (继承 {@link SupplierDTO})、副数据 account 列、暂无数据源的
 * 旧契约统计字段。只需供应商自有列时改走 {@code GET /user/supplier/base}(出参
 * {@link SupplierDTO}), 少一次 account 查询</p>
 *
 * <p><b>分页与详情的填充差异</b>: 分页走 {@code SupplierDAO.pageListWithAccount}, 联表只取
 * {@code a.username} / {@code a.real_name}, 其余 account 列为 null; 详情走
 * {@code SupplierClientDomain.supplier} 由 domain 逐字段显式填满</p>
 *
 * <p>⚠️ <b>对前端的契约变更</b>(与 {@code ChannelRes} 同批, 后端不做兼容映射, 由前端改):</p>
 * <ul>
 *   <li>{@code state} / {@code auditState} / {@code promisePayAuditState} 由 {@code Integer}
 *       改为枚举 —— 原先 MapStruct 用 {@code ordinal()} 强转, 与枚举 code 不保证一致, 属修 bug</li>
 *   <li>{@code identity} / {@code roleName} / {@code bodyType} 三字段删除 —— 全仓无数据源,
 *       原本恒为 null。身份由端点语义隐含</li>
 * </ul>
 *
 * @author fang
 * @ext 主数据 supplier, 副数据 account(单副, 副数据不再向下关联)。与
 *      {@code AccountController.identityDetail} 的「主 account / 副身份」方向相反, 两者不可互相替代
 */
@Data
public class SupplierRes extends SupplierDTO {

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
     * 上级甄选师ID, 取 account.invite_account_id
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
     * 待售卖的商品
     */
    private Integer goodsNotSaleCount;
    /**
     * 商品总金额
     */
    private Money goodsSaleAmount;
    /**
     * 商品月金额
     */
    private Money monthGoodsSaleAmount;
    /**
     * 商品总销量
     */
    private Integer goodsSaleCount;
    /**
     * 商品月销量
     */
    private Integer monthGoodsSaleCount;
    /**
     * 商品总成交销量
     */
    private Integer goodsDealCount;
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
