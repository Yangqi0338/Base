package com.newzkl.platform.base.biz.course.infrastructure.convert;

/**
 * 课程域单位换算工具
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.model.convert.CourseChapterConvertUtil}
 * 的换算部分。对外契约(入参/出参)用元与秒, 库中用分与百分秒, 换算全部收敛于本类,
 * 只在仓储实现的读写边界调用。</p>
 *
 * @author KC
 */
public final class CourseUnitConverter {

    private CourseUnitConverter() {
    }

    /**
     * 元转分
     *
     * @param yuan 金额, 单位元
     * @return 金额, 单位分, 入参为空返回 null
     */
    public static Long yuanToFen(Double yuan) {
        if (yuan == null) {
            return null;
        }
        return Math.round(yuan * 100);
    }

    /**
     * 分转元
     *
     * @param fen 金额, 单位分
     * @return 金额, 单位元, 入参为空返回 null
     */
    public static Double fenToYuan(Long fen) {
        if (fen == null) {
            return null;
        }
        return fen / 100.0;
    }

    /**
     * 秒转百分秒
     *
     * @param seconds 时长, 单位秒, 保留 2 位小数
     * @return 时长, 单位百分秒, 入参为空或负数返回 0
     */
    public static Integer secondsToCentisecond(Double seconds) {
        if (seconds == null || seconds < 0) {
            return 0;
        }
        return (int) Math.round(seconds * 100);
    }

    /**
     * 百分秒转秒
     *
     * @param centisecond 时长, 单位百分秒
     * @return 时长, 单位秒, 保留 2 位小数, 入参为空或负数返回 0.00
     */
    public static Double centisecondToSeconds(Integer centisecond) {
        if (centisecond == null || centisecond < 0) {
            return 0.00;
        }
        return centisecond / 100.0;
    }

    /**
     * 百分秒转秒
     *
     * @param centisecond 时长, 单位百分秒
     * @return 时长, 单位秒, 保留 2 位小数, 入参为空或负数返回 0.00
     */
    public static Double centisecondToSeconds(Long centisecond) {
        if (centisecond == null || centisecond < 0) {
            return 0.00;
        }
        return centisecond / 100.0;
    }

    /**
     * 百分秒格式化为时长描述
     *
     * <p>示例: 1008 转 "10秒08毫秒", 6550 转 "1分05秒50毫秒"。</p>
     *
     * @param centisecond 时长, 单位百分秒
     * @return 时长描述, 入参为空或负数按 0 处理
     */
    public static String formatCentisecond(Long centisecond) {
        long value = centisecond == null || centisecond < 0 ? 0L : centisecond;
        long totalSeconds = value / 100;
        long msPart = value % 100;
        long hour = totalSeconds / 3600;
        long minute = (totalSeconds % 3600) / 60;
        long sec = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0 || hour > 0) {
            sb.append(minute).append("分");
        }
        sb.append(sec).append("秒").append(String.format("%02d", msPart)).append("毫秒");
        return sb.toString();
    }
}
