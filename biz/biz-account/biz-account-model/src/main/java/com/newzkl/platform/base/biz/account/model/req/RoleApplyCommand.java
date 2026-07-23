package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/7/2715:30
 */
@Data
public class RoleApplyCommand implements Serializable {
    /**
     * 主体类型 0 个人 1 企业
     */
    private AccountEnum.BodyType bodyType;
    /**
     * 申请角色
     */
    private RoleEnum.CompanyRole role;
    /**
     * 企业信息
     */
    private String companyInfoVO;
    /**
     * 实名认证信息
     */
    private String nameAuthInfoVO;
}
