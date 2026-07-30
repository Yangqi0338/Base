package com.newzkl.platform.base.common.core.utils.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则常量与文本处理工具
 *
 * @author fang
 */
public class PatternUtil {
    public static final String GENERAL = "^\\w+$";
    public static final String NUMBERS = "\\d+";
    public static final String WORD = "[a-zA-Z]+";
    public static final String CHINESE = "[⺀-\u2eff⼀-\u2fdf㇀-\u31ef㐀-\u4dbf一-\u9fff豈-\ufaff\ud840\udc00-\ud869\udedf\ud869\udf00-\ud86d\udf3f\ud86d\udf40-\ud86e\udc1f\ud86e\udc20-\ud873\udeaf\ud87e\udc00-\ud87e\ude1f]";
    public static final String CHINESES = "[⺀-\u2eff⼀-\u2fdf㇀-\u31ef㐀-\u4dbf一-\u9fff豈-\ufaff\ud840\udc00-\ud869\udedf\ud869\udf00-\ud86d\udf3f\ud86d\udf40-\ud86e\udc1f\ud86e\udc20-\ud873\udeaf\ud87e\udc00-\ud87e\ude1f]+";
    public static final String GROUP_VAR = "\\$(\\d+)";
    public static final String IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$";
    public static final String IPV6 = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";
    public static final String MONEY = "^(\\d+(?:\\.\\d+)?)$";
    public static final String EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
    public static final String MOBILE = "(?:0|86|\\+86)?1[3-9]\\d{9}";
    public static final String MOBILE_HK = "(?:0|852|\\+852)?\\d{8}";
    public static final String MOBILE_TW = "(?:0|886|\\+886)?(?:|-)09\\d{8}";
    public static final String MOBILE_MO = "(?:0|853|\\+853)?(?:|-)6\\d{7}";
    public static final String TEL = "(010|02\\d|0[3-9]\\d{2})-?(\\d{6,8})";
    public static final String TEL_400_800 = "0\\d{2,3}[\\- ]?[1-9]\\d{6,7}|[48]00[\\- ]?[1-9]\\d{2}[\\- ]?\\d{4}";
    public static final String CITIZEN_ID = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)";
    public static final String ZIP_CODE = "^(0[1-7]|1[0-356]|2[0-7]|3[0-6]|4[0-7]|5[0-7]|6[0-7]|7[0-5]|8[0-9]|9[0-8])\\d{4}|99907[78]$";
    public static final String BIRTHDAY = "^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$";
    public static final String URL = "[a-zA-Z]+://[\\w-+&@#/%?=~_|!:,.;]*[\\w-+&@#/%=~_|]";
    public static final String URL_HTTP = "(https?|ftp|file)://[\\w-+&@#/%?=~_|!:,.;]*[\\w-+&@#/%=~_|]";
    public static final String GENERAL_WITH_CHINESE = "^[一-\u9fff\\w]+$";
    public static final String UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    public static final String UUID_SIMPLE = "^[0-9a-fA-F]{32}$";
    public static final String MAC_ADDRESS = "((?:[a-fA-F0-9]{1,2}[:-]){5}[a-fA-F0-9]{1,2})|0x(\\d{12}).+ETHER";
    public static final String HEX = "^[a-fA-F0-9]+$";
    public static final String TIME = "\\d{1,2}:\\d{1,2}(:\\d{1,2})?";
    public static final String PLATE_NUMBER = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";
    public static final String CREDIT_CODE = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$";
    public static final String CAR_VIN = "^[A-HJ-NPR-Z0-9]{8}[0-9X][A-HJ-NPR-Z0-9]{2}\\d{6}$";
    public static final String CAR_DRIVING_LICENCE = "^[0-9]{12}$";
    public static final String CHINESE_NAME = "^[⺀-\u9fff·]{2,60}$";
    private static final Pattern DOUBLE_QUOTE_SRC_PATTERN = Pattern.compile("src=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
    /**
     * 中国大陆手机号正则表达式（支持最新号段）
     */
    private static final Pattern CHINA_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    public static Set<String> getHtmlImgList(String html) {
        Set<String> srcSet = new LinkedHashSet<>();
        try {
            Document doc = Jsoup.parse(html);
            Elements imgElements = doc.select("p img");
            for (Element img : imgElements) {
                String imgTagStr = img.outerHtml();
                Matcher matcher = DOUBLE_QUOTE_SRC_PATTERN.matcher(imgTagStr);
                if (matcher.find()) {
                    String src = matcher.group(1);
                    if (StrUtil.isBlank(src)) {
                        continue;
                    }
                    String cleanSrc = HtmlUtil.cleanHtmlTag(src.trim());
                    if (StrUtil.isNotBlank(cleanSrc)) {
                        srcSet.add(cleanSrc);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("解析HTML失败：" + e.getMessage());
        }
        return srcSet;
    }

    public static String desensitized(String str, int start, int end) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        int length = str.length();
        int startIndex = Math.min(start, length - 1);
        int endIndex = Math.max(startIndex + 1, length - end);
        return StrUtil.hide(str, startIndex, endIndex);
    }

    /**
     * 判断是否为中国大陆手机号（严格校验）
     *
     * @param phone 待校验的手机号字符串
     * @return true-是有效手机号，false-无效
     */
    public static boolean isChinaPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        if (phone.length() != 11) {
            return false;
        }
        return CHINA_PHONE_PATTERN.matcher(phone).matches();
    }
}
