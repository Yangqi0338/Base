package com.newzkl.platform.base.biz.account.model.enums;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * BI 统计枚举
 *
 * <p>迁自旧 {@code com.zkl.scm.model.constants.user.BIEnum}。本域只用到查询维度
 * {@code Dimension}, 故仅迁该枚举, 其余 (运营商收益模块 / 汇总维度 / 汇总存储类型)
 * 未被 biz-account 引用, 不迁。各 code 与 value 逐字沿用旧定义, 不改前端契约</p>
 *
 * @author KC
 */
public class BIEnum {

    /**
     * 查询维度
     *
     * <p>逐字沿用旧实现, 含旧代码里的既有偏差: {@code SEASON} 的开始时间实际取的是本周起点
     * (旧 {@code DateUtil.beginOfWeek}), {@code TO_YESTERDAY} 会就地改写传入的 endTime
     * 并回 null。为不改变前端拿到的数字, 这两处按原样保留</p>
     *
     * @author KC
     */
    @Getter
    @AllArgsConstructor
    public enum Dimension {
        /** 当日 */
        DATE(0, "当日") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfDay(endTime);
            }
        },
        /** 近7天 */
        SEVEN_DATE(1, "近7天") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfDay(DateUtil.offset(endTime, DateField.DAY_OF_MONTH, -6));
            }
        },
        /** 本周 */
        WEEKLY(2, "本周") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfWeek(endTime);
            }
        },
        /** 本月 */
        MONTHLY(3, "本月") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfMonth(endTime);
            }
        },
        /** 本季度 */
        SEASON(4, "本季度") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfWeek(endTime);
            }
        },
        /** 本年 */
        YEAR(5, "本年") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfYear(endTime);
            }
        },
        /** 昨日 */
        YESTERDAY(6, "昨日") {
            @Override
            public DateTime getStartTime(Date endTime) {
                TO_YESTERDAY.getStartTime(endTime);
                return DateUtil.beginOfDay(DateUtil.offset(endTime, DateField.DAY_OF_YEAR, -1));
            }
        },
        /** 截止到昨日 */
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
        /** 近30天 */
        THIRTY_DATE(8, "近30天") {
            @Override
            public DateTime getStartTime(Date endTime) {
                return DateUtil.beginOfDay(DateUtil.offset(endTime, DateField.DAY_OF_MONTH, -30));
            }
        },
        ;

        private final Integer code;
        private final String value;

        /**
         * 按编码取维度
         *
         * @param code 维度编码
         * @return 匹配的维度, 未匹配回 null
         */
        public static Dimension findByCode(Integer code) {
            return Arrays.stream(Dimension.values()).filter(it -> it.getCode().equals(code)).findFirst().orElse(null);
        }

        /**
         * 按维度算开始时间
         *
         * @param dimension 维度
         * @param endTime   结束时间
         * @return 开始时间, 维度为空回 null
         */
        public static DateTime findStartTime(Dimension dimension, Date endTime) {
            if (dimension == null) {
                return null;
            }
            return dimension.getStartTime(endTime);
        }

        /**
         * 按维度算开始时间
         *
         * @param endTime 结束时间
         * @return 开始时间
         */
        public abstract DateTime getStartTime(Date endTime);
    }
}
