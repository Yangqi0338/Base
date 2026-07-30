package com.newzkl.platform.base.common.core.utils.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.*;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 通用集合 / 流 / 字符串 / 数值处理工具
 *
 * @author 孔祥基
 */
public class CommonUtil {

    public static final String[] image_accept = {"jpg", "png", "jpeg"};

    public static StringBuilder newStr(JSONObject fieldJson, Object newObj) {
        StringBuilder istr = new StringBuilder();
        istr.append("{");
        ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());
        for (String s : arrayList) {
            Object val = BeanUtil.getProperty(newObj, fieldJson.getString(s));
            if (ObjectUtil.isEmpty(val)) {
                continue;
            }

            istr.append(s).append(":").append(val);
            istr.append(";");
        }
        istr.append("}");
        return istr;
    }

    /**
     * 比较变更字段
     */
    public static StringBuilder updateStr(Object oldObj, Object newObj, JSONObject fieldJson, JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{(id=" + BeanUtil.getProperty(oldObj, "id") + ")");
        for (String name : arrayList) {
            String key = fieldJson.getString(name);
            Object oldStr = BeanUtil.getProperty(oldObj, key);
            Object newStr = BeanUtil.getProperty(newObj, key);
            if (ObjectUtil.isEmpty(oldStr) && ObjectUtil.isEmpty(newStr)) {
                continue;
            }

            if (ObjectUtil.equals(newStr, oldStr)) {
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("oldStr", oldStr);
            jsonObject.put("newStr", newStr);
            jsonArray.add(jsonObject);
            stringBuilder.append(name).append(":");
            stringBuilder.append(oldStr).append("->").append(newStr);

            stringBuilder.append(";");
        }
        stringBuilder.append("}");
        return stringBuilder;
    }

    /**
     * 比较所有记录字段
     */
    public static JSONArray recordField(Object newEntity, Object oldEntity) {
        JSONArray jsonArray = new JSONArray();
        Object object = null;
        List<Field> allFields = new ArrayList<>();
        try {
            if (oldEntity == null) {
                oldEntity = newEntity.getClass().newInstance();
            }
            object = oldEntity.getClass().newInstance();

            TransferUtils.transfer(newEntity, object);
            allFields = Arrays.stream(ReflectUtil.getFields(oldEntity.getClass())).collect(Collectors.toList());
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        for (Field field : allFields) {
            field.setAccessible(true);
            try {
                String oldStr = "";
                Object o = field.get(oldEntity);
                if (o != null) {
                    if (o instanceof Date) {
                        oldStr = DateUtil.formatDate((Date) o);
                    } else if (o instanceof BigDecimal) {
                        oldStr = ((BigDecimal) o).setScale(2, RoundingMode.DOWN).toPlainString();
                    } else {
                        oldStr = o.toString();
                    }

                }
                Object o1 = field.get(object);
                String newStr = "";
                if (o1 != null) {
                    if (o1 instanceof Date) {
                        newStr = DateUtil.formatDate((Date) o1);
                    } else if (o1 instanceof BigDecimal) {
                        newStr = ((BigDecimal) o1).setScale(2, RoundingMode.DOWN).toPlainString();
                    } else {
                        newStr = o1.toString();
                    }

                }
                if (!StrUtil.equals(newStr, oldStr)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", field.getName());
                    jsonObject.put("oldStr", oldStr);
                    jsonObject.put("newStr", newStr);
                    jsonArray.add(jsonObject);
                }
            } catch (Exception ignored) {
            }
        }
        return jsonArray;
    }

    /**
     * 去除参数
     */
    public static String removeQuery(String url) {
        if (StrUtil.isBlank(url)) {
            return url;
        }
        int i = url.indexOf("?");
        if (i == -1) {
            return url;
        }
        return URLUtil.decode(url.substring(0, url.indexOf("?")));
    }

    public static void removeQueryList(List list, String... p) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (Object o : list) {
            removeQuery(o, p);
        }
    }

    public static void removeQuery(Object o, String... p) {
        if (ObjectUtil.isEmpty(o) || ObjectUtil.isEmpty(p)) {
            return;
        }
        for (String s : p) {
            String url = BeanUtil.getProperty(o, s);
            if (StrUtil.isBlank(url)) {
                continue;
            }

            BeanUtil.setProperty(o, s, removeQuery(url));
        }
    }

    public static void removeQuerySplit(Object o, String split, String p) {
        if (ObjectUtil.isEmpty(o) || ObjectUtil.isEmpty(p)) {
            return;
        }
        String url = BeanUtil.getProperty(o, p);
        if (StrUtil.isBlank(url)) {
            return;
        }
        String newUrl = StrUtil.split(url, StrUtil.COMMA).stream().map(CommonUtil::removeQuery).collect(Collectors.joining(","));
        BeanUtil.setProperty(o, p, newUrl);
    }

    /**
     * 判断是否图片
     */
    public static boolean isImage(String fileName, boolean throwException) {
        String s = FileUtil.extName(fileName).toLowerCase();
        if (ArrayUtil.contains(image_accept, s)) {
            return true;
        } else {
            if (throwException) {
                throw new PlatformException(-103, "图片格式只支持:" + ArrayUtil.join(image_accept, ","));
            }
            return false;
        }
    }


    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.toList());
    }

    public static <T, U extends Enum<U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
                (c1, c2) -> Integer.compare(keyExtractor.apply(c1).ordinal(), keyExtractor.apply(c2).ordinal());
    }

    public static <T, K> Collector<T, ?, Map<K, T>> toMap(Function<? super T, ? extends K> classifier) {
        return toMap(classifier, Function.identity());
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> classifier, Function<? super T, ? extends U> mapper) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), value -> mapper.apply(value.get(0))));
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> groupingSingleBy(Function<? super T, ? extends K> classifier, Function<? super List<T>, ? extends U> mapper) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), mapper::apply));
    }

    public static <T, K> Collector<T, ?, Map<K, T>> groupingSingleBy(Function<? super T, ? extends K> classifier) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), (it) -> it.get(0)));
    }

    public static <T, U extends Comparable<U>> Comparator<T> nullFirstComparing(Function<? super T, ? extends U> keyExtractor) {
        return comparing(keyExtractor, false);
    }

    public static <T, U extends Comparable<U>> Comparator<T> nullLastComparing(Function<? super T, ? extends U> keyExtractor) {
        return comparing(keyExtractor, true);
    }

    public static <T, U extends Comparable<U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, boolean nullLast) {
        return Comparator.comparing(keyExtractor, nullLast ? Comparator.nullsLast(Comparable::compareTo) : Comparator.nullsFirst(Comparable::compareTo));
    }

    public static <T, K, U> Collector<T, ?, Map<K, List<U>>> groupingBy(Function<? super T, ? extends K> classifier, Function<? super T, ? extends U> mapper) {
        return Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.mapping(mapper, Collectors.toList()));
    }

    public static <T, V> Collector<T, ?, Map<T, V>> toKeyMap(Function<? super T, ? extends V> classifier) {
        return toMap(Function.identity(), classifier);
    }

    public static <T, V> Map<V, List<T>> inverse(Map<T, V> map) {
        final Map<V, List<T>> result = MapUtil.createMap(map.getClass());
        map.forEach((key, value) -> {
            List<T> list = result.computeIfAbsent(value, v -> new ArrayList<>());
            list.add(key);
            result.put(value, list);
        });
        return result;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    // 封装Hutool的StrJoiner
    public static <T> String saftyStrJoin(CharSequence delimiter, List<T> list, Function<T, Object> func) {
        if (CollUtil.isEmpty(list)) return "";
        return saftyStrJoin(delimiter, list.stream().map(func).map(Object::toString).toArray(Object[]::new)).toString();
    }

    public static <T> String strJoin(CharSequence delimiter, List<T> list, Function<T, Object> func) {
        if (CollUtil.isEmpty(list)) return "";
        return strJoin(delimiter, list.stream().map(func).map(StrUtil::utf8Str).toArray(Object[]::new)).toString();
    }

    public static StrJoiner strJoin(CharSequence delimiter, Object... str) {
        return strJoin(delimiter, StrJoiner.NullMode.IGNORE, str);
    }

    public static StrJoiner saftyStrJoin(CharSequence delimiter, Object... str) {
        return strJoin(delimiter).append(Arrays.stream(str).map(it -> ObjectUtil.isNotEmpty(it) ? it : " ").collect(Collectors.toList()));
    }

    public static StrJoiner strJoin(CharSequence delimiter, StrJoiner.NullMode nullMode, Object... str) {
        return StrJoiner.of(delimiter).setNullMode(nullMode).append(str);
    }

    public static StrJoiner appendPreAndSuffix(StrJoiner joiner, CharSequence prefix, CharSequence suffix) {
        return joiner.setPrefix(prefix).setSuffix(suffix);
    }

    public static <T> T listGet(Collection<T> collection, int index, T defaultValue) {
        try {
            return CollUtil.get(collection, index);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static boolean judge(Collection<Integer> judgeList, int index, int defaultValue) {
        return judge(judgeList, index, defaultValue, (judge) -> NumberUtil.equals(judge, (Number) 1));
    }

    public static boolean judge(Collection<Integer> judgeList, int index, int defaultValue, Function<Integer, Boolean> handler) {
        Integer result = CollUtil.get(judgeList, index);
        return result == null ? handler.apply(defaultValue) : handler.apply(result);
    }

    public static <T> Comparator<? super T> sizeNameSort(Function<? super T, String> func) {
        return Comparator.comparing((it) -> {
            String sizeName = func.apply(it);
            int base = 1;
            Integer rate = null;
            for (char c : sizeName.toCharArray()) {
                if (c == 'X') {
                    base += 1;
                } else if (c == 'S') {
                    rate = -1;
                } else if (c == 'M') {
                    rate = 0;
                } else if (c == 'L') {
                    rate = 1;
                }
            }
            if (rate == null) return Integer.MIN_VALUE;
            return rate * base;
        });
    }

    public static Comparator<? super String> sizeNameSort() {
        return sizeNameSort(Function.identity());
    }

    public static <T> BigDecimal sumBigDecimal(Collection<T> list, Function<T, BigDecimal> func) {
        return BigDecimal.valueOf(list.stream().map(func).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP);
    }

    public static <T> List<T> listFlatten(List<T>... sourceList) {
        return Arrays.stream(sourceList).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static <T> List<T> listTreeFlatten(List<T> sourceList, Function<T, List<T>> childrenListFunc) {
        List<T> result = new ArrayList<>();
        if (CollUtil.isEmpty(sourceList)) return sourceList;
        for (T source : sourceList) {
            result.add(source);
            listTreeFlatten(result, source, childrenListFunc);
        }
        return result;
    }

    private static <T> void listTreeFlatten(List<T> newList, T source, Function<T, List<T>> childrenListFunc) {
        if (source == null) return;
        List<T> list = childrenListFunc.apply(source);
        if (CollUtil.isEmpty(list)) return;
        for (T t : list) {
            newList.add(t);
            listTreeFlatten(newList, t, childrenListFunc);
        }
    }

    public static <T> Supplier<? extends T> getListOne(List<T> list, T dto) {
        list.add(dto);
        return () -> dto;
    }

    public static <T> Collection<T> filterNotEmpty(List<T> list, Function<T, ?> func) {
        return CollUtil.filterNew(list, notEmptyFunc(func));
    }

    public static <T> Collector<T, ?, Map<Boolean, List<T>>> groupNotBlank(Function<T, ?> classifier) {
        return Collectors.groupingBy((it) -> notEmptyFunc(classifier).accept(it), LinkedHashMap::new, Collectors.toList());
    }

    public static <T> Filter<T> notEmptyFunc(Function<T, ?> func) {
        return (t) -> ObjectUtil.isNotEmpty(func.apply(t));
    }

    public static <T> List<List<T>> flattenStructure(List<T> list, Function<T, List<T>> func) {
        List<List<T>> result = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        result.add(index.getAndIncrement(), list);
        do {
            list = list.stream().map(func).filter(CollUtil::isNotEmpty)
                    .flatMap(Collection::stream).collect(Collectors.toList());
            result.add(index.getAndIncrement(), list);
        } while (list.stream().anyMatch(it -> CollUtil.isNotEmpty(func.apply(it))));
        return result;
    }

    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (StrUtil.isEmpty(str) || StrUtil.isEmpty(suffix)) {
            return StrUtil.str(str);
        }

        String str2 = str.toString();
        if (str2.endsWith(suffix.toString())) {
            str2 = removeSuffix(StrUtil.subPre(str2, str2.length() - suffix.length()), suffix);
        }
        return str2;
    }

    public static <T> Collector<T, List<T>, List<T>> toNRNEList() {
        return new Collector<T, List<T>, List<T>>() {
            @Override
            public Supplier<List<T>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<T>, T> accumulator() {
                return (list, it) -> {
                    if (ObjectUtil.isNotEmpty(it)) {
                        CollUtil.addIfAbsent(list, it);
                    }
                };
            }

            @Override
            public BinaryOperator<List<T>> combiner() {
                return (left, right) -> {
                    CollUtil.removeWithAddIf(right, ObjectUtil::isEmpty);
                    return CollUtil.addAllIfNotContains(left, left);
                };
            }

            @Override
            public Function<List<T>, List<T>> finisher() {
                return (it) -> it;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
            }
        };
    }

    public static String fillZeroByInt(Integer num, Integer length) {
        String numStr = String.valueOf(num);
        return StrUtil.repeat("0", Math.max(length - numStr.length(), 0)) + numStr;
    }

    public static <T, R> void upsert(BiConsumer<T, R> func, T obj, R value) {
        if (ObjectUtil.isNotEmpty(value)) {
            return;
        }
        func.accept(obj, value);
    }

    public static <T, R> R get(T obj, Function<T, R> func) {
        return Opt.ofNullable(obj).map(func).orElse(null);
    }

    public static <K, V> Map<V, List<K>> reverseGrouping(Map<K, V> sourceMap) {
        return MapUtil.grouping(sourceMap.entrySet().stream()
                .map(entry -> MapUtil.entry(entry.getValue(), entry.getKey())).collect(Collectors.toList()));
    }

    public static Pair<Integer, Integer> intPairAddAlgorithm(Pair<Integer, Integer> s1, Pair<Integer, Integer> s2) {
        Pair<Integer, Integer> s3 = Opt.ofNullable(s2).orElseGet(() -> Pair.of(0, 0));
        return intPairAddAlgorithm(s1, s3.getKey(), s3.getValue());
    }

    public static Pair<Integer, Integer> intPairAddAlgorithm(Pair<Integer, Integer> s1, Integer key, Integer value) {
        Pair<Integer, Integer> s2 = Opt.ofNullable(s1).orElseGet(() -> Pair.of(0, 0));
        return Pair.of(s2.getKey() + key, s2.getValue() + value);
    }

    public static boolean biggerThanZero(Number number) {
        return NumberUtil.isGreater(NumberUtil.toBigDecimal(number), BigDecimal.ZERO);
    }

    public static List<Long> strToLongList(String string) {
        List<String> strings = StrUtil.splitTrim(string, ",");
        return CollUtil.map(strings, Long::parseLong, true);
    }

    public static List<Integer> strToIntList(String string) {
        List<String> strings = StrUtil.splitTrim(string, ",");
        return CollUtil.map(strings, Integer::parseInt, true);
    }

    private static final JavaProjectBuilder builder = new JavaProjectBuilder();
    private static final ConcurrentHashMap<Class<?>, JavaClass> JAVA_CLASS_CACHE = new ConcurrentHashMap<>();

    /**
     * 平台代码包名前缀，仅该前缀下的类走磁盘源码定位
     */
    private static final String PLATFORM_PACKAGE_PREFIX = "com.newzkl.platform";

    /**
     * Maven 标准源码目录相对片段
     */
    private static final String SRC_MAIN_JAVA = "src" + File.separator + "main" + File.separator + "java";

    /**
     * 类 → 所在模块源码根目录 缓存，未命中缓存空串
     */
    private static final ConcurrentHashMap<String, String> CLASS_DIR_CACHE = new ConcurrentHashMap<>();

    /**
     * 工程内全部模块源码根目录缓存，首次调用时扫描
     */
    private static volatile List<File> SOURCE_ROOT_CACHE;

    /**
     * 查找某个类在项目的目录位置
     * @ext 包名 → 目录不是一一映射(如 {@code common.core.mq} 分布在 core-mq-domain / core-mq-infrastructure，
     * {@code common.core.utils} 落在 core-utils)，故按工程真实模块布局遍历候选源码根，命中源文件者为准
     *
     * @param clazz 目标类
     * @return 该类所在模块的源代码目录路径(到 src/main/java 为止)，无法解析时返回空字符串
     */
    public static String findClassDirPath(Class<?> clazz) {
        if (clazz == null || clazz.getPackage() == null) {
            return "";
        }
        String packageName = clazz.getPackage().getName();
        // 非平台代码(三方 jar)不做磁盘扫描，直接交给 classpath 兜底
        if (!packageName.startsWith(PLATFORM_PACKAGE_PREFIX)) {
            return "";
        }

        return CLASS_DIR_CACHE.computeIfAbsent(clazz.getName(), key -> {
            String relativePath = packageName.replace('.', File.separatorChar)
                    + File.separator + topLevelSimpleName(clazz) + ".java";
            for (File sourceRoot : listSourceRoots()) {
                if (new File(sourceRoot, relativePath).isFile()) {
                    return sourceRoot.getPath();
                }
            }
            System.err.println("无法定位类" + clazz.getName() + "的源码目录，跳过该类");
            return "";
        });
    }

    /**
     * 取最外层类的简单名(内部类去掉 {@code $Inner} 后缀)，用于匹配磁盘上的 .java 文件名。
     *
     * @param clazz 目标类
     * @return 最外层类简单名
     */
    private static String topLevelSimpleName(Class<?> clazz) {
        String name = StrUtil.subAfter(clazz.getName(), ".", true);
        return StrUtil.subBefore(name, "$", false);
    }

    /**
     * 扫描工程内全部 Maven 模块的 {@code src/main/java} 目录
     * @ext 从当前工作目录向上找到聚合工程根(最外层含 pom.xml 的目录)，再沿含 pom.xml 的子目录递归，
     * 天然适配 biz/biz-xxx/biz-xxx-yyy、common/core/core-xxx、common/core/core-mq/core-mq-yyy 等多级嵌套
     *
     * @return 源码根目录列表，找不到工程根时返回空列表
     */
    private static List<File> listSourceRoots() {
        List<File> cached = SOURCE_ROOT_CACHE;
        if (cached != null) {
            return cached;
        }
        synchronized (CommonUtil.class) {
            if (SOURCE_ROOT_CACHE != null) {
                return SOURCE_ROOT_CACHE;
            }
            List<File> roots = new ArrayList<>();
            File projectRoot = findProjectRoot();
            if (projectRoot != null) {
                collectSourceRoots(projectRoot, roots);
            }
            SOURCE_ROOT_CACHE = roots;
            return roots;
        }
    }

    /**
     * 从工作目录向上定位聚合工程根目录
     *
     * @return 最外层含 pom.xml 的目录，均不含时返回 null
     */
    private static File findProjectRoot() {
        File current = new File(System.getProperty("user.dir", ".")).getAbsoluteFile();
        File root = null;
        while (current != null) {
            if (new File(current, "pom.xml").isFile()) {
                root = current;
            }
            current = current.getParentFile();
        }
        return root;
    }

    /**
     * 沿模块树递归收集源码根目录
     *
     * @param moduleDir 模块目录
     * @param roots     收集容器
     */
    private static void collectSourceRoots(File moduleDir, List<File> roots) {
        File sourceRoot = new File(moduleDir, SRC_MAIN_JAVA);
        if (sourceRoot.isDirectory()) {
            roots.add(sourceRoot);
        }
        File[] children = moduleDir.listFiles(File::isDirectory);
        if (children == null) {
            return;
        }
        for (File child : children) {
            // 仅下钻 Maven 模块目录，跳过 target / .git / node_modules 等
            if (new File(child, "pom.xml").isFile()) {
                collectSourceRoots(child, roots);
            }
        }
    }

    /**
     * 通过QDox解析器获取指定类的JavaClass对象
     * @ext 带缓存
     * @param clazz 目标类
     * @return QDox JavaClass对象
     * @throws Exception 找不到源文件或解析失败时抛出
     */
    public static JavaClass findJavaClass(Class<?> clazz) throws Exception {
        // 从缓存中获取
        if (JAVA_CLASS_CACHE.containsKey(clazz)) {
            return JAVA_CLASS_CACHE.get(clazz);
        }

        // 加载类的源文件
        loadJavaSource(clazz);

        // 获取QDox的JavaClass对象
        JavaClass javaClass = builder.getClassByName(clazz.getName());
        if (javaClass == null) {
            throw new IllegalArgumentException("无法找到类: " + clazz.getName());
        }
        // 缓存JavaClass对象
        JAVA_CLASS_CACHE.put(clazz, javaClass);
        return javaClass;
    }

    /**
     * 查 JavaField — QDox JavaClass.getFieldByName 仅本类字段；沿父类链递归查父类字段
     *
     * @param javaClass QDox JavaClass
     * @param fieldName 字段名
     * @return QDox JavaField；找不到返回 null
     */
    public static JavaField findJavaField(JavaClass javaClass, String fieldName) {
        if (javaClass == null) return null;
        // 本类命中直接返
        JavaField field = javaClass.getFieldByName(fieldName);
        if (field != null) return field;

        // 父类不存在或到 Object 终止
        JavaClass superJavaClass = javaClass.getSuperJavaClass();
        try {
            Class<?> clazz = Class.forName(superJavaClass.getFullyQualifiedName());
            return findJavaField(findJavaClass(clazz), fieldName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载Java源文件到QDox解析器
     *
     * @param clazz 目标类
     * @return 加载成功返回 true
     * @throws IOException 文件读取失败时抛出
     */
    private static boolean loadJavaSource(Class<?> clazz) throws IOException {
        /*
         * 优先按开发期磁盘路径读；生产/测试环境走 classpath 内嵌 .java
         * (依赖 parent pom 把 src/main/java 下 *.java 作为资源打入 jar)
         */
        String moduleSourceDir = findClassDirPath(clazz);
        String className = clazz.getName().replace('.', '/');
        File moduleFile = new File(moduleSourceDir, className + ".java");

        // 磁盘源
        if (moduleFile.exists() && moduleFile.isFile()) {
            builder.addSource(moduleFile);
            return true;
        }

        // classpath 内嵌 .java
        try (InputStream in = clazz.getClassLoader().getResourceAsStream(className + ".java")) {
            if (in != null) {
                builder.addSource(new InputStreamReader(in, StandardCharsets.UTF_8));
                return true;
            }
        } catch (Exception e) {
            System.err.println("从classpath读取源文件失败: " + e.getMessage());
        }

        System.err.println("无法找到源文件: " + clazz.getName());
        return false;
    }
}
