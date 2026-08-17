package com.newzkl.platform.base.common.core.model.money;

import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONString;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;

/**
 * Money 值对象 — 子类化 hutool Money, 工厂方法按分/元分流
 * <p>of 入参为分; of 入参为元.</p>
 * <p>NULL sentinel: of / null 输入返回 {@link Money#NULL} 单例 ,
 * 业务运算 / 序列化 / TypeHandler 一致按未设置语义处理, 调用方无需做 != null 判断.</p>
 * <p>业务运算用本类 static add/subtract/multiply/divide, 委派 hutool 父类同名方法 , 再包成子类型.</p>
 * <p>hutool JSONUtil 支持: 实现 {@code JSONString} 控制序列化, 注册 {@code ConverterRegistry} 自定义转换器控制 toBean 反序列化.</p>
 * @ext Long
 * @ext Long cent
 * @ext null
 * @ext immutable, 返回新值
 */
@NoArgsConstructor
public class Money extends cn.hutool.core.math.Money implements JSONString {

    public Money(Long cent) {
        this.setCent(Opt.ofNullable(cent).orElse(0L));
    }

    /**
     * NULL sentinel — 表示「未设置」, 与 ZERO (0 元) 区分
     */
    private static final Money NULL = new Money(BigDecimal.ZERO);
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    public static final Money HUNDRED = Money.of("100");

    static {
        // 注册到 hutool ConverterRegistry, 让 JSONUtil.toBean / BeanUtil.copyProperties 等遇 Money 字段自动走自定义转换
        ConverterRegistry.getInstance().putCustom(Money.class,
                (Converter<Money>) (value, defaultValue) -> hutoolConvert(value));
    }


    /**
     * 按分构造
     * @ext DB / TypeHandler 入口
     */
    public static Money of(Long cent) {
        if (cent == null) return ZERO;
        return new Money(BigDecimal.valueOf(cent, Currency.getInstance(DEFAULT_CURRENCY_CODE).getDefaultFractionDigits()));
    }

    public static Money of(Integer cent) {
        if (cent == null) return ZERO;
        return new Money(BigDecimal.valueOf(cent, Currency.getInstance(DEFAULT_CURRENCY_CODE).getDefaultFractionDigits()));
    }

    /**
     * 元字符串构造
     */
    public static Money of(String yuan) {
        if (StrUtil.isBlank(yuan)) return ZERO;
        return new Money(new BigDecimal(yuan));
    }

    /**
     * 元 BigDecimal 构造
     */
    public static Money of(BigDecimal amount) {
        if (amount == null) return ZERO;
        return new Money(amount);
    }

    public static Money nullVal() {
        return NULL;
    }

    /**
     * 判定是否未设置
     */
    public boolean isNull() {
        return this == NULL;
    }

    /**
     * 判定是否已设置
     */
    public boolean nonNull() {
        return this != NULL;
    }

    /**
     * 判定是否为零
     */
    public boolean isZero() {
        return nonNull() && this.equals(ZERO);
    }

    /**
     * 父类返回 hutool Money, 取 cent 重新包装为子类
     */
    private static Money wrap(cn.hutool.core.math.Money m) {
        return of(m.getCent());
    }

    // ===== 静态累加 — 替代 stream().reduce(Money.ZERO, Money::add) =====

    /**
     * 集合累加 , null 元素跳过 , 空集返回 ZERO
     * @ext 替代 col.stream().reduce(Money.ZERO, Money::add)
     */
    public static Money sum(Collection<Money> monies) {
        if (monies == null || monies.isEmpty()) return ZERO;
        Money total = ZERO;
        for (Money m : monies) {
            if (m != null) total = total.add(m);
        }
        return total;
    }

    /**
     * 提取字段后累加 , null 元素 / null 字段跳过 , 空集返回 ZERO
     * @ext 替代 col.stream().map(getter).reduce(Money.ZERO, Money::add)
     */
    public static <T> Money sumBy(Collection<T> list, java.util.function.Function<T, Money> getter) {
        if (list == null || list.isEmpty()) return ZERO;
        Money total = ZERO;
        for (T t : list) {
            if (t == null) continue;
            Money m = getter.apply(t);
            if (m != null) total = total.add(m);
        }
        return total;
    }

