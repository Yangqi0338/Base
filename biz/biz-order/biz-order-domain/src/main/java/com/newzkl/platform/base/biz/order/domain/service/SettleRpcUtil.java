package com.newzkl.platform.base.biz.order.domain.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;


import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2720:02
 */
public class SettleRpcUtil {

    /**
     * 获取下次结算时间
     *
     * @param currentSettleTime  当前结算时间
     * @param settlementTimeType
     * @param settlementTimeDay
     * @return
     */
    public static LocalDateTime getNextSettleTime(DateTime currentSettleTime, Integer settlementTimeType, Integer settlementTimeDay) {
        if (RoleEnum.DataType.MONTH_ONLY.getCode().equals(settlementTimeType)) { // 按月结算 (MONTH_ONLY) ，得到下个月的第 settlementTimeDay 天的 0 点。
            Date nextMonthDayStartTimeStamp = getNextMonthDayStartTimeStamp(settlementTimeDay);
            return DateUtil.toLocalDateTime(nextMonthDayStartTimeStamp);
        } else if (RoleEnum.DataType.GOODS_AUDIT.getCode().equals(settlementTimeType)) {
            Date date = startDayNumForStamp(currentSettleTime, settlementTimeDay); //以当前结算时间为基准，向后推 settlementTimeDay 天（通常是 N 天后的开始时间）。返回该日期的 LocalDateTime。
            return DateUtil.toLocalDateTime(date); // 典型场景：审核通过后“X 天后结算”，比如配置 settlementTimeDay = 7，表示审核 7 天后进入结算。
        } else {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
    }

    public static Integer getDataNumber(Integer dataType, String dataOne, String dataTow) {
        if (RoleEnum.DataType.MONTH_ONLY.getCode().equals(dataType)) {
            return Integer.parseInt(dataOne);
        } else if (RoleEnum.DataType.GOODS_AUDIT.getCode().equals(dataType)) {
            return Integer.parseInt(dataTow);
        } else {
            throw new PlatformException(BaseErrorCode.PARAM, "供应商的结算配置错误");
        }
    }

    /**
     * 获取 N天之后0点的时间戳
     *
     * @param date
     * @param num
     * @return
     */
    public static Date startDayNumForStamp(DateTime date, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取下个月指定日期的开始时间
     **/
    public static Date getNextMonthDayStartTimeStamp(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
