package com.newzkl.platform.base.common.ddd.model.enums.finance;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class BIEnum {

    /* 查询维度 */
    @Getter
    @AllArgsConstructor
    public enum Dimension implements IEnum<Integer> {
        DATE(0, "当日") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfDay(endTime);
            }
        },
        SEVEN_DATE(1, "近7天") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfDay(DateUtil.offset(endTime, DateField.DAY_OF_MONTH, -6));
            }
        },
        WEEKLY(2, "本周") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfWeek(endTime);
            }
        },
        MONTHLY(3, "本月") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfMonth(endTime);
            }
        },
        SEASON(4, "本季度") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfWeek(endTime);
            }
        },
        YEAR(5, "本年") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfYear(endTime);
            }
        },
        YESTERDAY(6, "昨日") {
            @Override
            public DateTime getStartTime(Date endTime) {
                TO_YESTERDAY.getStartTime(endTime);
                return DateUtil.beginOfDay(DateUtil.offset(endTime, DateField.DAY_OF_YEAR, -1));
            }
        },
        TO_YESTERDAY(7, "截止到昨日") {
            @Override
            public DateTime getStartTime(Date endTime) {
                Calendar calendar = DateUtil.calendar(endTime);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                endTime.setTime(calendar.getTimeInMillis());
                return null;
            }
        },
        THIRTY_DATE(8, "近30天") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfDay(DateUtil.offset(endTime, DateField.DAY_OF_MONTH, -30));
            }
        },
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static Dimension findByCode(Integer code) {
            return Arrays.stream(Dimension.values()).filter(it -> it.getCode().equals(code)).findFirst().orElse(null);
        }

        public static DateTime findStartTime(Dimension dimension, Date endTime) {
            if (dimension == null) return null;
            return dimension.getStartTime(endTime);
        }

        public abstract DateTime getStartTime(Date endTime);
    }

    /* 汇总维度 */
    @Getter
    @AllArgsConstructor
    public enum SummaryDimension implements IEnum<Integer> {
        TIME(0, "分时"),
        DATE(1, "今日"),
        YESTERDAY(2, "昨日"),
        WEEKLY(3, "周度"),
        MONTHLY(4, "月度"),
        SEASON(5, "季度"),
        YEAR(6, "年度"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static SummaryDimension findByCode(Integer code) {
            return Arrays.stream(SummaryDimension.values()).filter(it -> it.getCode().equals(code)).findFirst().orElse(null);
        }
    }

}
