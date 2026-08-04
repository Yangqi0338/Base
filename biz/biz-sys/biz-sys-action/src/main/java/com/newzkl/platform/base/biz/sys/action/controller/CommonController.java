package com.newzkl.platform.base.biz.sys.action.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.action.cmd.SysCmd;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.LogisticsApi;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.LogisticsQueryReq;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.OcrApi;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.OssTokenApi;
import com.newzkl.platform.base.biz.sys.domain.service.AppVersionDomain;
import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.model.appversion.query.AppVersionQuery;
import com.newzkl.platform.base.biz.sys.model.appversion.vo.AppVersionVO;
import com.newzkl.platform.base.biz.sys.model.region.req.RegionReq;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 平台-公共控制器
 *
 * <p>迁移说明: 源 {@code CommonController} 共 12 个端点, 已迁入 10 个。仍未迁部分及原因:</p>
 * <ul>
 *   <li>{@code /generateQrCode} — 依赖 zxing (hutool QrCodeUtil 的可选依赖), Base 未引入。</li>
 *   <li>{@code /getRegionByCode} — 源已标 {@code @Deprecated} (前端零引用), 按规则不迁。</li>
 *   <li>{@code /businessIdentify} — 依赖华为云 OCR SDK 之外还需 {@code RecognizeBusinessLicenseRes}
 *       出参 VO + "按营业执照地址反查区域编码"逻辑, 待后续单端点补迁。</li>
 * </ul>
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
 *   <li>{@code /queryLogistics} — 裸 HTTP 下沉至 {@code infrastructure/gateway},
 *       controller 经 {@code LogisticsApi} 出站端口调用; appcode/host/path 外提配置,
 *       默认值与旧硬编码一致。</li>
 * </ul>
 *
 * <p>{@code exportExcelError} 已于本轮补迁: 不复用 Base 的
 * {@code EasyExcelErrorVO.ErrorLineVO(line:String, errorMsg:String)} (字段名与类型均不同),
 * 改在 action 层定义与旧 {@code ExcelErrorVO.Item(line:Integer, msg:String)} 逐字同构的
 * {@code SysCmd.ExcelErrorItem}, 前端契约不变。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/common")
@RequiredArgsConstructor
public class CommonController {

    private final RegionDomain regionDomain;
    private final AppVersionDomain appVersionDomain;
    private final OssTokenApi ossTokenApi;
    private final LogisticsApi logisticsApi;
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
     * <p>响应契约与旧一致: {@code data} 为三方返回的原始报文串, 由前端自行解析</p>
     *
     * @param req 物流查询入参
     * @return 三方原始报文串
     */
    @PostMapping("/queryLogistics")
    public PlatformResult<String> queryLogistics(@RequestBody SysCmd.DeliverQueryReq req) {
        LogisticsQueryReq queryReq = LogisticsQueryReq.builder()
                .type(req.getType())
                .number(req.getNumber())
                .mobile(req.getMobile())
                .build();
        return PlatformResult.success(logisticsApi.queryLogistics(queryReq));
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
    public PlatformResult<Object> ocrIdentify(@RequestBody SysCmd.StringObj stringObj) {
        return PlatformResult.success(ocrApi.recognizeIdCard(stringObj.getString()));
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
     * Excel 异常信息导出
     *
     * <p>纯出参转换端点: 前端把导入时收到的异常明细回传, 服务端原样写成 xlsx 下载,
     * 无落库、无领域逻辑, 故留在 action 层。</p>
     *
     * @param response   servlet 响应, 直接写出 xlsx 字节流
     * @param excelError 异常明细行列表
     * @throws IOException 写出响应流失败时抛出
     */
    @PostMapping("exportExcelError")
    public void exportExcelError(HttpServletResponse response,
                                 @RequestBody List<SysCmd.ExcelErrorItem> excelError) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("Excel导入异常记录", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SysCmd.ExcelErrorItem.class).sheet("模板").doWrite(excelError);
    }
}
