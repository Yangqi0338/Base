package com.newzkl.platform.base.biz.socialbang.infrastructure.utils;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 活动分红周期时间工具, 迁移自 scm-common ScmDateUtil 中奖金池所需子集
 *
 * @author niu
 */
public class ActivityDateUtil {

    private ActivityDateUtil() {
    }

    /**
     * 按分红周期类型取下一个结算起点时间戳
     *
     * @param type 周期类型 1:次日 2:七天后 3:次月一号
     * @return 下一周期起点时间戳(毫秒)
     */
    public static Long getNextDateByRepeatType(Long type) {
        Date date = new Date();
        if (type.equals(1L)) {
            return getNextDayStartTimeStamp(date);
        }
        if (type.equals(2L)) {
            return startDayNumForStamp(date, 7);
        }
        if (type.equals(3L)) {
            return getNextMonthStartTimeStamp(date);
        }
        ThrowsException.exception(BaseErrorCode.PARAM);
        return null;
    }

    /**
     * 取次日零点时间戳
     *
     * @param date 基准时间
     * @return 次日零点时间戳(毫秒)
     */
    public static long getNextDayStartTimeStamp(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        setDayStart(calendar);
        return calendar.getTime().getTime();
    }

    /**
     * 取 N 天后零点时间戳
     *
     * @param date 基准时间
     * @param num  天数偏移
     * @return N 天后零点时间戳(毫秒)
     */
    public static Long startDayNumForStamp(Date date, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        setDayStart(calendar);
        return calendar.getTime().getTime();
    }

    /**
     * 取次月一号零点时间戳
     *
     * @param date 基准时间
     * @return 次月一号零点时间戳(毫秒)
     */
    public static long getNextMonthStartTimeStamp(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        setDayStart(calendar);
        long newtime = calendar.getTime().getTime();
        return strDateForStamp(stampForStrDate(newtime, "yyyy-MM") + "-01", "yyyy-MM-dd");
    }

    private static void setDayStart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static String stampForStrDate(long timestamp, String format) {
        return new SimpleDateFormat(format).format(new Date(timestamp));
    }

    private static long strDateForStamp(String dateStr, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateStr).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
}
