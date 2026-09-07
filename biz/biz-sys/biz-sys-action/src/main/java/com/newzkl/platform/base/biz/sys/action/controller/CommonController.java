package com.newzkl.platform.base.biz.sys.action.controller;

import cn.hutool.core.img.ColorUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeBusinessLicenseResponse;
import com.huaweicloud.sdk.ocr.v1.model.RecognizeIdCardResponse;
import com.newzkl.platform.base.biz.sys.action.cmd.SysCmd;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.OcrApi;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.OssTokenApi;
import com.newzkl.platform.base.biz.sys.domain.service.AppVersionDomain;
import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;
import com.newzkl.platform.base.biz.sys.model.region.req.RegionReq;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.core.logistics.LogisticsCompany;
import com.newzkl.platform.base.common.core.logistics.LogisticsMethod;
import com.newzkl.platform.base.common.core.logistics.LogisticsTrack;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * 平台-公共控制器
 *
 * <p>迁移说明: 源 {@code CommonController} 共 12 个端点, 已迁入 11 个。仍未迁部分及原因:</p>
 * <ul>
 *   <li>{@code /generateQrCode} — 依赖 zxing (hutool QrCodeUtil 的可选依赖), Base 未引入。</li>
 *   <li>{@code /getRegionByCode} — 源已标 {@code @Deprecated} (前端零引用), 按规则不迁。</li>
 * </ul>
 *
 * <p>2026-08-06 补迁 {@code /businessIdentify} (华为云 OCR 营业执照识别 + 地址反查区域码):
 * 复用既有 {@code OcrClient} bean, SDK 调用落 {@code HuaweiOcrGateway#recognizeBusinessLicense},
 * controller 经 {@code OcrApi} 出站端口调用, 未改任何 pom。地址反查区域码逻辑落
 * {@code RegionDomainImpl#businessIdentify} (复用 {@code Area#getCodeByName} + areaMap 剥名),
 * 出参不引入源 {@code RecognizeBusinessLicenseRes} (其 extends SDK 类型, 会漏 SDK 进 model),
 * 改由 gateway 转通用 JSON 结构, 序列化后契约 {@code {result:{...},areaCode:[]}} 与源逐字一致。</p>
 *
 * <p>2026-08-04 补迁 {@code /ocrIdentify} (华为云 OCR 身份证识别, 用户授权引入
 * {@code com.huaweicloud.sdk:huaweicloud-sdk-ocr:3.1.60}): SDK 调用下沉
 * {@code infrastructure/gateway} 的 {@code HuaweiOcrGateway}, controller 经
 * {@code OcrApi} 出站端口调用; AK/SK 由源硬编码改 {@code huawei.ocr} 配置下发。</p>
 *
 * <p>本轮补迁 3 个端点, 均未改动任何 pom:</p>
 * <ul>
 *   <li>{@code /pictureUpToken} — 七牛 SDK 在根 pom 仅有 dependencyManagement 版本声明,
 *       未被任何模块引入; 改为在 {@code infrastructure/gateway} 按七牛官方算法自行签名,
 *       不引 SDK。配置类 {@code QiniuProperties} 前缀 {@code qiniuyun.config} 与旧逐字一致。</li>
 *   <li>{@code /getBusinessRegion} — 端点与本地分支 (父编码过滤/名称匹配打标/剔除打标/平展)
 *       全部落地; 仅 {@code operatorFilter=1} 的运营商区域打标依赖跨域 RPC,
 *       在 {@code RegionDomainImpl} 内保留注释 + {@code TODO[cross-service]} 未硬接。
 *       该入参传 1 时当前返回未按运营商打标的区域树, 前端注意。</li>
 *   <li>{@code /queryLogistics} — 三方由阿里云极速快递换成快递100, 调用收敛至
 *       {@code core-logistics} 的 {@code LogisticsMethod} 门面 (签名/缓存/编码表都在门面内),
 *       biz 侧不再自建出站端口。出参由原始报文串改结构化 {@code LogisticsTrack},
 *       详见端点上的契约变更说明。</li>
 * </ul>
 *
 * <p>{@code exportExcelError} (Excel 导入异常明细回传下载) 随本轮导出能力整体移除, 不再暴露</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/common")
@RequiredArgsConstructor
@Validated
@FuncPermission("平台公共")
public class CommonController {

    private final RegionDomain regionDomain;
    private final AppVersionDomain appVersionDomain;
    private final OssTokenApi ossTokenApi;
    private final OcrApi ocrApi;

    /**
     * 枚举消息
     *
     * <p>源实现即为空返回 (仅靠返回泛型向前端暴露枚举结构, 无运行时数据),
     * 故此处保持 {@code data} 为空, 响应体与旧契约一致。</p>
     *
     * @return 空结果
     */
    @GetMapping("/enumInfo")
    public PlatformResult<Void> enumInfo() {
        return PlatformResult.success();
    }

    /**
     * 获取地址信息
     *
     * @return 区域树 JSON 串
     */
    @GetMapping("/getRegion")
    public PlatformResult<String> getRegion() {
        return PlatformResult.success(regionDomain.getRegion());
    }

    /**
     * 获取地址信息 (业务版)
     *
     * <p>能力缺失提示: 入参 {@code operatorFilter=1} 表示"按运营商已开通区域打标"
     * ({@code flag=1}), 该能力依赖运营商域 RPC, Base 当前无对应出站端口, 未实现。
     * 传 1 时本端点返回的是<b>未按运营商打标</b>的区域树 (不抛错, 仅打 warn 日志);
     * 若同时传了 {@code flagRemove=1}, 则因为无标记可剔, 返回结果为完整区域树。
     * 其余分支 (父编码过滤 / {@code matchStrList} 名称匹配打标 / {@code flagRemove} 剔除 /
     * {@code flatten} 平展) 与旧实现等价。详见 {@code RegionDomainImpl} 内
     * {@code TODO[cross-service]}</p>
     *
     * @param req 区域查询入参
     * @return 区域列表
     */
    @PostMapping("/getBusinessRegion")
    public PlatformResult<List<Area>> getBusinessRegion(@RequestBody RegionReq req) {
        return PlatformResult.success(regionDomain.getBusinessRegion(req));
    }

    /**
     * 获取七牛云上传凭证
     *
     * <p>7 个前端仓均在用的高频端点。响应契约与旧一致: {@code data} 为上传凭证串本身</p>
     *
     * <p>实现说明: 旧实现调 qiniu-java-sdk 的 {@code Auth#uploadToken}, 该 SDK 未被 Base
     * 任何模块引入 (根 pom 仅有版本声明); 本次不改 pom, 改由
     * {@code QiniuOssTokenGateway} 按七牛官方算法自行签名, 输出格式完全一致</p>
     *
     * @return 七牛上传凭证
     */
    @GetMapping("/pictureUpToken")
    public PlatformResult<Object> pictureUpToken() {
        return PlatformResult.success(ossTokenApi.uploadToken());
    }

    /**
     * 查询物流信息
     *
     * <p>⚠️ 出参契约变更: 旧实现直返三方 (阿里云极速快递) 原始报文串由前端自行解析,
     * 本轮三方换成快递100, 原始报文结构已完全不同, 旧解析代码无论如何都失效,
     * 故索性改返结构化 {@code LogisticsTrack} (公司/单号/状态/节点列表)。
     * <b>前端 3 处调用 {@code getDeliveryInfoReq} 需改按 {@code data.nodes} 渲染</b>:
     * {@code platform-admin/src/http/common.ts}、{@code platform-admin/src/http/order.ts}、
     * {@code gys-admin/src/http/order.ts}</p>
     *
     * <p>入参 {@code type} (快递公司编码或中文名) 可空, 空时由快递100 按单号自动识别;
     * {@code mobile} 旧三方用于顺丰等隐私单号校验, 快递100 免费版不需要, 未使用</p>
     *
     * <p>出参公司与单号字段与发货单同口径: {@code expressCompanyName} / {@code expressCompanyCode} / {@code expressNo}</p>
     *
     * @param req 物流查询入参
     * @return 物流轨迹
     */
    @PostMapping("/queryLogistics")
    public PlatformResult<LogisticsTrack> queryLogistics(@RequestBody SysCmd.DeliverQueryReq req) {
        return PlatformResult.success(LogisticsMethod.queryTrack(req.getType(), req.getNumber()));
    }

    /**
     * 查询快递公司列表
     *
     * <p>数据源为快递100 官方编码表 (classpath {@code kuaidi100com.csv}, 1421 家),
     * 进程内存懒加载不落库; 顺序与编码表一致, 国内主流快递在前。
     * 全量条数偏多故分页返回, 前端下拉框可直接搜关键字</p>
     *
     * <p>出参字段 {@code expressCompanyName} / {@code expressCompanyCode} 与发货接口入参同名:
     * 下拉选中后原样回传 {@code deliverCreate} / {@code deliverEdit} 即可, 前端无需字段映射。
     * 发货侧会按本编码表校验公司名, 故发货表单的公司应从本接口取, 不要让用户自由输入</p>
     *
     * @param keyword  名称或编码关键字, 可空, 空则返回全量
     * @param pageNo   页码, 从 1 开始
     * @param pageSize 每页条数
     * @return 快递公司分页
     */
    @GetMapping("/logisticsCompanies")
    public PlatformResult<Page<LogisticsCompany>> getLogisticsCompanies(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "20") long pageSize) {
        List<LogisticsCompany> all = LogisticsMethod.companies(keyword);
        Page<LogisticsCompany> page = new Page<>(pageNo, pageSize, all.size());
        int from = (int) ((pageNo - 1) * pageSize);
        if (from < 0 || from >= all.size()) {
            page.setRecords(Collections.emptyList());
            return PlatformResult.success(page);
        }
        int to = (int) Math.min(from + pageSize, all.size());
        page.setRecords(all.subList(from, to));
        return PlatformResult.success(page);
    }

    /**
     * 身份证识别
     *
     * <p>gys-admin 3 处在用。经华为云 OCR SDK 识别身份证图片, SDK 调用下沉至
     * {@code infrastructure/gateway} 的 {@code HuaweiOcrGateway}, controller 只经
     * {@code OcrApi} 出站端口调用。</p>
     *
     * <p>契约对齐: 入参沿用旧 {@code StringObj#string} (身份证图片 URL);
     * 出参为三方识别结果 (源直返 SDK {@code RecognizeIdCardResponse},
     * 序列化后为 {@code {result:{...}}}, 本实现经 gateway 转通用结构后形状一致)。
     * 识别失败时返回 {@code data} 为 {@code null} (与源逐字一致)。</p>
     *
     * <p>安全提示: 华为云 AK/SK 已由源硬编码改为 {@code huawei.ocr} 配置项下发,
     * 旧硬编码凭证已泄漏在 git 历史, 上线前应轮换。</p>
     *
     * @param stringObj 身份证图片 URL 载体
     * @return 三方识别结果
     */
    @PostMapping("/ocrIdentify")
    public PlatformResult<RecognizeIdCardResponse> ocrIdentify(@RequestBody SysCmd.StringObj stringObj) {
        return PlatformResult.success(ocrApi.recognizeIdCard(stringObj.getString()));
    }

    /**
     * 营业执照识别
     *
     * <p>经华为云 OCR SDK 识别营业执照图片, SDK 调用下沉 {@code infrastructure/gateway} 的
     * {@code HuaweiOcrGateway}, controller 只经 {@code OcrApi} 出站端口调用; 识别结果里的
     * 注册地址由 {@code RegionDomain} 反查行政区域编码, 剥去命中的区域名后回填, 顶层追加
     * {@code areaCode} 编码列表</p>
     *
     * <p>契约对齐: 入参沿用旧 {@code StringObj#string} (营业执照图片 URL); 出参形状与源
     * {@code {result:{...}, areaCode:[]}} 一致 (源出参 VO {@code RecognizeBusinessLicenseRes}
     * extends SDK 响应, 会把 SDK 类型漏进 model, 本实现改用通用结构承载, 序列化后形状不变)。
     * 识别失败返回 {@code data} 为 {@code null} (与源逐字一致)</p>
     *
     * <p>安全提示: 华为云 AK/SK 已由源硬编码改为 {@code huawei.ocr} 配置项下发,
     * 旧硬编码凭证已泄漏在 git 历史, 上线前应轮换</p>
     *
     * @param stringObj 营业执照图片 URL 载体
     * @return 三方识别结果 (含反查区域编码)
     */
    @PostMapping("/businessIdentify")
    public PlatformResult<RecognizeBusinessLicenseRes> businessIdentify(@RequestBody SysCmd.StringObj stringObj) {
        Pair<RecognizeBusinessLicenseResponse, List<Integer>> pair = regionDomain.businessIdentify(stringObj.getString());
        RecognizeBusinessLicenseResponse key = pair.getKey();
        if (key == null) {
            return PlatformResult.success();
        }
        RecognizeBusinessLicenseRes res = TransferUtils.transfer(key, RecognizeBusinessLicenseRes.class);
        res.setAreaCode(pair.getValue());
        return PlatformResult.success(res);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RecognizeBusinessLicenseRes extends RecognizeBusinessLicenseResponse {

        private List<Integer> areaCode;

    }

    /**
     * 查询 app 版本列表
     *
     * <p>TODO[auth-defer]: 旧实现带 {@code @Limit(app_manage, get)}, 鉴权注解整体延后,
     * 暂由网关侧兜住。</p>
     *
     * <p>⚠️ 出参契约(2026-07-30 更正): 原注「现直返 {@code List<AppVersionVO>}」<b>已作废</b> ——
     * 那样把 {@code total} 丢了, 与 {@code rules/Architecture.md}
     * 「{@code PageInfo}→{@code IPage/Page} 直返」矛盾。现返 MyBatis-Plus {@code Page}</p>
     *
     * <p>相对旧契约: 旧是 PageHelper {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 现是 {@code Page}({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端(platform-admin)需把 {@code res.data.list} 改成 {@code res.data.records}</b></p>
     *
     * @param req 查询条件
     * @return app 版本分页
     */
    @PostMapping("/queryAppVersionList")
    public PlatformResult<Page<AppVersionVO>> queryAppVersionList(@RequestBody AppVersionQuery req) {
        return PlatformResult.success(appVersionDomain.queryAppVersionList(req));
    }

    /**
     * 保存 app 版本信息
     *
     * <p>TODO[auth-defer]: 旧实现带 {@code @Limit(app_manage, set)}, 鉴权注解整体延后,
     * 暂由网关侧兜住。</p>
     *
     * @param appVersion app 版本视图对象
     * @return 空结果
     */
    @PostMapping("/saveAppVersion")
    @FuncPermission("保存app版本")
    public PlatformResult<Void> saveAppVersion(@RequestBody AppVersionVO appVersion) {
        appVersionDomain.saveAppVersion(appVersion);
        return PlatformResult.success();
    }

    /**
     * 查询 app 最新版本
     *
     * <p>TODO[auth-defer]: 旧实现带 {@code @Limit(app_manage, get)}, 鉴权注解整体延后,
     * 暂由网关侧兜住。</p>
     *
     * @param appName app 名称
     * @return app 版本视图对象
     */
    @PostMapping("/queryAppNewVersion/{appName}")
    public PlatformResult<AppVersionVO> queryAppNewVersion(@PathVariable String appName) {
        return PlatformResult.success(appVersionDomain.queryAppNewVersion(appName));
    }

    /**
     * 生成二维码
     *
     * <p>渲染 600x600 无白边二维码 PNG, base64 编码后以 {@code text/plain} 直写响应体,
     * 由前端拿 base64 串自行渲染 (响应契约与源逐字一致)。</p>
     *
     * <p>实现说明: 沿用源 hutool {@code QrCodeUtil} (zxing 薄封装, 本轮经用户授权在
     * biz-sys-action pom 引入 {@code com.google.zxing:core+javase:3.5.3});
     * {@code foreColor} 可选, 传入时经 {@code ColorUtil#getColor} 解析前景色。</p>
     *
     * @param content   二维码内容, 不可为空
     * @param foreColor 前景色 (可选), 十六进制或颜色名, 由 hutool {@code ColorUtil} 解析
     * @param response  servlet 响应, 直接写出 base64 串
     * @throws IOException 写出响应流失败时抛出
     */
    @GetMapping("/generateQrCode")
    public void generateQrCode(@NotBlank(message = "内容不能为空") @RequestParam(name = "content") String content,
                               @RequestParam(name = "foreColor", required = false) String foreColor,
                               HttpServletResponse response) throws IOException {
        QrConfig config = new QrConfig(600, 600);
        config.setMargin(0);
        if (StrUtil.isNotBlank(foreColor)) {
            config.setForeColor(ColorUtil.getColor(foreColor));
        }
        try (PrintWriter out = response.getWriter()) {
            byte[] bytes = QrCodeUtil.generatePng(content, config);
            String base64Str = Base64.getEncoder().encodeToString(bytes);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            out.write(base64Str);
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("二维码生成失败：" + e.getMessage());
        }
    }
}
