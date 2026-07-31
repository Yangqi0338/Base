package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fang
 */
public class MemberCmd {
    @Data
    public static class ID {
        /** 会员ID */
        private Long memberId;
    }

    @Data
    public static class IDList {
        /** 会员ID列表 */
        @NotEmpty
        private List<Long> memberIdList;
    }

    @Data
    public static class Edit {
        /** 主键ID */
        private Long id;
        /** 会员命令 */
        private MemberReq memberCommand;
    }

    @Data
    public static class WxLogin {
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
}
