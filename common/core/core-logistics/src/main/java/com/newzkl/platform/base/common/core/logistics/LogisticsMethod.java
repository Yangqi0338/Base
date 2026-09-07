package com.newzkl.platform.base.common.core.logistics;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物流门面
 *
 * <p>业务方唯一入口: 快递公司目录 + 单号识别 + 轨迹查询, 三方调用与 Redis 缓存全部收在本类。
 * 纯静态方法, action / application / domain / infrastructure 各层均可直接调用</p>
 *
 * <p>公司目录来自 classpath 下 {@code kuaidi100com.csv} (快递100 官方编码表),
 * 首次访问时懒加载进进程内存, 不落库不走 Redis</p>
 */
@Slf4j
public class LogisticsMethod {

    /**
     * 快递100 官方公司编码表文件名
     */
    private static final String COMPANY_CSV = "kuaidi100com.csv";

    /**
     * 公司目录缓存, 双检锁懒加载
     */
    private static volatile Directory directory;

    /**
     * 查询快递公司列表
     *
     * @param keyword 名称或编码关键字, 为空返回全量
     * @return 快递公司列表, 顺序与官方编码表一致 (国内主流在前)
     */
    public static List<LogisticsCompany> companies(String keyword) {
        List<LogisticsCompany> all = new ArrayList<>(directory().byCode().values());
        if (StrUtil.isBlank(keyword)) {
            return all;
        }
        String trimmed = StrUtil.trim(keyword);
        List<LogisticsCompany> hit = new ArrayList<>();
        for (LogisticsCompany company : all) {
            if (StrUtil.containsIgnoreCase(company.getExpressCompanyName(), trimmed)
                    || StrUtil.containsIgnoreCase(company.getExpressCompanyCode(), trimmed)) {
                hit.add(company);
            }
        }
        return hit;
    }

    /**
     * 按编码查询单个快递公司
     *
     * @param expressCompanyCode 快递公司编码
     * @return 快递公司, 未命中返回 null
     */
    public static LogisticsCompany company(String expressCompanyCode) {
        if (StrUtil.isBlank(expressCompanyCode)) {
            return null;
        }
        return directory().byCode().get(StrUtil.trim(expressCompanyCode));
    }

    /**
     * 快递公司名称
     *
     * @param expressCompanyCode 快递公司编码
     * @return 公司名称, 未命中时回退返回编码本身
     */
    public static String companyName(String expressCompanyCode) {
        LogisticsCompany company = company(expressCompanyCode);
        return company == null ? expressCompanyCode : company.getExpressCompanyName();
    }

    /**
     * 校验并归一化快递公司名称
     *
     * <p>发货入口 (手工填写 / Excel 导入) 的公司名可能是简称、别名或带空格, 落库前必须归一到
     * 编码表官方名称, 否则后续 {@link #queryTrack} 只能靠单号识别兜底, 识别不出即整条轨迹查不到。
     * 故此处识别不出直接抛错, 不容忍脏值落库 —— Excel 导入侧会把异常转成精确行号错误</p>
     *
     * @param expressCompanyName 快递公司名称或编码, 不可为空
     * @return 编码表官方公司名称
     */
    public static String normalizeCompanyName(String expressCompanyName) {
        if (StrUtil.isBlank(expressCompanyName)) {
            throw new PlatformException(BaseErrorCode.PARAM, "快递公司名称不能为空");
        }
        String code = codeOf(expressCompanyName);
        if (StrUtil.isBlank(code)) {
            throw new PlatformException(BaseErrorCode.CUSTOM,
                    StrUtil.format("无法识别的快递公司「{}」, 请从快递公司列表中选择", StrUtil.trim(expressCompanyName)));
        }
        return companyName(code);
    }

