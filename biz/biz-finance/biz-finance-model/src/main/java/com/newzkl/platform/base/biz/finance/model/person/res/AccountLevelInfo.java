package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;

/**
 * 连连账户等级信息 (个人开户回调 {@code accountInfo} 节点)。
 *
 * <p>迁移自 new-scm {@code application.utils.model.res.ParsonPurseLeve}
 * (旧名拼写错误 Parson/Leve, 迁移时更正为 {@code AccountLevelInfo})。</p>
 *
 * @author KC
 */
@Data
public class AccountLevelInfo {

    /**
     * 账户类型。
     */
    private String account_type;

    /**
     * 账户等级。
     */
    private String account_level;

    /**
     * 待升级账户类型。
     */
    private String account_need_type;

    /**
     * 待升级账户等级。
     */
    private String account_need_level;
}
