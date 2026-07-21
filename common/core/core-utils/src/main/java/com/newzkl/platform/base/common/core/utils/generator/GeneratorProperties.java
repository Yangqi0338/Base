package com.newzkl.platform.base.common.core.utils.generator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 昵称 / 头像随机生成器的字库配置。
 *
 * <p>原为 Spring Boot {@code @ConfigurationProperties}，迁移后降级为纯静态字库；
 * 如需外部覆盖可通过 setter 注入。</p>
 *
 * @author sijiwang
 */
public class GeneratorProperties {

    // 1. 自然意象
    public static List<String> NATURE_WORDS = Arrays.asList(
            "风", "月", "星", "云", "山", "川", "禾", "林", "溪", "雪",
            "露", "晨", "暮", "光", "影", "雾", "霞", "虹", "海", "潮"
    );

    // 2. 品德与修养
    public static List<String> VIRTUE_WORDS = Arrays.asList(
            "仁", "义", "礼", "智", "信", "忠", "孝", "廉", "洁", "雅",
            "贤", "德", "善", "慈", "恭", "谦", "和", "睦", "诚", "真"
    );

    // 3. 美好祝愿
    public static List<String> BLESSING_WORDS = Arrays.asList(
            "福", "禄", "寿", "喜", "安", "康", "宁", "乐", "顺", "吉",
            "祥", "瑞", "兴", "旺", "盛", "昌", "隆", "泰", "平", "定"
    );

    // 4. 文艺与雅致
    public static List<String> ELEGANCE_WORDS = Arrays.asList(
            "诗", "书", "画", "琴", "棋", "墨", "笔", "纸", "砚", "辞",
            "章", "文", "采", "韵", "律", "赋", "歌", "谣", "篇", "卷"
    );

    // 前缀词
    public static List<String> PREFIXES = Arrays.asList("小", "阿", "初", "知", "若", "如", "似");

    // 后缀词
    public static List<String> SUFFIXES = Arrays.asList("儿", "子", "君", "卿", "郎", "娘", "仙", "客");

    public static List<String> AVATAR_URLS = Collections.unmodifiableList(Arrays.asList(
            "https://zztp.zzxyg88.com/pulseclear/avatar-8.png",
            "https://zztp.zzxyg88.com/pulseclear/avatar-7.png",
            "https://zztp.zzxyg88.com/pulseclear/avatar-6.png",
            "https://zztp.zzxyg88.com/pulseclear/avatar-5.png",
            "https://zztp.zzxyg88.com/pulseclear/avatar-4.png",
            "https://zztp.zzxyg88.com/pulseclear/avatar-3.png",
            "https://zztp.zzxyg88.com/pulseclear/avatar-2.png",
            "https://zztp.zzxyg88.com/pulseclear/avatar-1.png"
    ));

    public void setNatureWords(List<String> natureWords) {
        GeneratorProperties.NATURE_WORDS = natureWords;
    }

    public void setVirtueWords(List<String> virtueWords) {
        GeneratorProperties.VIRTUE_WORDS = virtueWords;
    }

    public void setBlessingWords(List<String> blessingWords) {
        GeneratorProperties.BLESSING_WORDS = blessingWords;
    }

    public void setEleganceWords(List<String> eleganceWords) {
        GeneratorProperties.ELEGANCE_WORDS = eleganceWords;
    }

    public void setPREFIXES(List<String> PREFIXES) {
        GeneratorProperties.PREFIXES = PREFIXES;
    }

    public void setSUFFIXES(List<String> SUFFIXES) {
        GeneratorProperties.SUFFIXES = SUFFIXES;
    }

    public void setAvatarUrls(List<String> avatarUrls) {
        GeneratorProperties.AVATAR_URLS = avatarUrls;
    }
}
