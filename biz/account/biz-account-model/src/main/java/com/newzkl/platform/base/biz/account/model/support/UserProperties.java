package com.newzkl.platform.base.biz.account.model.support;

import cn.hutool.core.collection.CollUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "scm.user")
public class UserProperties {

    private static List<String> passSmsCodeList = CollUtil.newArrayList("000000", "147852");

    public static boolean isPassSmsCode(String code) {
        return CollUtil.contains(passSmsCodeList, code);
    }

    public void setPassSmsCodeList(List<String> passSmsCodeList) {
        UserProperties.passSmsCodeList = passSmsCodeList;
    }

}
