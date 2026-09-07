package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 账号 + 身份聚合根
 * <p>
 * 继承 {@link AccountRes}, 账号主体字段直接平铺在聚合根上 (不含密码)。额外携带:
 * <ul>
 *     <li>{@code identityList} — 账号身份列表 (对应 account.identityList, 已解析为枚举)</li>
 *     <li>{@code roleList} — 账号角色编码列表 (前端 v-permission 按 code 判定)</li>
 *     <li>4 个身份槽 — member / emp / supplier / channel, 传了哪个身份就回填哪个槽,
 *         一个账号可同时命中多个槽 (多身份)</li>
 * </ul>
 * 身份表与 account 共用主键, 身份行缺失 (脏数据) 时对应槽为 null。
 *
 * @author KC
 */
@Data
public class AccountAggRes extends AccountRes {

    /**
     * 账号身份列表 (对应 account.identityList, 已解析为枚举)
     */
    private List<AccountEnum.Identity> identityList;

    /**
     * 账号角色编码列表 (前端 v-permission 按 code 判定), 无角色则空列表
     */
    private List<String> roleList;

    /**
     * 会员身份槽, 非会员时为 null
     */
    private MemberVO member;

    /**
     * 员工身份槽, 非员工时为 null
     */
    private EmpVO emp;

    /**
     * 供应商身份槽, 非供应商时为 null
     */
    private SupplierVO supplier;

    /**
     * 渠道商身份槽, 非渠道商时为 null
     */
    private ChannelVO channel;
}
