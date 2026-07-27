package com.newzkl.platform.base.common.core.model.exception;

/**
 * 抛出异常
 *
 * @author niu
 */
public class ThrowsException {

    public static void exception(ErrorCode errorCode) {
        throw new PlatformException(errorCode);
    }

    public static void exception(ErrorCode errorCode, Object... params) {
        throw new PlatformException(errorCode.getCode(), errorCode.getMessage(params));

    }

    public static void exception(int code, String msg) {
        throw new PlatformException(code, msg);

    }

    // ========== 格式化方法 ==========

    /**
     * 将错误编号对应的消息使用 params 进行格式化。
     *
     * @param code           错误编号
     * @param messagePattern 消息模版
     * @param params         参数
     * @return 格式化后的提示
     */
    public static String doFormat(int code, String messagePattern, Object... params) {
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int i = 0;
        int j;
        int l;
        for (l = 0; l < params.length; l++) {
            j = messagePattern.indexOf("{}", i);
            if (j == -1) {
                if (i == 0) {
                    return messagePattern;
                } else {
                    sbuf.append(messagePattern.substring(i));
                    return sbuf.toString();
                }
            } else {
                sbuf.append(messagePattern, i, j);
                sbuf.append(params[l]);
                i = j + 2;
            }
        }
        sbuf.append(messagePattern.substring(i));
        return sbuf.toString();
    }
// ======================== 新增：空值校验方法 ========================

    /**
     * 校验对象是否为 null，若为 null 则抛出指定错误码的异常
     *
     * @param obj       待校验对象（如参数、查询结果）
     * @param errorCode 异常错误码（如 PARAM 表示参数空，NODATA 表示数据不存在）
     * @param objName   对象名称（用于异常消息，如“配置ID”“操作人ID”）
     */
    public static void isNull(Object obj, ErrorCode errorCode, String objName) {
        if (obj == null) {
            // 消息模板：默认“{对象名}不能为空”，复用 errorCode 的 message 进行格式化
            exception(errorCode, objName + "不能为空");
        }
    }

    /**
     * 重载：校验对象是否为 null，默认使用“参数异常”错误码（适用于入参空值校验）
     *
     * @param obj     待校验参数
     * @param objName 参数名称（如“配置ID”“环境标识”）
     */
    public static void isNull(Object obj, String objName) {
        isNull(obj, BaseErrorCode.PARAM, objName);
    }

    /**
     * 重载：校验字符串是否为 null 或空串（trim 后），默认使用“参数异常”错误码
     *
     * @param str     待校验字符串（如配置键、分组名称）
     * @param strName 字符串名称（如“配置键”“分组名称”）
     */
    public static void isBlank(String str, String strName) {
        if (str == null || str.trim().isEmpty()) {
            exception(BaseErrorCode.PARAM, strName + "不能为空或空白");
        }
    }


    // ======================== 新增：布尔断言方法 ========================

    /**
     * 校验布尔值是否为 true，若为 false 则抛出异常（适用于“必须满足某种条件”的场景）
     *
     * @param condition 待校验条件（如“获取锁成功”“更新行数>0”）
     * @param errorCode 异常错误码
     * @param msg       异常消息（说明条件不满足的原因）
     */
    public static void isTrue(boolean condition, ErrorCode errorCode, String msg) {
        if (!condition) {
            exception(errorCode, msg);
        }
    }

    /**
     * 重载：校验布尔值是否为 true，默认使用“操作失败”错误码（适用于业务操作结果校验）
     *
     * @param condition 待校验条件
     * @param msg       操作失败的原因（如“更新配置失败”“删除模板失败”）
     */
    public static void isTrue(boolean condition, String msg) {
        isTrue(condition, BaseErrorCode.EXECUTE, msg);
    }

    /**
     * 校验布尔值是否为 false，若为 true 则抛出异常（适用于“必须不满足某种条件”的场景）
     *
     * @param condition 待校验条件（如“配置已存在”“模板是默认模板”）
     * @param errorCode 异常错误码
     * @param msg       异常消息（说明条件满足的问题）
     */
    public static void isFalse(boolean condition, ErrorCode errorCode, String msg) {
        if (condition) {
            exception(errorCode, msg);
        }
    }

    /**
     * 重载：校验布尔值是否为 false，默认使用“记录已存在”错误码（适用于唯一性校验）
     *
     * @param condition 待校验条件（如“配置键已存在”“模板ID已重复”）
     * @param msg       异常消息（如“配置键在当前环境中已存在”）
     */
    public static void isFalse(boolean condition, String msg) {
        isFalse(condition, BaseErrorCode.EXIST_DATA, msg);
    }
}
