package com.newzkl.platform.base.common.core.utils.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import cn.hutool.core.date.DateUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义Logback Appender：将Error日志发送到飞书
 *
 * <p>同 key 报警 12 小时内仅发送一次</p>
 */
@Data
public class FeiShuErrorAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    /*
     * dedup 缓存: key = exceptionClass + ":" + msg前50字符, TTL 12h
     * Caffeine maximumSize 防止 key 爆炸
     */
    private static final Cache<String, Boolean> DEDUP_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(12, TimeUnit.HOURS)
            .maximumSize(10_000)
            .build();

    /**
     * 业务包前缀
     * @ext 可通过 logback.xml 多次配置 {@code <bizPackagePrefix>} 追加
     */
    private List<String> bizPackagePrefix = new ArrayList<>(List.of("com.zhongze.chicken"));

    /**
     * 异常栈输出最大行数
     */
    private int maxStackLines = 8;

    /**
     * logback DI 单值追加
     * @ext 支持 XML 配置 {@code <bizPackagePrefix>...</bizPackagePrefix>} 多次声明
     */
    public void addBizPackagePrefix(String prefix) {
        if (prefix != null && !prefix.isBlank()) {
            this.bizPackagePrefix.add(prefix);
        }
    }

    /**
     * 处理日志事件，将ERROR级别日志推送至飞书
     *
     * @param event Logback日志事件
     */
    @Override
    protected void append(ILoggingEvent event) {
        if (SecurityContextHolder.isLocal()) {
            return;
        }

        if (!event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            return;
        }

        String dedupKey = buildDedupKey(event);
        if (DEDUP_CACHE.getIfPresent(dedupKey) != null) {
            return;
        }
        DEDUP_CACHE.put(dedupKey, Boolean.TRUE);

        String logType = event.getMDCPropertyMap().getOrDefault("logType", "app");
        String traceId = event.getMDCPropertyMap().getOrDefault("traceId", "-");

        StringBuilder logContent = new StringBuilder();
        logContent.append(String.format("【ADOPT-CHICKEN ERROR-%s】\n", logType.toUpperCase()))
                .append(String.format("时间：%s\n", DateUtil.date(DateUtil.calendar(event.getTimeStamp())).toString()))
                .append(String.format("trace：%s\n", traceId))
                .append(String.format("日志类：%s\n", event.getLoggerName()))
                .append(String.format("内容：%s\n", event.getFormattedMessage()));

        if (event.getThrowableProxy() != null) {
            IThrowableProxy throwable = event.getThrowableProxy();
            String rootCause = buildRootCause(throwable);
            if (!rootCause.isEmpty()) {
                logContent.append(String.format("根因：%s\n", rootCause));
            }
            String stack = buildCompactStack(throwable);
            if (!stack.isEmpty()) {
                logContent.append(String.format("异常栈：\n%s\n", stack));
            }
        }

        try {
            FeiShuMessageSendUtil.sendTextMessage(logContent.toString());
        } catch (Exception e) {
            addError("飞书Error日志发送失败", e);
        }
    }

    /**
     * 构造紧凑异常栈
     * <p>规则：</p>
     * <ul>
     * <li>排除动态代理帧</li>
     * <li>业务包前缀帧全部保留</li>
     * <li>非业务包前缀帧按"最深公共前缀分组"折叠：每组只取首帧</li>
     * <li>分组键 = 类全名各段中"首字母 + 与下一帧首段差异片段"，例 a.b.c.d.a / a.b.c.d.a1 → 同组</li>
     * <li>最多输出 maxStackLines 行</li>
     * </ul>
     * @ext className 含 {@code $$} 或 {@code $Proxy}
     * @ext a.b.c.d 公共，a vs a1 差异
     */
    private String buildCompactStack(IThrowableProxy throwable) {
        StackTraceElementProxy[] frames = throwable.getStackTraceElementProxyArray();
        if (frames == null || frames.length == 0) {
            return "";
        }
        List<String> output = new ArrayList<>();
        Set<String> seenGroupKeys = new HashSet<>();
        String[] classNames = new String[frames.length];
        for (int i = 0; i < frames.length; i++) {
            classNames[i] = frames[i].getStackTraceElement().getClassName();
        }

        for (int i = 0; i < frames.length && output.size() < maxStackLines; i++) {
            String className = classNames[i];
            // 跳过动态代理
            if (className.contains("$$") || className.contains("$Proxy")) {
                continue;
            }
            // 业务包前缀全保留
            if (matchBizPrefix(className)) {
                output.add(frames[i].toString());
                continue;
            }
            // 同组只取首帧
            String groupKey = computeGroupKey(className, i + 1 < classNames.length ? classNames[i + 1] : null);
            if (seenGroupKeys.add(groupKey)) {
                output.add(frames[i].toString());
            }
        }
        return String.join("\n", output);
    }

    /**
     * 分组键：与下一帧的最深公共包前缀路径
     * <p>例：a.b.c.d.a vs a.b.c.d.a1 → key = "a.b.c.d"；a.b.c.d1 vs a.b.c1 → key = "a.b"</p>
     * <p>无下一帧或完全不同 → key 为类全名</p>
     * @ext 独立分组
     */
    private String computeGroupKey(String current, String next) {
        if (next == null) {
            return current;
        }
        String[] currentSegs = current.split("\\.");
        String[] nextSegs = next.split("\\.");
        int common = 0;
        int max = Math.min(currentSegs.length, nextSegs.length);
        while (common < max && currentSegs[common].equals(nextSegs[common])) {
            common++;
        }
        if (common == 0) {
            return current;
        }
        StringBuilder sb = new StringBuilder(currentSegs[0]);
        for (int i = 1; i < common; i++) {
            sb.append('.').append(currentSegs[i]);
        }
        return sb.toString();
    }

    /**
     * 类名命中任一业务包前缀
     */
    private boolean matchBizPrefix(String className) {
        for (String prefix : bizPackagePrefix) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 提取最深 Caused by 根因 — 形式 {@code 类名: 消息}
     * <p>沿 cause 链下钻到末端，输出末端异常类、消息、第一条业务包帧</p>
     * @ext +业务首帧定位
     * @ext 若有
     */
    private String buildRootCause(IThrowableProxy throwable) {
        IThrowableProxy cur = throwable;
        while (cur.getCause() != null) {
            cur = cur.getCause();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(cur.getClassName());
        String msg = cur.getMessage();
        if (msg != null && !msg.isEmpty()) {
            sb.append(": ").append(msg);
        }
        StackTraceElementProxy[] frames = cur.getStackTraceElementProxyArray();
        if (frames != null) {
            for (StackTraceElementProxy frame : frames) {
                String cn = frame.getStackTraceElement().getClassName();
                if (cn.contains("$$") || cn.contains("$Proxy")) {
                    continue;
                }
                if (matchBizPrefix(cn)) {
                    sb.append(" @ ").append(frame.toString());
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 构造去重 key — 异常类名 + 消息前 50 字符
     */
    private String buildDedupKey(ILoggingEvent event) {
        String exClass = "NO_EX";
        IThrowableProxy throwable = event.getThrowableProxy();
        if (throwable != null) {
            IThrowableProxy cur = throwable;
            while (cur.getCause() != null) {
                cur = cur.getCause();
            }
            exClass = cur.getClassName();
        }
        String msg = event.getFormattedMessage();
        if (msg == null) {
            msg = "";
        } else if (msg.length() > 50) {
            msg = msg.substring(0, 50);
        }
        return exClass + ":" + msg;
    }
}
