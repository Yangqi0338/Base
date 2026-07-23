package com.newzkl.platform.base.biz.store.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fang
 */
public class AuditEnum {

    public static final String END_STEP_CODE = "end";

    @Getter
    @AllArgsConstructor
    public enum TemplateType {
        ROLE_APPLY(1L,"角色申请审批模板"),
        PROMISE_FLOW(2L,"保证金缴纳审批模板"),
        SPU_CREATE(3L,"SPU上传审批模板"),
        BRAND_CREATE(4L,"品牌申请审批模板"),
        SPU_WORK_TABLE(5L,"SPU工单审批模板"),
        NAME_AUTH(6L,"实名认证审批模板"),
        ;
        private Long code;
        private String value;
    }
    //(0 待用户提交 1 待审核  2 通过 3 未通过)
    @Getter
    @AllArgsConstructor
    public enum State {
        CUSTOM(0,"待用户提交"),
        AUDITING(1,"待审核"),
        SUCCESS(2,"通过"),
        FAIL(3,"未通过"),
        STOP(4,"终止"),
        ;
        private Integer code;
        private String value;
        /**
         * 通过code获取枚举实例
         */
        public static State getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (State state : State.values()) {
                if (state.getCode().equals(code)) {
                    return state;
                }
            }
            return null;
        }
    }
    @Getter
    @AllArgsConstructor
    public enum Action {
        REFUSE(0,"拒绝"),
        PASS(1,"通过"),
        ;
        private Integer code;
        private String value;
    }
}
