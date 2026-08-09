package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 会员登录或注册请求参数
 */
@Data
public class MemberLoginOrRegisterReq implements Serializable {

    /**
     * 登录凭证码
     */
    @NotBlank(message = "code?")
    String code;
    /**
     * 账号ID
     */
    Long accountId;

    /**
     * 性别
     * @ext 0 未知 1 男 2 女; 无对应枚举, 保留 Integer
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 常住地-省份
     */
    private String residenceProvince;

    /**
     * 常住地-城市
     */
    private String residenceCity;

    /**
     * 常住地-区县
     */
    private String residenceDistrict;
}
