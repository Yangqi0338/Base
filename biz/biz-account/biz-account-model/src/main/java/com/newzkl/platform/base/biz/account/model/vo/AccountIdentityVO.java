package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 账号基础VO
 *
 * @author fang
 */
@Data
public class AccountIdentityVO extends BaseVO {

    /**
     * 会员账号
     */
    private String userAccount;
    /**
     * 会员头像
     */
    private String head;
    /**
     * 会员昵称
     */
    private String nickname;

}