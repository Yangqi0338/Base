package com.newzkl.platform.base.biz.account.model.vo;

import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员+账号关联分页返回VO
 *
 * @author fang
 */
@Data
public class MemberAccountVO {
    /**
     * 会员ID
     */
    private Long id;
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
    /**
     * 会员状态
     */
    private AccountEnum.State state;
    /**
     * 会员创建时间
     */
    private LocalDateTime createTime;
    /**
     * 账号手机号
     */
    private String phone;
    /**
     * 账号上级ID（pid）
     */
    private Long pid;
    /**
     * 会员账号
     */
    private String pUserAccount;
    /**
     * 会员昵称
     */
    private String pNickname;
    /**
     * 建群数量
     */
    private Integer groupNum;
}