package com.newzkl.platform.base.common.core.sms;


import lombok.Data;

/**
 * 短信响应封装类
 */
public class SmsRes {

    /**
     * 联麓短信发送响应体
     */
    @Data
    public static class LianLuSendMsgRes {

        /**
         * 状态
         */
        private String status;

        /**
         * 状态消息
         */
        private String message;

        /**
         * 判断短信是否发送成功
         *
         * @return 状态码为"00"时返回 true
         */
        public boolean isSuccess() {
            return "00".equals(status);
        }
    }

}