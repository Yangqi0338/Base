package com.newzkl.platform.base.common.core.utils.generator;

import java.util.List;

import static com.newzkl.platform.base.common.core.utils.generator.GeneratorProperties.AVATAR_URLS;

/**
 * 头像随机生成
 *
 * @author sijiwang
 */
public class AvatarGenerator extends PropertiesRandomGenerator {

    @Override
    public List<String> getDataList() {
        return AVATAR_URLS;
    }
}
