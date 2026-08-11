package com.newzkl.platform.base.biz.socialbang.domain.utils;

import java.time.LocalDate;
import java.time.YearMonth;

public class CustomDateUtils {

    /**
     * 根据自定义周规则（1-7号为第一周，8-14为第二周...）获取上周的开始与结束日期
     */
    public static LocalDate[] getLastWeekRange(LocalDate date) {
        int dayOfMonth = date.getDayOfMonth();

        // 当前属于第几周
        int currentWeek = (dayOfMonth - 1) / 7 + 1;

        // 上一周
        int lastWeek = currentWeek - 1;
        int year = date.getYear();
        int month = date.getMonthValue();

        if (lastWeek < 1) {
            // 如果当前是第一周，上周就是上个月最后一周
            LocalDate firstDayOfThisMonth = LocalDate.of(year, month, 1);
            LocalDate lastDayOfLastMonth = firstDayOfThisMonth.minusDays(1);
            YearMonth lastMonth = YearMonth.from(lastDayOfLastMonth);

            int lastMonthDays = lastMonth.lengthOfMonth();
            int lastMonthWeeks = (int) Math.ceil(lastMonthDays / 7.0);

            int startDay = (lastMonthWeeks - 1) * 7 + 1;
            int endDay = lastMonthDays;

            LocalDate startDate = LocalDate.of(lastMonth.getYear(), lastMonth.getMonth(), startDay);
            LocalDate endDate = LocalDate.of(lastMonth.getYear(), lastMonth.getMonth(), endDay);

            return new LocalDate[]{startDate, endDate};
        } else {
            // 上周在当前月内
            int startDay = (lastWeek - 1) * 7 + 1;
            int endDay = Math.min(lastWeek * 7, date.lengthOfMonth());
            LocalDate startDate = LocalDate.of(year, month, startDay);
            LocalDate endDate = LocalDate.of(year, month, endDay);

            return new LocalDate[]{startDate, endDate};
        }
    }

    public static int getWeekValue(LocalDate date) {
        int dayOfMonth = date.getDayOfMonth();
        return (dayOfMonth - 1) / 7 + 1;
    }

    public static void main(String[] args) {
        LocalDate[] lastWeekRange = getLastWeekRange(LocalDate.now());

        System.out.println(lastWeekRange[0]);
        System.out.println(lastWeekRange[1]);

    }
}
