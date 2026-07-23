package com.newzkl.platform.base.biz.account.model.req.web;


// TODO[cross-domain finance]: import com.zkl.scm.finance...ServiceFeeConfigVO;
import com.newzkl.platform.base.biz.account.model.enums.identity.OperatorEnum;
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
    @NotEmpty(message = "username?")
    private String username;
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
     * 运营类型 0 机构 1 行业 2 区域
     */
    @NotNull(message = "运营类型?")
    private OperatorEnum.Type type;
    /**
     * 行业id,区域地址编码 | (多选,拼接)
     */
    @NotNull(message = "运营行业id|区域地址id")
    private String typeForeignId;
    /**
     * 行业名称 / 区域地址
     */
    @NotNull(message = "运营行业|区域地址|品牌名?")
    private String typeForeignName;
    /**
     * 采购金类型 0 自营 1 合作
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
