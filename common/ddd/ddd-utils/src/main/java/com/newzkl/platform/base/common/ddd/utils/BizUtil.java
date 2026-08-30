package com.newzkl.platform.base.common.ddd.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.res.PlatformTreeNode;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.IgnoreStrJoiner;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 通用业务工具类
 *
 * <p>迁移说明: 原依赖 {@code RoleEnum.CompanyRole} 的角色相关方法
 * (getOptionMapJson / findClientRoleList / getLevelEnumList / clientContainRole 等)
 * 属业务语义，已随业务枚举下沉到业务层，通用层不再保留。</p>
 *
 * @author muc_fang
 */
@Component
public class BizUtil {

    public static final BigDecimal BIG_100 = new BigDecimal(100);
    private static final JavaProjectBuilder builder = new JavaProjectBuilder();
    private static final Map<Class<?>, JavaClass> JAVA_CLASS_CACHE = new HashMap<>();

    public static String addIdString(String source, Long addItem) {
        if (StrUtil.isEmpty(source)) {
            return String.valueOf(addItem);
        } else {
            List<Long> longList = CommonUtil.strToLongList(source);
            if (longList.contains(addItem)) {
                return source;
            } else {
                longList.add(addItem);
                return CollUtil.join(longList, ",");
            }
        }
    }

    public static String addNameString(String sourceJson, String addItem) {
        if (StrUtil.isEmpty(sourceJson)) {
            return JSONObject.toJSONString(Collections.singletonList(addItem));
        } else {
            List<String> longList = JSON.parseArray(sourceJson, String.class);
            if (longList.contains(addItem)) {
                return sourceJson;
            } else {
                longList.add(addItem);
                return JSONObject.toJSONString(longList);
            }
        }
    }

    public static String cutIdString(String source, Long cutItem) {
        if (StrUtil.isEmpty(source)) {
            return "";
        } else {
            List<Long> longList = CommonUtil.strToLongList(source);
            if (longList.contains(cutItem)) {
                longList.remove(cutItem);
                return CollUtil.join(longList, ",");
            } else {
                return source;
            }
        }
    }

