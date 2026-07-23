package com.newzkl.platform.base.biz.finance.model.enums.order;

/**
 * @Description: 运费相关枚举
 * @Author: niu
 * @Date: 2023/4/27 17:01
 */
public class PackOrderEnum {

    /**
     * 状态
     */
    public enum State {
        /**
         * 新订单
         */
        NEW(0, "新订单"),
        /** 待付款 */
        WAIT_PAY(2, "待付款"),
        /** 待发货 */
        WAIT_DELIVERY(4, "待发货"),
        /** 待收货 */
        WAIT_RECEIVE(6, "待收货"),
        /** 已收货 */
        DOWN_RECEIVE(8, "已收货"),
        /** 已完成 */
        SUCCESS(10, "已完成"),
        /** 已关闭 */
        CLOSE(-1, "已关闭"),
        ;

        private Integer code;
        private String info;

        State(Integer code, String info) {
            this.code = code;
            this.info = info;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
