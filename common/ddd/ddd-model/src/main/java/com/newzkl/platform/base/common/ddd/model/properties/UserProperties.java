package com.newzkl.platform.base.common.ddd.model.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder.isProd;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "platform.user")
public class UserProperties {

    public static List<String> passSmsCodeList = CollUtil.newArrayList("000000", "147852");

    public static boolean isPassSmsCode(String code) {
        return CollUtil.contains(passSmsCodeList, code);
    }

    public void setPassSmsCodeList(List<String> passSmsCodeList) {
        UserProperties.passSmsCodeList = passSmsCodeList;
    }

    @PostConstruct
    public void init() {
        if (isProd()) {
            String code = RandomUtil.randomNumbers(6);
            UserProperties.passSmsCodeList = CollUtil.newArrayList(code);
            log.error("[SMS-PASS-CODE] prod通行码: {}", code);
        }
    }
}
