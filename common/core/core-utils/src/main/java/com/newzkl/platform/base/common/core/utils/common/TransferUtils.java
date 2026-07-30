package com.newzkl.platform.base.common.core.utils.common;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 对象 / 集合 / 分页转换工具 (替代 BeanUtils.copyProperties)。
 *
 * @author fang
 */
@Slf4j
public class TransferUtils {

    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new HashMap<>();

    public static <D> D newInstance(Supplier<D> supplier, Consumer<D> consumer) {
        D d = supplier.get();
        if (consumer != null) {
            consumer.accept(d);
        }
        return d;
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier) {
        return transfer(s, supplier, null, null);
    }

    public static <S, D> D transfer(S s, D obj) {
        return transfer(s, () -> obj, null, null);
    }

    public static <S, D> D transfer(S s, D obj, CopyOptions copyOptions) {
        return transfer(s, () -> obj, null, copyOptions);
    }

    public static <S, D> D transfer(S s, Class<D> clazz) {
        return transfer(s, clazz, null, null);
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier, CopyOptions copyOptions) {
        return transfer(s, supplier, null, copyOptions);
    }

    public static <S, D> D transfer(S s, Class<D> clazz, BiConsumer<S, D> biConsumer) {
        return transfer(s, () -> ReflectUtil.newInstance(clazz), biConsumer);
    }

    public static <S, D> D transfer(S s, Class<D> clazz, BiConsumer<S, D> biConsumer, CopyOptions copyOptions) {
        return transfer(s, () -> ReflectUtil.newInstance(clazz), biConsumer, copyOptions);
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        return transfer(s, supplier, biConsumer, null);
    }

    public static <S, D> D transfer(S s, Supplier<D> supplier, BiConsumer<S, D> biConsumer, CopyOptions copyOptions) {
        if (s == null) {
            return null;
        }
        D d = supplier.get();
        cn.hutool.core.bean.BeanUtil.copyProperties(s, d, copyOptions);
        if (biConsumer != null) {
            biConsumer.accept(s, d);
        }
        return d;
    }

    public static <S, D> D transfer(S s, Function<S, D> function) {
        return function.apply(s);
    }

    public static <S, D> List<D> transfers(Collection<S> list, Class<D> clazz) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper -> transfer(mapper, clazz)).collect(Collectors.toList());
    }

    public static <S, D> List<D> transfers(Collection<S> list, Supplier<D> supplier) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper -> transfer(mapper, supplier)).collect(Collectors.toList());
    }

    public static <S, D> List<D> transfers(Collection<S> list, Class<D> clazz, BiConsumer<S, D> biConsumer) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper -> transfer(mapper, clazz, biConsumer)).collect(Collectors.toList());
    }

    public static <S, D> List<D> transfers(Collection<S> list, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper -> transfer(mapper, supplier, biConsumer)).collect(Collectors.toList());
    }

    public static <S, D> List<D> transfers(Collection<S> list, Function<S, D> function) {
        if (list == null) {
            return null;
        }
        List<D> ds = new ArrayList<>();
        list.forEach(s -> {
            D d = function.apply(s);
            ds.add(d);
        });
        return ds;
    }

    public static <S, D> List<D> transfers(Collection<S> list, Function<S, D> function, BiConsumer<S, D> biConsumer) {
        if (list == null) {
            return null;
        }
        List<D> ds = new ArrayList<>();
        list.forEach(s -> {
            D d = function.apply(s);
            ds.add(d);
            if (biConsumer != null) {
                biConsumer.accept(s, d);
            }
        });
        return ds;
    }

    /**
     * 转换分页对象 - 使用 Function 方式
     *
     * @param sourcePage 源分页对象
     * @param function   目标对象的构造器
     * @param <S>        源对象类型
     * @param <D>        目标对象类型
     * @return 转换后的分页对象
     */
    public static <S, D> Page<D> transferPage(Page<S> sourcePage, Function<S, D> function) {
        return transferPage(sourcePage, function, null);
    }

    public static <S, D> Page<D> transferPage(Page<S> sourcePage, Supplier<D> supplier) {
        return transferPage(sourcePage, supplier, null);
    }

    public static <S, D> Page<D> transferPage(Page<S> sourcePage, Class<D> clazz) {
        return transferPage(sourcePage, clazz, null);
    }

    public static <S, D> Page<D> transferPage(Page<S> sourcePage, Function<S, D> function, BiConsumer<S, D> biConsumer) {
        return transfer(sourcePage, (List<S> list) -> transfers(list, function, biConsumer));
    }

    public static <S, D> Page<D> transferPage(Page<S> sourcePage, Class<D> clazz, BiConsumer<S, D> biConsumer) {
        return transfer(sourcePage, (List<S> list) -> transfers(list, clazz, biConsumer));
    }

    public static <S, D> Page<D> transferPage(Page<S> sourcePage, Supplier<D> supplier, BiConsumer<S, D> biConsumer) {
        return transfer(sourcePage, (List<S> list) -> transfers(list, supplier, biConsumer));
    }

    private static <S, D> Page<D> transfer(Page<S> sourcePage, Function<List<S>, List<D>> function) {
        if (sourcePage == null) {
            return null;
        }
        Page<D> targetPage = (Page<D>) sourcePage;
        if (CollUtil.isNotEmpty(sourcePage.getRecords())) {
            List<D> targetRecords = function.apply(sourcePage.getRecords());
            targetPage.setRecords(targetRecords);
        }

        return targetPage;
    }

}
