package com.newzkl.platform.base.common.core.redis;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.enums.ModuleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Redis key 枚举
 *
 * <p>各 biz 域原按域复制同名枚举, 现合并至 core-model 单一副本 (account 版为全集)。
 * 语义非持久化值, 故不实现 IEnum。</p>
 */
public class RedisEnum {

    /**
     * key
     */
    @Getter
    @AllArgsConstructor
    public enum Key {
        /* ============ 用户 ============ */
        /**
         * 应用订单, s = 应用订单ID
         */
        APP_ORDER(ModuleEnum.USER, "app_order:%s", "应用订单, s = 应用订单ID"),
        /** 入会礼包预单缓存, s = 礼包订单ID */
        PACK_ORDER_PRE_CACHE(ModuleEnum.USER, "packOrderPre:%s", "入会礼包预单缓存, s = 礼包订单ID"),
        /** 预支付订单缓存, s1 = 门店ID, s2 = 账号ID */
        PRE_PAY_ORDER(ModuleEnum.COMMON, "prePayOrder:%s-%s", "预支付订单缓存, s1 = 门店ID, s2 = 账号ID"),
        /** 父id列表, s = 账号ID */
        PID_LIST(ModuleEnum.USER, "AMC:pidList:%s", "父id列表, s = 账号ID"),
        /** 父角色列表, s = 账号ID */
        PROLE_LIST(ModuleEnum.USER, "AMC:pRoleList:%s", "父角色列表, s = 账号ID"),
        /** 子结构列表, s = 账号ID */
        SUB_STRUCTURE(ModuleEnum.USER, "AMC:subStructure:%s", "子结构列表, s = 账号ID"),
        /**
         * 角色申请资料, s1 = 账号ID, s2 = 角色ID
         * <p>沿用旧常量 {@code RoleEnum.ApplyCommandRedisKeyPre} 的裸 key 形态 (无模块前缀),
         * 以便与旧数据共存。</p>
         */
        ROLE_APPLY_COMMAND(ModuleEnum.COMMON, "role:roleApplyCommand:%s:%s", "角色申请资料, s1 = 账号ID, s2 = 角色ID"),

        /* ============ 商品 ============ */
        /** 商品信息, s = SKU ID */
        SKU(ModuleEnum.GOODS, "sku:%s", "商品信息, s = SKU ID"),
        /** 渠道商商品关系, s = 渠道商ID */
        CHANNEL_RELATION(ModuleEnum.GOODS, "relation:%s", "渠道商商品关系, s = 渠道商ID"),
        /** 三方商品销售范围检查结果 */
        OUT_SKU_CHECK(ModuleEnum.GOODS, "out_sku:check:%s", "三方商品销售范围检查结果"),
        /** 运费模板, s = 运费模板ID */
        FREIGHT_TEMPLATE(ModuleEnum.GOODS, "freight_template:%s", "运费模板, s = 运费模板ID"),
        /** 渠道商服务费, s = 渠道商ID */
        FEE_CONFIG(ModuleEnum.GOODS, "order:fee:%s", "渠道商服务费, s = 渠道商ID"),
        /** 渠道商分润配置, s = 渠道商ID */
        SHARE_CONFIG(ModuleEnum.GOODS, "order:share:%s", "渠道商分润配置, s = 渠道商ID"),
        /** 供应商结算节点, s = 供应商ID */
        SETTLE_ORDER_TYPE(ModuleEnum.GOODS, "order:settle_type:%s", "供应商结算节点, s = 供应商ID"),
        /** 支付状态, s1 = 订单类型, s2 = 订单ID */
        PAYMENT_STATE(ModuleEnum.GOODS, "payment:state:%s:%s", "支付状态, s1 = 订单类型, s2 = 订单ID"),

        /* ============ 财务 ============ */
        /** 供应商结算数据 */
        SUPPLIER_SETTLE_DATA_CACHE(ModuleEnum.FINANCE, "SUPPLIER_SETTLE_DATA_CACHE", "供应商结算数据"),
        /** 运营商杠杆等级 */
        CONFIG_CACHE_PREFIX(ModuleEnum.FINANCE, "operatorLever", "运营商杠杆等级"),
        /** 等待分润金额, s = 账号id */
        WAIT_EARNING_AMOUNT(ModuleEnum.FINANCE, "waitEarningAmount:%s", "等待分润金额, s = 账号id"),
        /** 总分润金额 */
        TOTAL_EARNING_AMOUNT(ModuleEnum.FINANCE, "totalEarningAmount", "总分润金额"),

        /* ============ 订单 ============ */
        /** 订单支付锁, s = 订单编号 */
        ORDER_PAY_LOCK_PRE(ModuleEnum.FINANCE, "orderPayLock:%s", "订单支付锁, s = 订单编号"),
        /** 订单支付信息缓存, s = 订单编号 */
        ORDER_PAY_CACHE_PRE(ModuleEnum.FINANCE, "orderPayCache:%s", "订单支付信息缓存, s = 订单编号"),

