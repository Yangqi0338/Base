package com.newzkl.platform.base.biz.account.model.req.cmd;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 供应商
 * @date 2023/9/2210:29
 */
@Data
public class AccountCmd {
    @Data
    public static class InitFinance {
        /**
         * 客户id
         */
        private Long accountId;

        /**
         * 客户名称
         */
        private String accountName;

        /**
         * 客户类型
         */
        private RoleEnum.CompanyRole role;

        /**
         * 无需子账户传null
         * 若需开通子账户必传  集合长度==子账户数量  集合元素代表子账户类型
         * 同一accountId,同一账户类型，子账户类型唯一
         */
        private List<Integer> subPurseType;

        /**
         * 上级id 没有上级传0
         */
        private Long parentId;
    }
}
