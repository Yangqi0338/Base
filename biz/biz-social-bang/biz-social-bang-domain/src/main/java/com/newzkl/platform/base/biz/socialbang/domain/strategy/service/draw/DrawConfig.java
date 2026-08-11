package com.newzkl.platform.base.biz.socialbang.domain.strategy.service.draw;


import com.newzkl.platform.base.biz.socialbang.domain.strategy.service.method.DrawMethod;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽取策略配置
 * @Author: niu
 * @Date: 2024/1/16 16:12
 */
public class DrawConfig {

    @Resource
    private final List<DrawMethod> drawMethods = new ArrayList<>();

    protected static Map<Integer,DrawMethod> drawMethodMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        drawMethods.forEach(method -> {
            drawMethodMap.put(method.methodMode(),method);
        });
    }
}
