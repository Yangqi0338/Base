package com.newzkl.platform.base.biz.finance.domain.hf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.check.CheckCommand;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.ddd.model.enums.finance.HuifuEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

/**
 * 基础内部类
 *
 * @author kc
 */
abstract class Base {

    /**
     * 文件列表子项
     */
    @Data
    static class FileListItem {

        /**
         * 文件类型
         */
        @NotBlank(message = "文件类型不能为空")
        @Size(max = 8, message = "文件类型长度不能超过8位")
        private String file_type;

        /**
         * 文件jfileID
         */
        @NotBlank(message = "文件jfileID不能为空")
        @Size(max = 128, message = "文件jfileID长度不能超过128位")
        private String file_id;

        /**
         * 文件名称
         */
        @Size(max = 128, message = "文件名称长度不能超过128位（128位英文字符或64个汉字）")
        private String file_name;
    }

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 15:22:17
     */
    @Data
    static class Res implements Serializable {

        private String resp_code;
        private String resp_desc;
        private String huifu_id;

        public boolean isSuccess() {
            return HuifuEnum.isSuccess(resp_code);
        }

    }

    /**
     * 若是异步,应该在拦截器做一层解套
     * @author niu
     */
    @Data
    static class AsyncRes<T> extends Base.Res {

        // TODO 验签
        private String sign;

        private T data;

        public <T extends AsyncRes> T build() {
            if (data instanceof NotifyRes) {
                ((NotifyRes) data).build(this);
            }
            return (T) this;
        }
    }

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 16:57:20
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class NotifyRes extends Base.Res {

        /**
         * 业务返回码
         */
        private String sub_resp_code;

        /**
         * 业务返回描述
         */
        private String sub_resp_desc;

        @Override
        public boolean isSuccess() {
            return super.isSuccess() && HuifuEnum.isSuccess(this.getSub_resp_code());
        }

        public <T extends NotifyRes> T build() {
            return build(null);
        }

        public <T extends NotifyRes> T build(Base.Res res) {
            if (res == null) {
                this.setResp_code(this.getSub_resp_code());
                this.setResp_desc(this.getSub_resp_desc());
            } else {
                this.setResp_code(res.getResp_code());
                this.setResp_desc(res.getResp_desc());
            }
            return (T) this;
        }
    }

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 16:57:20
     */
    @Data
    static class Req {

        /**
         * 请求流水号
         */
        @NotBlank(groups = {CheckCommand.class}, message = "请求流水号不能为空")
        @Size(max = 32, message = "请求流水号长度不能超过32位")
        private String req_seq_id;

        /**
         * 请求日期
         */
        @NotBlank(groups = {CheckCommand.class}, message = "请求日期不能为空")
        @Pattern(regexp = "^\\d{8}$", message = "请求日期格式必须为yyyyMMdd")
        private String req_date;

        /**
         * 汇付客户Id
         */
        @NotBlank(groups = {UpdateCommand.class}, message = "汇付客户Id不能为空")
        private String huifu_id;

        public void build(String defaultHuiFuId) {
            if (StrUtil.isBlank(req_seq_id)) {
                this.req_seq_id = UUID.randomUUID().toString().replace("-", "");
            }
            if (StrUtil.isBlank(req_date)) {
                this.req_date = DateUtil.date().toString("yyyyMMdd");
            }
            if (StrUtil.isBlank(huifu_id)) {
                this.huifu_id = defaultHuiFuId;
            }
        }
    }
}
