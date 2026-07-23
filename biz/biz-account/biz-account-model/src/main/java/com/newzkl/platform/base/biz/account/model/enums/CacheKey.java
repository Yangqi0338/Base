package com.newzkl.platform.base.biz.account.model.enums;

public interface CacheKey {

    String AMC_PID_LIST = "AMC:pidList:%s";
    String AMC_PROLE_LIST = "AMC:pRoleList:%s";
    String AMC_SUB_STRUCTURE = "AMC:subStructure:%s";

    String PAYMENT_STATE = "payment:state:{}:{}";

    String INTEREST_PROJECT = "project:interest:{}";
    String PROJECT_INTEREST_NUM = "project:interestNum:{}";

    /**
     * 用户关联门店ID列表，参数: accountId
     */
    String STORE_ACCOUNT_RELATION = "store:account:relation:{}";

}
