package com.newzkl.platform.base.biz.order.model.order.util;

import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分组统计补齐工具
 *
 * <p>迁移自旧 {@code com.zkl.scm.model.utils.ScmUtil#groupCountRes2Complete} 与
 * {@code com.zkl.scm.model.utils.DateSplitUtils}。SQL 只会返回「有数据」的时间桶,
 * 本工具按 {@code TimeQuery} 的分组粒度把区间切成连续桶并补 0, 保证前端折线图不断点。</p>
 *
 * <p>偏离说明: 旧实现依赖 hutool {@code DateUtil} + {@code Calendar}, 此处改用
 * {@code java.time} 以免 model 模块引入 hutool。桶键格式沿用旧 Java 侧格式
 * ({@code yyyy-MM-dd-HH} / {@code yyyy-MM-dd}) 保持前端契约不变。</p>
 *
 * @author KC
 */
public final class GroupCountUtils {

    /**
     * 分组类型: 按小时
     */
    public static final int GROUP_TYPE_HOUR = 0;

    /**
     * 小时桶键格式
     */
    public static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    /**
     * 天桶键格式
     */
    public static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private GroupCountUtils() {
    }

    /**
     * 按查询区间补齐分组统计, 缺失桶补 0
     *
     * @param groupCountRes SQL 分组结果 (可为 null, 视为无数据)
     * @param timeQuery     时间分组查询 (起止时间为毫秒时间戳)
     * @return 连续且升序的分组统计列表
     */
    public static List<GroupCountRes> complete(List<GroupCountRes> groupCountRes, TimeQuery timeQuery) {
        List<GroupCountRes> result = new ArrayList<>();
        if (timeQuery == null || timeQuery.getCreateBeginTime() == null || timeQuery.getCreateEndTime() == null) {
            return result;
        }
        Map<String, GroupCountRes> existMap = new HashMap<>();
        if (groupCountRes != null) {
            for (GroupCountRes item : groupCountRes) {
                if (item != null && item.getTransDay() != null) {
                    existMap.put(item.getTransDay(), item);
                }
            }
        }
        boolean byHour = isGroupByHour(timeQuery);
        DateTimeFormatter formatter = byHour ? HOUR_FORMATTER : DAY_FORMATTER;
        for (LocalDateTime bucket : bucketStarts(timeQuery)) {
            String key = bucket.format(formatter);
            GroupCountRes group = new GroupCountRes();
            group.setTransDay(key);
            GroupCountRes exist = existMap.get(key);
            if (exist != null) {
                group.setTransNum(exist.getTransNum());
                group.setTransAmount(exist.getTransAmount());
            }
            result.add(group);
        }
        return result;
    }

    /**
     * 判断是否按小时分组
     *
     * @param timeQuery 时间分组查询
     * @return true 按小时, false 按天
     */
    public static boolean isGroupByHour(TimeQuery timeQuery) {
        return timeQuery.getGroupType() != null && GROUP_TYPE_HOUR == timeQuery.getGroupType();
    }

    /**
     * 切出区间内所有桶的起始时间
     *
     * <p>沿用旧 {@code DateSplitUtils} 语义: 从起始时间开始按步长递增, 直到达到或超过结束时间;
     * 结束时间小于等于起始时间时返回空列表。</p>
     *
     * @param timeQuery 时间分组查询
     * @return 桶起始时间列表 (升序)
     */
    private static List<LocalDateTime> bucketStarts(TimeQuery timeQuery) {
        List<LocalDateTime> buckets = new ArrayList<>();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime start = Instant.ofEpochMilli(timeQuery.getCreateBeginTime()).atZone(zone).toLocalDateTime();
        LocalDateTime end = Instant.ofEpochMilli(timeQuery.getCreateEndTime()).atZone(zone).toLocalDateTime();
        if (!end.isAfter(start)) {
            return buckets;
        }
        int step = timeQuery.getGroupCount() == null || timeQuery.getGroupCount() <= 0 ? 1 : timeQuery.getGroupCount();
        boolean byHour = isGroupByHour(timeQuery);
        LocalDateTime cursor = start;
        while (cursor.isBefore(end)) {
            buckets.add(cursor);
            cursor = byHour ? cursor.plusHours(step) : cursor.plusDays(step);
        }
        return buckets;
    }
}
