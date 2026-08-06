package com.newzkl.platform.base.common.ddd.model.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;


import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Configuration
@ConfigurationProperties(prefix = "pallet")
public class PalletProperties {

    public static Map<String, Properties> properties;

    public static List<String> detailHtmlTagList;

    public static String testSpuShipName;

    public static Integer hdhChannelType = 2;

    /**
     * 中泽 D 平台商品同步地址
     */
    public static String zzDGoodsUrl;

    /**
     * 中泽 D 平台订单回调转发地址
     */
    public static String zzDOrderUrl;

    /**
     * 售后地址解析正则模板列表
     * <p>每条正则需含 named groups: province / city / district / detail / receiver / phone</p>
     * @ext 运营弹窗"分析"按钮用
     */
    public static List<Pattern> afterSaleAddressPatterns = new ArrayList<>();

    public static Properties getChannelProperties(String channelSource) {
        return Opt.ofNullable(
                MapUtil.get(properties, channelSource, Properties.class)
        ).orElseThrow(() -> new PlatformException(BaseErrorCode.PARAM_JSON));
    }

    public void setProperties(Map<String, Properties> properties) {
        PalletProperties.properties = properties;
    }

    public void setTestSpuShipName(String testSpuShipName) {
        PalletProperties.testSpuShipName = testSpuShipName;
    }

    public void setDetailHtmlTagList(List<String> detailHtmlTagList) {
        PalletProperties.detailHtmlTagList = detailHtmlTagList;
    }

    public void setHdhChannelType(Integer hdhChannelType) {
        PalletProperties.hdhChannelType = hdhChannelType;
    }

    public void setZzDGoodsUrl(String zzDGoodsUrl) {
        PalletProperties.zzDGoodsUrl = zzDGoodsUrl;
    }

    public void setZzDOrderUrl(String zzDOrderUrl) {
        PalletProperties.zzDOrderUrl = zzDOrderUrl;
    }

    public void setAfterSaleAddressPatterns(List<String> regexList) {
        if (CollUtil.isEmpty(regexList)) {
            PalletProperties.afterSaleAddressPatterns = new ArrayList<>();
            return;
        }
        List<Pattern> list = new ArrayList<>();
        for (String s : regexList) {
            list.add(Pattern.compile(s, Pattern.DOTALL));
        }
        PalletProperties.afterSaleAddressPatterns = list;
    }

    @Data
    public static class Properties {
        private String privateKey;
        private String publicKey;

        private String baseUrl;

        private Map<String, String> url = new HashMap<>();
    }
}
