package com.newzkl.platform.base.biz.account.model.res;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2211:09
 */
@Data
public class SubAccount extends BaseRes {

    /**
     * 主账号id
     */
    private Long mainAccountId;
    /**
     * 昵称 (查询)
     */
    private String nickname;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 密码
     */
    private String password;
    /**
     * 帐号状态（0正常 1停用） (查询)
     */
    private AccountEnum.State state;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 实名认证信息
     */
    private String nameAuthInfo;
    /**
     * 实名认证审批状态
     */
    private Integer nameAuthAuditState;
    /**
     * 子账号数量
     */
    private Integer subAccountCount;
    /**
     * 下级数量
     */
    private Integer belowCount;
    /**
     * 邀请人账号ID, json格式, 每个角色都可能有邀请人账号ID
     */
    private Long inviteAccountId;
    /**
     * 子账号角色ID集合
     */
    private String roleIdList;
    /**
     * 子账号员工角色ID集合
     */
    private String jobIdList;
    /**
     * 邀请码
     */
    private String yqm;

    /**
     *
     * @param registerRole  要注册的角色
     * @param username      用户名
     * @param password      密码
     * @param mainAccountId 父ID
     * @param state         状态
     * @return
     */
    public void init(List<Long> registerRole, String username, String password, Long mainAccountId, AccountEnum.State state) {
        if (StrUtil.isEmpty(username)) {
            throw new PlatformException(BaseErrorCode.PARAM);
        } else {
            this.username = username;
        }
        //设置密码
        JSONObject passwordObj = new JSONObject();
        for (Long item : registerRole) {
            passwordObj.put(item.toString(), new BCryptPasswordEncoder().encode(password));
        }
        //设置密码
        this.password = JSONObject.toJSONString(passwordObj);
        //设置父账号ID
        this.mainAccountId = mainAccountId == null ? 0 : mainAccountId;
        //默认是启动状态
        if (state == null) {
            this.state = AccountEnum.State.ENABLE;
        } else {
            this.state = state;
        }
        //设置角色ID
        for (Long roleId : registerRole) {
            this.roleIdList = BizUtil.addIdString(this.roleIdList, roleId);
        }
        //设置邀请码
        this.yqm = BizUtil.generate6code();
        //默认实名认证审批状态
        this.nameAuthAuditState = AuditEnum.State.CUSTOM.getCode();
        //默认实名认证审批状态
        this.setNameAuthAuditState(AuditEnum.State.CUSTOM.getCode());
        this.subAccountCount = 0;
        this.belowCount = 0;
    }
}
