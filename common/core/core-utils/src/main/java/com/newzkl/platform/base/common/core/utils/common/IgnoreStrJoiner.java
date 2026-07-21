package com.newzkl.platform.base.common.core.utils.common;

import cn.hutool.core.collection.ArrayIter;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Iterator;
import java.util.function.Function;

/**
 * 忽略 null 值的字符串拼接器。
 *
 * @author fang
 */
public class IgnoreStrJoiner implements CharSequence {

    private static final StrJoiner.NullMode nullMode = StrJoiner.NullMode.IGNORE;
    private final StrJoiner strJoiner;

    public IgnoreStrJoiner() {
        this.strJoiner = StrJoiner.of(",").setNullMode(nullMode);
    }

    public IgnoreStrJoiner(StrJoiner strJoiner) {
        this.strJoiner = strJoiner.setNullMode(nullMode);
    }

    public IgnoreStrJoiner(CharSequence delimiter) {
        this.strJoiner = StrJoiner.of(delimiter).setNullMode(nullMode);
    }

    public IgnoreStrJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        this.strJoiner = StrJoiner.of(delimiter, prefix, suffix).setNullMode(nullMode);
    }

    public static IgnoreStrJoiner of() {
        return new IgnoreStrJoiner();
    }

    public static IgnoreStrJoiner of(StrJoiner joiner) {
        return new IgnoreStrJoiner(joiner);
    }

    public static IgnoreStrJoiner of(CharSequence delimiter) {
        return new IgnoreStrJoiner(delimiter);
    }

    public static IgnoreStrJoiner of(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return new IgnoreStrJoiner(delimiter, prefix, suffix);
    }

    public static String easy2Str(String... appendList) {
        IgnoreStrJoiner ignoreStrJoiner = new IgnoreStrJoiner();
        for (String s : appendList) {
            ignoreStrJoiner.append(s);
        }
        return ignoreStrJoiner.toString();
    }

    public static String toStr(String delimiter, String... appendList) {
        IgnoreStrJoiner ignoreStrJoiner = new IgnoreStrJoiner(delimiter);
        for (String s : appendList) {
            ignoreStrJoiner.append(s);
        }
        return ignoreStrJoiner.toString();
    }

    public static String full2Str(String delimiter, CharSequence prefix, CharSequence suffix, String... appendList) {
        IgnoreStrJoiner ignoreStrJoiner = new IgnoreStrJoiner(delimiter, prefix, suffix);
        for (String s : appendList) {
            ignoreStrJoiner.append(s);
        }
        return ignoreStrJoiner.toString();
    }

    public IgnoreStrJoiner setDelimiter(CharSequence delimiter) {
        this.strJoiner.setDelimiter(delimiter);
        return this;
    }

    public IgnoreStrJoiner setPrefix(CharSequence prefix) {
        this.strJoiner.setPrefix(prefix);
        return this;
    }

    public IgnoreStrJoiner setSuffix(CharSequence suffix) {
        this.strJoiner.setSuffix(suffix);
        return this;
    }

    public IgnoreStrJoiner setWrapElement(boolean wrapElement) {
        this.strJoiner.setWrapElement(wrapElement);
        return this;
    }

    public IgnoreStrJoiner setEmptyResult(String emptyResult) {
        this.strJoiner.setEmptyResult(emptyResult);
        return this;
    }

    public IgnoreStrJoiner append(Object obj) {
        if (null == obj) {
            append((CharSequence) null);
        } else if (ArrayUtil.isArray(obj)) {
            append(new ArrayIter<>(obj));
        } else if (obj instanceof Iterator) {
            append((Iterator<?>) obj);
        } else if (obj instanceof Iterable) {
            append(((Iterable<?>) obj).iterator());
        } else {
            append(ObjectUtil.toString(obj));
        }
        return this;
    }

    public <T> IgnoreStrJoiner append(T[] array) {
        if (null == array) {
            return this;
        }
        return append(new ArrayIter<>(array));
    }

    public <T> IgnoreStrJoiner append(Iterator<T> iterator) {
        if (null != iterator) {
            while (iterator.hasNext()) {
                append(iterator.next());
            }
        }
        return this;
    }

    public <T> IgnoreStrJoiner append(T[] array, Function<T, ? extends CharSequence> toStrFunc) {
        return append((Iterator<T>) new ArrayIter<>(array), toStrFunc);
    }

    public <E> IgnoreStrJoiner append(Iterable<E> iterable, Function<? super E, ? extends CharSequence> toStrFunc) {
        return append(IterUtil.getIter(iterable), toStrFunc);
    }

    public <E> IgnoreStrJoiner append(Iterator<E> iterator, Function<? super E, ? extends CharSequence> toStrFunc) {
        if (null != iterator) {
            while (iterator.hasNext()) {
                append(toStrFunc.apply(iterator.next()));
            }
        }
        return this;
    }

    public IgnoreStrJoiner append(CharSequence csq) {
        return append(csq, 0, StrUtil.length(csq));
    }

    public IgnoreStrJoiner append(CharSequence csq, int startInclude, int endExclude) {
        this.strJoiner.append(csq, startInclude, endExclude);
        return this;
    }

    public IgnoreStrJoiner append(char c) {
        return append(String.valueOf(c));
    }

    public IgnoreStrJoiner merge(StrJoiner strJoiner) {
        this.strJoiner.merge(strJoiner);
        return this;
    }

    @Override
    public int length() {
        return this.strJoiner.length();
    }

    @Override
    public char charAt(int index) {
        return this.strJoiner.toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.strJoiner.toString().substring(start, end);
    }

    @Override
    public String toString() {
        return this.strJoiner.toString();
    }
}