        /* ============ 系统 ============ */
        /** 短信分钟发送限制 */
        SMS_SEND_COUNT_MINUTE(ModuleEnum.COMMON, "sms_limit:minute", "短信分钟发送限制"),
        /** 短信小时发送限制 */
        SMS_SEND_COUNT_HOUR(ModuleEnum.COMMON, "sms_limit:hour", "短信小时发送限制"),
        /** SMS */
        SMS(ModuleEnum.COMMON, "SMS:%s", "SMS"),
        /** 账号权限 code 列表缓存, s = 账号ID */
        ACCOUNT_PERM(ModuleEnum.COMMON, "perm:account:%s", "账号权限 code 列表缓存, s = 账号ID"),
        /** 账号角色 code 列表缓存, s = 账号ID */
        ACCOUNT_ROLE(ModuleEnum.COMMON, "role:account:%s", "账号角色 code 列表缓存, s = 账号ID"),

        /* ============ 互动统计 ============ */
        /** 互动统计缓存, s1 = 目标类型, s2 = 目标ID (格式 store:stat:type:ID) */
        STORE_STAT(ModuleEnum.COMMON, "store:stat:%s:%s", "互动统计缓存, s1 = 目标类型, s2 = 目标ID"),
        /** 互动统计同步KEY集合 (Set) */
        STORE_STAT_SYNC_SET(ModuleEnum.COMMON, "store:stat:sync:keys", "互动统计同步KEY集合"),
        /** 互动统计锁, s1 = 目标类型, s2 = 目标ID (格式 store:stat:lock:type:ID) */
        STORE_STAT_LOCK(ModuleEnum.COMMON, "store:stat:lock:%s:%s", "互动统计锁, s1 = 目标类型, s2 = 目标ID"),
        /** 互动统计增量更新锁, s = 统计缓存key (格式 lock:finalValue:store:stat:type:ID) */
        STORE_STAT_INCR_LOCK(ModuleEnum.COMMON, "lock:finalValue:%s", "互动统计增量更新锁, s = 统计缓存key"),
        /** 互动统计定时同步任务锁 */
        STORE_STAT_SYNC_TASK_LOCK(ModuleEnum.COMMON, "store:stat:sync:task:lock", "互动统计定时同步任务锁"),

        /* ============ 课程 ============ */
        /** 章节观看事件缓冲队列 (RDeque, 待落库观看事件 JSON) */
        COURSE_CHAPTER_WATCH_BUCKET(ModuleEnum.COMMON, "course:chapter:watch:bucket", "章节观看事件缓冲队列"),
        /** 章节观看同步任务锁 (防多实例重复执行) */
        COURSE_CHAPTER_WATCH_SYNC_TASK_LOCK(ModuleEnum.COMMON, "course:chapter:watch:sync:task:lock", "章节观看同步任务锁"),
        /** 课程购买防并发锁, s1 = 用户ID, s2 = 课程ID */
        COURSE_PURCHASE_LOCK(ModuleEnum.COMMON, "course:purchase:lock:%s:%s", "课程购买防并发锁, s1 = 用户ID, s2 = 课程ID"),

        /* ============ 找回密码 ============ */
        /** 找回密码 nonce 分布式锁, s = nonce */
        RESET_PWD_NONCE_LOCK(ModuleEnum.COMMON, "reset:pwd:nonce:lock:%s", "找回密码 nonce 分布式锁, s = nonce"),
        /** 找回密码「设备+手机号」验证通过标记, s1 = 手机号, s2 = 设备码 */
        RESET_PWD_DEVICE(ModuleEnum.COMMON, "reset:pwd:device:%s:%s", "找回密码设备验证通过标记, s1 = 手机号, s2 = 设备码"),
        /** 找回密码 nonce 防重放标记, s = nonce */
        RESET_PWD_NONCE(ModuleEnum.COMMON, "reset:pwd:nonce:%s", "找回密码 nonce 防重放标记, s = nonce"),
        /** 找回密码接口限流, s = 限流标识 */
        RESET_PWD_RATE_LIMIT(ModuleEnum.COMMON, "reset:pwd:rate:%s", "找回密码接口限流, s = 限流标识"),

        /* ============ 黄金价格 ============ */
        /** 黄金实时价格缓存 */
        GOLD_REAL_TIME_PRICE(ModuleEnum.COMMON, "GoldRealTimePrice::realTimePrice", "黄金实时价格缓存"),
        /** 黄金价格更新时间缓存 */
        GOLD_UPDATE_TIME(ModuleEnum.COMMON, "GoldRealTimePrice::updateTime", "黄金价格更新时间缓存"),

        /* ============ 活动 ============ */
        /** 活动其它配置缓存, s = 配置ID */
        ACTIVITY_OTHER_CONFIG(ModuleEnum.COMMON, "otherConfig:%s", "活动其它配置缓存, s = 配置ID"),

        /** 短信验证码存储 Hash 大 key (field = 手机号) */
        SMS_CODE_HASH(ModuleEnum.COMMON, "SMS:", "短信验证码存储 Hash 大 key"),

        EMPTY(ModuleEnum.COMMON, "", ""),
        UN_PERMISSION_KEY_PREFIX(ModuleEnum.COMMON, "gateway:un_permission:", ""),
        INTERFACE_KEY_PREFIX(ModuleEnum.COMMON, "interface:list:", ""),

        ;

        private final ModuleEnum module;
        private final String code;
        private final String value;

        public String getCode(Object... params) {
            return String.format(this.getCode(), params);
        }

        public String getCode() {
            if (module == null || StrUtil.isBlank(module.getCode())) {
                return code;
            }
            return module.getCode() + ":" + code;
        }

        @Override
        public String toString() {
            return getCode();
        }
    }
}