    /**
     * 名称或编码归一化为快递公司编码
     *
     * <p>依次尝试: 编码精确命中 → 名称精确命中 → 名称模糊命中 (取编码表首个,
     * 故 "顺丰" 会命中 "顺丰速运" 而非 "顺丰国际")</p>
     *
     * @param nameOrCode 快递公司名称或编码
     * @return 快递公司编码, 识别不出返回 null
     */
    public static String codeOf(String nameOrCode) {
        if (StrUtil.isBlank(nameOrCode)) {
            return null;
        }
        String trimmed = StrUtil.trim(nameOrCode);
        Directory local = directory();
        if (local.byCode().containsKey(trimmed)) {
            return trimmed;
        }
        String code = local.nameToCode().get(trimmed);
        if (StrUtil.isNotBlank(code)) {
            return code;
        }
        for (Map.Entry<String, String> entry : local.nameToCode().entrySet()) {
            if (StrUtil.contains(entry.getKey(), trimmed)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 按单号识别快递公司编码
     *
     * <p>识别结果长缓存 (单号与快递公司的对应关系不会变), 命中缓存不再调三方</p>
     *
     * @param expressNo 快递单号
     * @return 快递公司编码, 识别不出返回 null
     */
    public static String autoCodeOf(String expressNo) {
        if (StrUtil.isBlank(expressNo)) {
            return null;
        }
        String cacheKey = RedisEnum.Key.KUAIDI100_COMPANY.getCode(expressNo);
        String cached = RedisUtil.get(cacheKey);
        if (StrUtil.isNotBlank(cached)) {
            return cached;
        }
        List<Kuaidi100Res.AutoNumRes> candidates = Kuaidi100Method.autoNum(expressNo);
        if (CollUtil.isEmpty(candidates)) {
            return null;
        }
        String code = candidates.get(0).getComCode();
        if (StrUtil.isBlank(code)) {
            return null;
        }
        RedisUtil.set(cacheKey, code, LogisticsConfig.companyCacheSeconds);
        return code;
    }

    /**
     * 确定快递公司编码
     *
     * <p>业务侧只存了快递公司中文名 (发货时人工填写), 故先按名称归一化,
     * 名称识别不出再退回按单号识别</p>
     *
     * @param nameOrCode 快递公司名称或编码, 可为空
     * @param expressNo  快递单号, 不可为空
     * @return 快递公司编码
     */
    public static String resolveCode(String nameOrCode, String expressNo) {
        String code = codeOf(nameOrCode);
        if (StrUtil.isNotBlank(code)) {
            return code;
        }
        code = autoCodeOf(expressNo);
        if (StrUtil.isBlank(code)) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "无法识别的快递单号, 请手动选择快递公司");
        }
        return code;
    }

    /**
     * 查询快递轨迹
     *
     * <p>轨迹按 (快递公司编码 + 单号) 短缓存, 命中缓存不再调三方。三方暂无轨迹数据时
     * 返回节点为空的轨迹对象 (刚发货未揽收属正常场景), 不返回 null</p>
     *
     * @param nameOrCode 快递公司名称或编码, 可为空 (为空时按单号识别)
     * @param expressNo  快递单号, 不可为空
     * @return 轨迹
     */
    public static LogisticsTrack queryTrack(String nameOrCode, String expressNo) {
        if (StrUtil.isBlank(expressNo)) {
            throw new PlatformException(BaseErrorCode.PARAM, "快递单号不能为空");
        }
        String code = resolveCode(nameOrCode, expressNo);
        String cacheKey = RedisEnum.Key.KUAIDI100_TRACK.getCode(code, expressNo);
        Kuaidi100Res.QueryRes res = RedisUtil.get(cacheKey);
        if (res == null) {
            res = Kuaidi100Method.queryTrack(code, expressNo);
            if (res != null) {
                RedisUtil.set(cacheKey, res, LogisticsConfig.trackCacheSeconds);
            }
        }
        return toTrack(code, expressNo, res);
    }

    /**
     * 三方响应转轨迹
     *
     * @param code      快递公司编码
     * @param expressNo 快递单号
     * @param res       三方响应, 可为空
     * @return 轨迹
     */
    private static LogisticsTrack toTrack(String code, String expressNo, Kuaidi100Res.QueryRes res) {
        LogisticsTrack track = new LogisticsTrack();
        track.setExpressCompanyCode(code);
        track.setExpressCompanyName(companyName(code));
        track.setExpressNo(expressNo);
        if (res == null) {
            track.setSigned(false);
            track.setNodes(Collections.emptyList());
            return track;
        }
        Integer state = NumberUtil.isInteger(res.getState()) ? Integer.valueOf(res.getState()) : null;
        track.setState(state);
        track.setStateText(stateText(state, res.getState()));
        track.setSigned("1".equals(res.getIscheck()));
        if (CollUtil.isEmpty(res.getData())) {
            track.setNodes(Collections.emptyList());
            return track;
        }
        List<LogisticsTrack.Node> nodes = new ArrayList<>(res.getData().size());
        for (Kuaidi100Res.Trace trace : res.getData()) {
            LogisticsTrack.Node node = new LogisticsTrack.Node();
            node.setTime(StrUtil.isNotBlank(trace.getFtime()) ? trace.getFtime() : trace.getTime());
            node.setStatus(trace.getStatus());
            node.setContent(trace.getContext());
            node.setLocation(trace.getLocation());
            nodes.add(node);
        }
        track.setNodes(nodes);
        return track;
    }

    /**
     * 物流状态码转文案
     *
     * @param state 状态码, 可为空
     * @param raw   三方原始状态串
     * @return 状态文案, 状态码超出已知范围时回退原始串
     */
    private static String stateText(Integer state, String raw) {
        if (state == null) {
            return raw;
        }
        return switch (state) {
            case 0 -> "在途";
            case 1 -> "已揽收";
            case 2 -> "疑难";
            case 3 -> "已签收";
            case 4 -> "退签";
            case 5 -> "同城派送中";
            case 6 -> "退回";
            case 7 -> "转单";
            default -> raw;
        };
    }

    /**
     * 加载公司目录
     *
     * @return 公司目录
     */
    private static Directory directory() {
        Directory local = directory;
        if (local != null) {
            return local;
        }
        synchronized (LogisticsMethod.class) {
            if (directory == null) {
                directory = loadDirectory();
            }
            return directory;
        }
    }

    /**
     * 读取 classpath 编码表
     *
     * <p>首行为表头 (带 BOM) 直接跳过; 保持文件顺序入 {@link LinkedHashMap},
     * 使国内主流快递排在列表最前</p>
     *
     * @return 公司目录
     */
    private static Directory loadDirectory() {
        String content;
        try {
            content = ResourceUtil.readUtf8Str(COMPANY_CSV);
        } catch (Exception e) {
            log.error("加载快递公司编码表失败 file={}", COMPANY_CSV, e);
            throw new PlatformException(BaseErrorCode.CUSTOM, "加载快递公司列表失败");
        }
        Map<String, LogisticsCompany> byCode = new LinkedHashMap<>();
        Map<String, String> nameToCode = new LinkedHashMap<>();
        String[] lines = content.split("\\r?\\n");
        for (int i = 1; i < lines.length; i++) {
            String line = StrUtil.trim(lines[i]);
            if (StrUtil.isBlank(line)) {
                continue;
            }
            String[] cols = line.split(",");
            if (cols.length < 2) {
                continue;
            }
            String name = StrUtil.trim(cols[0]);
            String code = StrUtil.trim(cols[1]);
            if (StrUtil.hasBlank(name, code)) {
                continue;
            }
            byCode.putIfAbsent(code, new LogisticsCompany(name, code));
            nameToCode.putIfAbsent(name, code);
        }
        log.info("快递公司编码表加载完成 count={}", byCode.size());
        return new Directory(byCode, nameToCode);
    }

    /**
     * 公司目录
     *
     * @param byCode     编码到公司, 保持编码表顺序
     * @param nameToCode 名称到编码, 保持编码表顺序
     */
    private record Directory(Map<String, LogisticsCompany> byCode, Map<String, String> nameToCode) {
    }
}
