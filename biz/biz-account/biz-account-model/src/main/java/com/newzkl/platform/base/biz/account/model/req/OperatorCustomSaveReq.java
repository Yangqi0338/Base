package com.newzkl.platform.base.biz.account.model.req;

// TODO[cross-domain finance]: import com.zkl.scm.finance...ServiceFeeConfigVO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 运营商自助注册请求参数
 *
 * @author fang
 */
@Data
public class OperatorCustomSaveReq {
    /**
     * ID
     */
    private Long id;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;
    /**
     * 服务费
     */
    @NotNull(message = "服务费?")
    // TODO[cross-domain finance]: private ServiceFeeConfigVO serviceFeeConfigVO;
    /**
     * 邀请人id
     */
    private Long inviteId;
    /**
     * 采购金类型
     * @ext 0 自营 1 合作; 无对应枚举, 保留 Integer
     */
    private Integer balanceType;

    @NotNull
    /**
     * 域名
     */
    private String domain;
    /**
     * 信息
     */
    private String info;
    /**
     * 杠杆比例
     */
    private Integer leverageRatio;
}
