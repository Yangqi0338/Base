package com.newzkl.platform.base.biz.finance.domain.hf;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.finance.model.enums.finance.HuifuEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 账号相关的内部返回类
 *
 * @author kc
 */
abstract class AccountRes {

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 15:32:54
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class AccountBindAsyncRes extends Base.NotifyRes {

        private String req_seq_id;

        private String req_date;

        private String notify_type;

        private String state;

        private String state_desc;

        @Override
        public boolean isSuccess() {
            // TODO 根据具体业务判断
//            return super.isSuccess() && ;
            return super.isSuccess();
        }
    }

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 15:32:54
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class AccountBindSyncRes extends UserRes {

        private String token_no;

        private String apply_no;

        private List<RespBusiness> resp_business;

        @Override
        public boolean isSuccess() {
            if (CollUtil.isEmpty(this.getResp_business())) {
                return false;
            }

            return super.isSuccess() &&
                    this.getResp_business().stream().allMatch(it ->
                            HuifuEnum.BizCodeEnum.isSuccess(it.getCode()));
        }

        @Data
        public static class RespBusiness {
            private String type;
            private String code;
            private String msg;
        }
    }

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 15:25:03
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class OpenAccountRes extends UserRes {

        private String login_name;

        private String login_password;

    }

    /**
     * 账号基础类
     *
     * @author niu
     * @description:
     * @date 2025-08-25 15:25:03
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class UserRes extends Base.Res {

    }

}