    public static Long nextCode(Long pid, List<Long> idList) {
        if (pid == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "pid不能为空");
        }
        HashSet<Long> subIdSet = new HashSet<>(idList);
        for (int i = idList.size(); i <= 90; i++) {
            long sourceId = pid == 0 ? 10 : pid * 100;
            Long nextId = sourceId + i + 1;
            if (!subIdSet.contains(nextId)) {
                return nextId;
            }
        }
        for (int i = 0; i < idList.size(); i++) {
            long sourceId = pid == 0 ? 10 : pid * 100;
            Long nextId = sourceId + i + 1;
            if (!subIdSet.contains(nextId)) {
                return nextId;
            }
        }
        throw new PlatformException(BaseErrorCode.PARAM, "数据异常");
    }

    public static Long nextCode(Long pid, Long code) {
        if (pid == 0) {
            if (code == null) {
                return 10L;
            } else {
                if (code.longValue() == 99L) {
                    return 10L;
                } else {
                    return code + 1;
                }
            }
        } else {
            if (code == null) {
                return Long.parseLong(pid + "10");
            } else {
                String codeStr = code.toString();
                String head = codeStr.substring(0, codeStr.length() - 2);
                String foot = codeStr.substring(codeStr.length() - 2);
                if (foot.equals("99")) {
                    return Long.parseLong(head + "10");
                } else {
                    return code + 1;
                }
            }
        }
    }

    /**
     * 将 list 转换为树
     *
     * @param list 节点列表
     * @param <T>  节点类型
     * @return 树的根节点列表
     */
    public static <T extends PlatformTreeNode> List<T> listToTree(List<T> list) {
        List<T> rootList = new ArrayList<>();
        Map<Long, T> nodeMap = new HashMap<>(20);
        for (T node : list) {
            nodeMap.put(node.getNodeId(), node);
        }
        for (T node : list) {
            if (node.getNodePid() == 0) {
                rootList.add(node);
            } else {
                T pNode = nodeMap.get(node.getNodePid());
                if (pNode != null) {
                    pNode.putChildren(node);
                }
            }
        }
        return rootList;
    }

    public static <T extends PlatformTreeNode> List<T> treeToList(List<T> itemList) {
        List<T> rootList = new ArrayList<>();
        addChildren(rootList, itemList);
        return rootList;
    }

    public static <T extends PlatformTreeNode> List<T> treeToList(T item) {
        List<T> rootList = new ArrayList<>();
        rootList.add(item);
        addChildren(rootList, item.getChildren());
        return rootList;
    }

    private static <T extends PlatformTreeNode> void addChildren(List<T> rootList, List<T> children) {
        if (ObjectUtil.isNotEmpty(children)) {
            for (T sub : children) {
                rootList.add(sub);
                addChildren(rootList, sub.getChildren());
            }
        }
    }

    public static List<String> split(String spuParamAttribute, String... split) {
        if (StrUtil.isEmpty(spuParamAttribute)) {
            return new ArrayList<>();
        }
        List<String> source = new ArrayList<>();
        source.add(spuParamAttribute);
        for (String s : split) {
            List<String> currentSource = new ArrayList<>();
            for (String sourceItem : source) {
                String[] sourceSplit = sourceItem.split(s);
                List<String> sourceSplitList = Arrays.asList(sourceSplit);
                currentSource.addAll(sourceSplitList);
            }
            source = currentSource;
        }
        return trim(source);
    }

    public static List<String> trim(List<String> source) {
        List<String> trimList = new ArrayList<>();
        for (String s : source) {
            trimList.add(s.trim());
        }
        return trimList;
    }

    public static String stringArrayToString(boolean isIgnoreEmpty, String... stringArray) {
        StringBuffer sb = new StringBuffer();
        int ii = 0;
        for (int i = 0; i < stringArray.length; i++) {
            if (isIgnoreEmpty && StrUtil.isEmpty(stringArray[i])) {
                continue;
            }
            if (ii == 0) {
                sb.append(stringArray[i]);
            } else {
                sb.append("," + stringArray[i]);
            }
            ii++;
        }
        return sb.toString();
    }

    public static String stringListToString(List<String> list) {
        if (ObjectUtil.isEmpty(list)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            builder.append(",");
        }
        String substring = builder.substring(0, builder.length() - 1);
        return substring;
    }

    public static String stringListToString(boolean isIgnoreEmpty, List<String> stringArray) {
        StringBuffer sb = new StringBuffer();
        int ii = 0;
        for (int i = 0; i < stringArray.size(); i++) {
            if (isIgnoreEmpty && StrUtil.isEmpty(stringArray.get(i))) {
                continue;
            }
            if (ii == 0) {
                sb.append(stringArray.get(i));
            } else {
                sb.append("," + stringArray.get(i));
            }
            ii++;
        }
        return sb.toString();
    }

    public static String generate6code() {
        String tmpResult;
        StringBuilder soleResult = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            int mark = Math.random() >= 0.5 ? 1 : 0;
            if (0 == mark) {
                Random random = new Random();
                tmpResult = random.nextInt(10) + "";
            } else {
                char[] englishNumArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                        'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
                Random random = new Random();
                int sub = random.nextInt(englishNumArray.length);
                tmpResult = englishNumArray[sub] + "";
            }
            soleResult.append(tmpResult);
        }
        return soleResult.toString();
    }

    /**
     * 检查是否在状态中
     *
     * @param asList    合法状态集合
     * @param state     当前状态
     * @param errorCode 错误码
     */
    public static void checkInState(List<Integer> asList, Integer state, ErrorCode errorCode) {
        if (!asList.contains(state)) {
            throw new PlatformException(errorCode);
        }
    }

    public static String generateDiffCode(int length) {
        if (length > 32) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        String md5Hex = MD5.create().digestHex16(UUID.randomUUID().toString());
        return md5Hex.substring(0, length);
    }

    /**
     * 生成指定长度的随机码 (数字 + 小写字母混合)
     *
     * <p>每一位以约 50% 概率取 0-9 数字, 否则取 a-z 小写字母。
     * 与 {@link BizUtil#generateDiffCode} 不同, 本方法不依赖 MD5 摘要长度,
     * 因此支持大于 32 的长度 (常用于 openapi secret 等 32 位凭证)。</p>
     *
     * <p>迁移说明: 原 {@code com.zkl.scm.model.utils.ScmUtil#generateCode(int)},
     * 行为逐字保留 (含"数字/字母各半"的随机策略)。</p>
     *
     * @param length 随机码长度
     * @return 随机码
     * @author KC
     */
    public static String generateCode(int length) {
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (Math.random() >= 0.5) {
                result.append(letters[random.nextInt(letters.length)]);
            } else {
                result.append(random.nextInt(10));
            }
        }
        return result.toString();
    }

    public static String cutLast(String string) {
        if (StrUtil.isEmpty(string)) {
            return "";
        }
        return string.substring(0, string.length() - 1);
    }

    public static String composeUrl(String domain, String api) {
        String url;
        if (BizUtil.stringLast(domain).equals("/")) {
            if (BizUtil.stringFirst(domain).equals("/")) {
                url = BizUtil.cutLast(domain) + api;
            } else {
                url = domain + api;
            }
        } else {
            url = domain + "/" + api;
        }
        return url;
    }

    public static String stringFirst(String string) {
        if (StrUtil.isEmpty(string)) {
            return "";
        }
        return string.substring(0, 1);
    }

    public static String stringLast(String string) {
        if (StrUtil.isEmpty(string)) {
            return "";
        }
        return string.substring(string.length() - 1);
    }

    public static Set<String> generateDiffCode(int length, Integer number) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < number; i++) {
            set.add(BizUtil.generateDiffCode(length));
        }
        return set;
    }

    /**
     * 根据企业角色码计算下级类型
     *
     * <p>用于权限功能点归属判定: 平台管理员(角色码 0)对应下级类型 1,
     * 市场管理员(角色码 1)对应下级类型 2, 其余角色对应 0。</p>
     *
     * <p>迁移说明: 原实现引用业务枚举 {@code RoleEnum.CompanyRole.PLATFORM/MARKET}，
     * 因通用层不得依赖业务枚举, 此处内联其角色码字面量 (PLATFORM=0, MARKET=1)。</p>
     *
     * @param code 企业角色码
     * @return 下级类型: 平台=1, 市场=2, 其他=0
     */
    public static Integer getBelowType(Long code) {
        if (Long.valueOf(0L).equals(code)) {
            return 1;
        } else if (Long.valueOf(1L).equals(code)) {
            return 2;
        } else {
            return 0;
        }
    }

    public static List<Long> strToLongList(String[] fieldRole) {
        List<Long> longList = new ArrayList<>();
        if (fieldRole != null) {
            for (String s : fieldRole) {
                longList.add(Long.valueOf(s));
            }
        }
        return longList;
    }

    public static String excelMoney(Integer orderMoney) {
        double result = (double) orderMoney / 100;
        return String.format("%.2f", result);
    }

    /**
     * Money 金额转 Excel 展示字符串, 保留两位小数(向下取整)
     *
     * @param orderMoney 金额, 为 null 时返回 "0.00"
     * @return 元单位的两位小数字符串
     */
    public static String excelMoney(Money orderMoney) {
        if (orderMoney == null) {
            return "0.00";
        }
        return orderMoney.getAmount().setScale(2, RoundingMode.DOWN).toPlainString();
    }

    public static String goodsSkuName(String skuAttribute) {
        String skuName = "";
        if (StrUtil.isNotEmpty(skuAttribute)) {
            JSONArray jsonArray = JSON.parseArray(skuAttribute);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                skuName = jsonObject.getString("value") + ";";
            }
            return skuName.substring(0, skuName.length() - 1);
        } else {
            return skuName;
        }
    }

    /**
     * 大于0
     *
     * @param number 数值
     * @return 是否大于0
     */
    public static boolean biggerThanZero(Number number) {
        return NumberUtil.isGreater(NumberUtil.toBigDecimal(number), BigDecimal.ZERO);
    }

    /**
     * 查找某个类在项目的目录位置
     *
     * @param clazz 目标类
     * @return 目录路径
     */
    public static String findClassDirPath(Class<?> clazz) {
        List<String> dirList = StrUtil.split("src.main.java", ".");

        String packageName = clazz.getPackage().getName();
        String prefix = "com.zkl.scm.";

        if (packageName.startsWith(prefix)) {
            String remaining = packageName.substring(prefix.length());
            String[] parts = remaining.split("\\.");

            Object subModuleName = ArrayUtil.get(parts, 1);
            Object moduleName = ArrayUtil.get(parts, 0);
            if (ObjectUtil.isEmpty(moduleName)) {
                System.err.println("无法从类" + clazz.getName() + "中提取模块名，跳过该类");
                return "";
            }
            moduleName = "scm-" + moduleName;

            if (ObjectUtil.isNotEmpty(subModuleName)) {
                dirList.add(0, moduleName + "-" + subModuleName.toString());
            }
            if (ObjectUtil.isNotEmpty(moduleName)) {
                dirList.add(0, moduleName.toString());
            }
        }

        if (dirList.size() <= 3) {
            System.err.println("无法从类" + clazz.getName() + "中提取模块名，跳过该类");
            return "";
        }

        return IgnoreStrJoiner.toStr(File.separator, dirList.toArray(new String[0]));
    }

    public static JavaClass findJavaClass(Class<?> clazz) throws Exception {
        if (JAVA_CLASS_CACHE.containsKey(clazz)) {
            return JAVA_CLASS_CACHE.get(clazz);
        }

        loadJavaSource(clazz);

        JavaClass javaClass = builder.getClassByName(clazz.getName());
        if (javaClass == null) {
            throw new IllegalArgumentException("无法找到类: " + clazz.getName());
        }
        JAVA_CLASS_CACHE.put(clazz, javaClass);
        return javaClass;
    }

    private static boolean loadJavaSource(Class<?> clazz) throws IOException {
        String moduleSourceDir = BizUtil.findClassDirPath(clazz);
        String className = clazz.getName().replace('.', '/') + ".java";
        File moduleFile = new File(moduleSourceDir, className);

        if (moduleFile.exists() && moduleFile.isFile()) {
            builder.addSource(moduleFile);
            return true;
        }

        URL url = clazz.getClassLoader().getResource(className);
        if (url != null) {
            try {
                builder.addSource(new File(url.toURI()));
                return true;
            } catch (Exception e) {
                System.err.println("从类路径加载源文件失败: " + e.getMessage());
            }
        }

        System.err.println("无法找到源文件: " + clazz.getName());
        return false;
    }

    public static Integer percentFloor(Number... value) {
        return NumberUtil.mul(value).divide(BIG_100, 0, RoundingMode.FLOOR).intValue();
    }

    public static BigDecimal percent(Integer value, int scale) {
        return NumberUtil.div(value, BIG_100, scale, RoundingMode.HALF_UP);
    }

    /**
     * 根据分转换为元，默认使用2位小数，不足的用0补齐，向下取整
     *
     * @param value 分
     * @return 元字符串
     */
    public static String toYuanStr(Integer value) {
        return toYuan(value, RoundingMode.FLOOR).toString();
    }

    public static BigDecimal toYuan(Integer value, RoundingMode roundingMode) {
        return NumberUtil.div(value, BIG_100, 2, roundingMode);
    }

    public static BigDecimal percent(Integer value, int scale, RoundingMode roundingMode) {
        return NumberUtil.div(value, BIG_100, scale, roundingMode);
    }

    /**
     * 拼接父账号层级关系
     */
    public static String getPidList(String pidList, Long accountId) {
        return concatParentAccountStr(pidList, accountId + "");
    }

    /**
     * 拼接角色列表
     */
    public static String getIdentityList(String identityList, AccountEnum.Identity identity) {
        return concatParentAccountStr(identityList, identity + "");
    }

    /**
     * 拼接父账号层级关系
     */
    public static String concatParentAccountStr(String pStr, String str) {
        return StrUtil.concat(true, pStr, str, ",");
    }

    /**
     * 根据充值金额阶梯配置，查找匹配的阶梯值
     */
    public static Double findStepValue(TreeMap<Integer, Double> config, Double defaultValue, Integer rechargeAmount) {
        Double nowValue = 0.0;
        if (config != null) {
            Opt<Double> defaultValueOpt = Opt.ofNullable(config.firstEntry()).map(Map.Entry::getValue);
            if (defaultValue == null) {
                nowValue = defaultValueOpt.orElseThrow(() -> new PlatformException(BaseErrorCode.CUSTOM, "错误的平台充值配置"));
            } else {
                nowValue = defaultValueOpt.orElse(defaultValue);
            }
            for (Map.Entry<Integer, Double> entry : config.entrySet()) {
                if (rechargeAmount >= entry.getKey()) {
                    nowValue = entry.getValue();
                }
            }
        }
        return nowValue;
    }

    public static <T> void async(T obj, Consumer<T> consumer) {
        Class<T> clazz = (Class<T>) obj.getClass();
        T bean = SpringUtil.getBean(clazz);
        consumer.accept(bean);
    }

    public static <T, R> CompletableFuture<R> asyncFuture(T obj, Function<T, CompletableFuture<R>> function) {
        Class<T> clazz = (Class<T>) obj.getClass();
        T bean = SpringUtil.getBean(clazz);
        return function.apply(bean);
    }
}
