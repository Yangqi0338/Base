package com.newzkl.platform.base.biz.sys.action.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.sys.domain.service.DictDomain;
import com.newzkl.platform.base.common.ddd.model.enums.sys.DictEnum;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.ddd.facade.OrderConfigVO;
import com.newzkl.platform.base.biz.sys.model.dict.req.DictReq;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@FuncPermission("平台配置")
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
    @FuncPermission("订单配置修改")
    public PlatformResult<Void> orderSet(@RequestBody OrderConfigVO orderConfigVO) {
        DictReq dictReq = new DictReq();
        dictReq.setCode(DictEnum.Key.ORDER_CONFIG.getCode());
        dictReq.setValue(JSONUtil.toJsonStr(orderConfigVO));
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
    @FuncPermission("数智门店配置修改")
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
