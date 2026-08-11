package com.newzkl.platform.base.common.core.utils.generator.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.newzkl.platform.base.common.core.utils.generator.random.GeneratorProperties.*;

/**
 * 用户名组装生成
 *
 * @author sijiwang
 */
public class UserNameGenerator extends PropertiesRandomGenerator {

    /**
     * 随机组合不同列表的字
     */
    @SafeVarargs
    private static String combineLists(List<String>... lists) {
        return Arrays.stream(lists).map(UserNameGenerator::getRandomElement).collect(Collectors.joining());
    }

    /**
     * 生成一个随机的昵称
     *
     * @return 多样化的昵称
     */
    public static String generateRandomNickname() {
        // 随机选择一种生成模式
        int mode = RANDOM.nextInt(5); // 0到4，五种模式

        switch (mode) {
            case 0:
                // 模式1: 自然意象 + 自然意象 (如：风禾, 月见)
                return combineLists(NATURE_WORDS, NATURE_WORDS);
            case 1:
                // 模式2: 品德/祝愿 + 自然/文艺 (如：安歌, 雅溪)
                List<String> list1 = RANDOM.nextBoolean() ? VIRTUE_WORDS : BLESSING_WORDS;
                List<String> list2 = RANDOM.nextBoolean() ? NATURE_WORDS : ELEGANCE_WORDS;
                return combineLists(list1, list2);
            case 2:
                // 模式3: 前缀 + 任意双字词 (如：小星眠, 阿沐风)
                String prefix = getRandomElement(PREFIXES);
                String twoCharName = generateRandomNickname(); // 递归调用，生成一个双字词
                return prefix + twoCharName;
            case 3:
                // 模式4: 任意单字 + 后缀 (如：云君, 诗客)
                List<String> allSingleWords = new ArrayList<>();
                allSingleWords.addAll(NATURE_WORDS);
                allSingleWords.addAll(VIRTUE_WORDS);
                allSingleWords.addAll(BLESSING_WORDS);
                allSingleWords.addAll(ELEGANCE_WORDS);
                return getRandomElement(allSingleWords) + getRandomElement(SUFFIXES);
            case 4:
                // 模式5: 文艺组合 (如：诗与远方 -> 简化为 诗远, 墨染春秋 -> 墨秋)
                return combineLists(ELEGANCE_WORDS, NATURE_WORDS);
            default:
                return "无名";
        }
    }

    @Override
    public List<String> getDataList() {
        return null;
    }

    @Override
    public String nextUUID(Object entity) {
        return generateRandomNickname();
    }
}
