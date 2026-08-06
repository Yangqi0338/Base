package com.newzkl.platform.base.biz.account.model.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.biz.account.model.rpc.StoreOutVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountRoleVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户账号外部视图
 *
 * @author fang
 */
@Data
public class AccountOutRes extends BaseRes {
     /**
      * 主账号id
      */
     private Long mainAccountId;
    /**
     * 父ID (查询)
     */
     private Long pid;
     /**
     * 昵称 (查询)
     */
     private String nickname;
     /**
      * 登录名称
     */
     private String username;
     /**
      * 真实姓名
      */
     private String realName;
     /**
      * 帐号状态
     */
     private AccountEnum.State state;
     /**
     * 上次登录时间
     */
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
     private LocalDateTime lastLoginTime;
     /**
     * 实名认证信息
     */
     private String nameAuthInfo;
     /**
     * 子账号数量
     */
     private Integer subAccountCount;
     /**
     * 下级数量
     */
     private Integer belowCount;
     /**
      * 供应商信息
      */
     private SupplierOutRes supplier;
     /**
      * 渠道商信息
      */
     private ChannelOutRes channel;
     /**
      * 交易师信息
      */
     private DealerOutRes dealer;
     /**
      * 运营商信息
      */
     private OperatorOutRes operator;
     /**
      * 甄选师信息
      */
     private SelectorOutRes selector;
    /**
     * 员工信息
     */
    private EmpOutRes emp;
    /**
     * C端用户信息
     */
    private MemberOutRes member;
    /**
     * 商店信息
     */
    private StoreOutVO store;
     /**
      * 账号角色ID集合
      */
     private String roleIdList;
     /**
      * 账号已开通角色申请信息列表
      *
      * <p>迁移自旧 {@code AccountOutVO#accountRoleVOList}。
      * 中台仅迁移出参模型，角色查询仓储暂未接通，当前恒为 null，
      * 待 {@code RoleController#userRoleInfo} 链路打通后回填。</p>
      */
     private List<AccountRoleVO> accountRoleVOList;
     /**
      * 邀请码 6位
      */
     private String yqm;
    /**
     * 手机号
     */
    private String phone;
     /**
      * 是否设置密码
      */
     private CommonEnum.YesOrNo isSetPassword;
     /**
      * 安全指数
      */
     private Integer safeIndex;
     /**
      * 是否认证
      */
     private Integer isAuth;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * IM同步状态
     */
    private AccountEnum.ImSyncState imSyncStatus;

    /**
     * 头像
     */
    private String head;

}