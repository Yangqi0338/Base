package com.newzkl.platform.base.biz.socialbang.infrastructure.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 活动id生成器
 *
 * @author niu
 */
public class CustomerIdGenerator {

    /**
     * 格式化时间：yyyyMMddHHmmss
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 生成活动id：前缀 + 时间戳 + 6位随机数
     *
     * @param prefix 前缀
     * @return 活动id
     */
    public static String generateActivityId(String prefix) {

        String dateStr = DATE_FORMAT.format(new Date());

        // 生成随机的 6 位数字，不足左补 0
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000);
        String seqStr = String.format("%06d", randomNum);

        return prefix + dateStr + seqStr;
    }
}
