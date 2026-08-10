package com.newzkl.platform.base.common.core.model.properties;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统全局配置属性
 *
 * <p>静态字段供无 Spring 上下文处静态读取; 值由 setter 注入。</p>
 *
 * @author fang
 */
@Configuration
@ConfigurationProperties(prefix = "scm.sys")
public class SysProperties {

    /**
     * 网关地址
     */
    public static String gatewayUrl;
    /**
     * 当前应用的数据库名称
     */
    public static String db;
    /**
     * 同类目能有的数量
     */
    public static Integer sameCategoryCount = 90;
    /**
     * 当前应用的程序版本
     */
    public static String version;
    /**
     * 平台名字
     */
    public static String appName;
    /**
     * 不受限制用户id列表
     */
    public static List<Long> freedomUser = new ArrayList<>();
    /**
     * 全局JSON翻译映射，key=原始值字符串，value=翻译值
     * @ext @JsonTranslate type=FIX 用
     */
    public static Map<String, String> translateMap = new HashMap<>();

    public void setGatewayUrl(String gatewayUrl) {
        SysProperties.gatewayUrl = gatewayUrl;
    }
    public void setDb(String db) {
        SysProperties.db = db;
    }
    public void setSameCategoryCount(Integer sameCategoryCount) {
        SysProperties.sameCategoryCount = sameCategoryCount;
    }
    public void setVersion(String version) {
        SysProperties.version = version;
    }
    public void setAppName(String appName) {
        SysProperties.appName = appName;
    }
    public void setFreedomUser(List<Long> freedomUser) {
        SysProperties.freedomUser = freedomUser;
    }

    private String translate;
    public void setTranslate(String translate) {
        this.translate = translate;
        if (StrUtil.isNotBlank(translate)) {
            SysProperties.translateMap = new HashMap<>();
            String[] split = translate.split(",");
            for (String s : split) {
                String[] strings = s.split(":");
                SysProperties.translateMap.put(ArrayUtil.get(strings,0), ArrayUtil.get(strings,1));
            }
        }
    }

    /**
     * 用 translateMap 对文本做子串替换
     * <p>用于面向用户展示的文本做"个别字替换"。{@code JsonTranslateSerializer}
     * 是整值替换，本方法是子串替换，互不冲突。</p>
     * @ext 逐 entry.key → entry.value 全替换
     * @ext 通知内容/标题等
     * @param text 原文
     * @return 替换后文本；text=null 时原样返回
     */
    public static String translateText(String text) {
        if (text == null || MapUtil.isEmpty(translateMap)) {
            return text;
        }
        String out = text;
        for (Map.Entry<String, String> e : translateMap.entrySet()) {
            out = out.replace(e.getKey(), e.getValue());
        }
        return out;
    }
}
