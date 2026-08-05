package com.newzkl.platform.base.common.core.model.enums;

/**
 * 缓存 key 常量集
 *
 * <p>各 biz 域原按域复制同名常量接口, 现合并至 core-model 单一副本 (account 版为全集, 无键名/值冲突)</p>
 */
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