    // ===== 静态业务运算 — 委派 hutool 父类 (immutable) =====

    /**
     * 加法
     */
    public Money add(Money money) {
        return wrap(super.add(money));
    }

    /**
     * 减法
     */
    public Money subtract(Money money) {
        return wrap(super.subtract(money));
    }

    /**
     * 整数倍
     */
    public Money multiply(long number) {
        return wrap(super.multiply((double) number));
    }

    /**
     * 比率乘 , NULL/rate=null 视为 ZERO
     * @ext a * rate, HALF_UP 保留 2 位
     */
    public Money radioMul(BigDecimal rate) {
        if (rate == null) {
            rate = BigDecimal.ZERO;
        }
        return wrap(super.multiply(rate.add(BigDecimal.ONE)));
    }

    /**
     * 整数除 , NULL 视为 ZERO
     * @ext a / n, HALF_UP 保留 2 位
     */
    public Money divide(long number) {
        return wrap(super.divide(BigDecimal.valueOf(number), RoundingMode.HALF_UP));
    }

    /**
     * BigDecimal 除 , NULL 视为 ZERO
     * @ext a / n, HALF_UP 保留 2 位
     */
    public Money divide(BigDecimal number) {
        return wrap(super.divide(number, RoundingMode.HALF_UP));
    }

    /**
     * 金额单位的数量, NULL 视为 0
     */
    public Integer quantityDiv(Money unitPrice) {
        if (isNull() || unitPrice.isNull()) return 0;
        return super.getAmount().divide(unitPrice.getAmount(), 0, RoundingMode.CEILING).intValue();
    }

    /**
     * 判断金额是否大于零, NULL 视为 false
     */
    public boolean greaterThanZero() {
        if (isNull()) return false;
        return super.greaterThan(ZERO);
    }

    public boolean greaterThan(Money other) {
        if (isNull()) return false;
        return super.greaterThan(other);
    }

    /**
     * 判断金额是否小于零, NULL 视为 false
     */
    public boolean smallerThanZero() {
        if (isNull()) return false;
        return super.getCent() < 0;
    }

    public boolean smallerThan(Money other) {
        if (isNull()) return false;
        return super.getCent() < other.getCent();
    }

    /**
     * hutool JSONString 序列化, NULL 输出 null, 否则输出带引号元字符串 "11.11"
     */
    @Override
    public String toJSONString() {
        if (isNull()) return "null";
        return "\"" + super.getAmount().toPlainString() + "\"";
    }

    /**
     * hutool ConverterRegistry 自定义转换 — JSONUtil.toBean / Convert.convert 入口
     */
    private static Money hutoolConvert(Object value) {
        if (value == null) return NULL;
        if (value instanceof Money m) return m;
        if (value instanceof cn.hutool.core.math.Money hm) return of(hm.getCent());
        if (value instanceof Number n) return of(new BigDecimal(n.toString()));
        if (value instanceof CharSequence cs) {
            String s = cs.toString().trim();
            if (StrUtil.isBlank(s) || "null".equalsIgnoreCase(s)) return NULL;
            return of(s);
        }
        return NULL;
    }

    private Money(double amount) {
        super(amount);
    }

    private Money(BigDecimal amount) {
        super(amount);
    }

    /**
     * 除以另一金额的元值(标量), NULL/null 除数视为返回 ZERO
     * @ext a / divisor.amount, HALF_UP 保留 2 位
     */
    public Money divide(Money divisor) {
        return divide(divisor, RoundingMode.HALF_UP);
    }

    /**
     * 除以另一金额的元值(标量), NULL/null 除数视为返回 ZERO
     * @ext a / divisor.amount, HALF_UP 保留 2 位
     */
    public Money divide(Money divisor, RoundingMode roundingMode) {
        if (divisor == null || divisor.isNull()) {
            return ZERO;
        }
        return wrap(super.divide(divisor.getAmount(), roundingMode));
    }

    public Money percent(double ratio) {
        return Money.of(this.getAmount().multiply(new BigDecimal(ratio))).percent();
    }

    public Money percent() {
        return this.divide(Money.HUNDRED, RoundingMode.FLOOR);
    }
}
