package com.newzkl.platform.base.biz.auth.model.oauth.res;


import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;

import java.util.List;

/**
 * 登录响应结果
 *
 * @author muc_fang
 * @date 2024/2/22 11:23
 */
@Data
public class LoginRes {
    /**
     * 账号信息
     */
    private LoginAccountRes accountVO;
    /**
     * Token
     */
    private String token;
    /**
     * 客户端
     */
    private List<AccountEnum.Client> client;
    /**
     * 角色
     */
    private AccountEnum.Identity identity;
    /**
     * 供应商状态
     *
     * <p>仅当登录端为 {@link AccountEnum.Client#SUPPLIER} 时填充, 前端据此判断:
     * INIT(未开通) 跳转供应商角色申请页填写公司法人信息, 提交后转 AUDITING(审核申请中)</p>
     */
    private SupplierEnum.State supplierState;
    /**
     * 供应商保证金缴纳审核状态
     *
     * <p>非强制业务态, 仅供应商端登录时随 supplierState 一并回填</p>
     */
    private AuditEnum.State promisePayAuditState;
}
