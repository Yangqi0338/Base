package com.newzkl.platform.base.biz.account.model.req.web;


// TODO[cross-domain finance]: import com.zkl.scm.finance...ServiceFeeConfigVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 市场运营商
 *
 * @author fang
 */
@Data
public class OperatorProxySaveReq {
    /** 账号 */
    @NotEmpty(message = "username?")
    private String username;
    /** 密码 */
    @NotEmpty(message = "password?")
    private String password;
    /**
     * 昵称 (查询)
     */
    private String name;
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号?")
    private String phone;
    /**
     * 服务费
     */
    @NotNull(message = "服务费?")
    // TODO[cross-domain finance]: private ServiceFeeConfigVO serviceFeeConfigVO;
    /**
     * 运营类型
     * @ext 前端传数字 code, Jackson 经 OperatorEnum.Type 的 @JsonValue 反序列化为枚举
     */
    @NotNull(message = "运营类型?")
    private OperatorEnum.Type type;
    /**
     * 行业id/区域地址编码
     * @ext 多选时拼接
     */
    @NotNull(message = "运营行业id|区域地址id")
    private String typeForeignId;
    /**
     * 行业名称 / 区域地址
     */
    @NotNull(message = "运营行业|区域地址|品牌名?")
    private String typeForeignName;
    /**
     * 采购金类型
     * @ext 0 自营 1 合作; 无对应枚举, 保留 Integer
     */
    @NotNull
    private Integer balanceType;

    /**
     * 域名
     */
    @NotNull
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
