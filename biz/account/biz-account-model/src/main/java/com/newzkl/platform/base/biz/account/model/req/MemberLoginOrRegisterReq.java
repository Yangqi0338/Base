package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class MemberLoginOrRegisterReq implements Serializable {

    @NotBlank(message = "code?")
    String code;
    /**
     * 渠道商ID
     */
    Long accountId;

    /**
     * 性别：0-未知，1-男，2-女
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
