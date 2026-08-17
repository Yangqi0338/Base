package com.newzkl.platform.base.biz.sys.action.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.sys.domain.service.DictDomain;
import com.newzkl.platform.base.biz.sys.model.config.enums.DictEnum;
import com.newzkl.platform.base.biz.sys.model.config.vo.AppConfigVO;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.facade.OrderConfigVO;
import com.newzkl.platform.base.biz.sys.model.dict.req.DictReq;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 平台-配置控制器
 *
 * <p>配置项本体存于字典表, 键见 {@code DictEnum.Key}。</p>
 *
 * <p><b>Bean 名显式指定</b>: Base 的 biz-finance 已有同简名 {@code ConfigController}
 * (映射 {@code /config}), 两者若都用默认 bean 名会触发
 * {@code ConflictingBeanDefinitionException}, 故此处显式命名 {@code sysConfigController}。
 * 对外 URL 仍为 {@code /admin/config}, 与源逐字一致。</p>
 *
 * <p>TODO[auth-defer]: 源各端点带 {@code @Limit(order_goods_set / app / app_order /
 * channel_set, get|set)}, Base 的鉴权切面尚未迁入, 注解整体延后, 暂由网关侧兜住。</p>
 *
 * <p>JSON 处理: 源用 fastjson {@code JSONObject}, 此处改用已在用的 hutool
 * {@code JSONUtil}, 不新增依赖, 序列化结果同构。</p>
 *
 * @author KC
 */
@RestController("sysConfigController")
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class ConfigController {

    private final DictDomain dictDomain;

    /**
     * 订单配置查询
     *
     * @return 订单配置, 字典未配置时返回全空对象
     */
    @PostMapping("/orderGet")
    public PlatformResult<OrderConfigVO> orderGet() {
        DictRes dictRes = dictDomain.dictVOByCode(DictEnum.Key.ORDER_CONFIG.getCode());
        if (dictRes == null || StrUtil.isEmpty(dictRes.getValue())) {
            return PlatformResult.success(new OrderConfigVO());
        }
        return PlatformResult.success(JSONUtil.toBean(dictRes.getValue(), OrderConfigVO.class));
    }

    /**
     * 订单配置修改
     *
     * @param orderConfigVO 订单配置
     * @return 空结果
     */
    @PostMapping("/orderSet")
    public PlatformResult<Void> orderSet(@RequestBody OrderConfigVO orderConfigVO) {
        DictReq dictReq = new DictReq();
        dictReq.setCode(DictEnum.Key.ORDER_CONFIG.getCode());
        dictReq.setValue(JSONUtil.toJsonStr(orderConfigVO));
        dictDomain.dictSave(dictReq);
        return PlatformResult.success();
    }

    /**
     * 应用查询
     *
     * @return 应用配置列表, 字典未配置时返回空列表
     */
    @PostMapping("/appGet")
    public PlatformResult<List<AppConfigVO>> appGet() {
        DictRes dictRes = dictDomain.dictVOByCode(DictEnum.Key.APP_CONFIG.getCode());
        if (dictRes == null || StrUtil.isEmpty(dictRes.getValue())) {
            return PlatformResult.success(new ArrayList<>());
        }
        return PlatformResult.success(JSONUtil.toList(dictRes.getValue(), AppConfigVO.class));
    }

    /**
     * 应用修改
     *
     * <p>只允许改既有应用的名称/图片/价格, id 不存在直接报参数异常。</p>
     *
     * @param appConfigVO 应用配置, id 必填
     * @return 空结果
     */
    @PostMapping("/appSet")
    public PlatformResult<Void> appSet(@RequestBody AppConfigVO appConfigVO) {
        DictRes dictRes = dictDomain.dictVOByCode(DictEnum.Key.APP_CONFIG.getCode());
        ThrowsException.isNull(dictRes, BaseErrorCode.NODATA, "应用配置");

        List<AppConfigVO> appConfigList = JSONUtil.toList(dictRes.getValue(), AppConfigVO.class);
        Map<Long, AppConfigVO> appConfigMap = appConfigList.stream()
                .collect(Collectors.toMap(AppConfigVO::getId, Function.identity()));
        AppConfigVO oldAppConfigVO = appConfigMap.get(appConfigVO.getId());
        if (oldAppConfigVO == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "ID不存在");
        } else {
            oldAppConfigVO.setName(appConfigVO.getName());
            oldAppConfigVO.setImg(appConfigVO.getImg());
            oldAppConfigVO.setPrice(appConfigVO.getPrice());
        }

        DictReq dictReq = new DictReq();
        dictReq.setId(dictRes.getId());
        dictReq.setDesc(dictRes.getDesc());
        dictReq.setValue(JSONUtil.toJsonStr(appConfigList));
        dictDomain.dictSave(dictReq);
        return PlatformResult.success();
    }

    /**
     * 数智门店配置修改
     *
     * @param orderConfigVO 数智门店配置 (源参数名如此, 保留)
     * @return 空结果
     */
    @PostMapping("/channelConfigSet")
    public PlatformResult<Void> channelConfigSet(@RequestBody ChannelConfigVO orderConfigVO) {
        DictReq dictReq = new DictReq();
        dictReq.setCode(DictEnum.Key.CHANNEL_CONFIG.getCode());
        dictReq.setValue(JSONUtil.toJsonStr(orderConfigVO));
        dictDomain.dictSave(dictReq);
        return PlatformResult.success();
    }

    /**
     * 数智门店配置查询
     *
     * @return 数智门店配置, 字典未配置时返回全空对象
     */
    @PostMapping("/channelConfigGet")
    public PlatformResult<ChannelConfigVO> channelConfigGet() {
        DictRes dictRes = dictDomain.dictVOByCode(DictEnum.Key.CHANNEL_CONFIG.getCode());
        if (dictRes == null || StrUtil.isEmpty(dictRes.getValue())) {
            return PlatformResult.success(new ChannelConfigVO());
        }
        return PlatformResult.success(JSONUtil.toBean(dictRes.getValue(), ChannelConfigVO.class));
    }
}
