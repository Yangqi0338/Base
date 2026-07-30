package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.DictDomain;
import com.newzkl.platform.base.biz.sys.model.dict.req.DictReq;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台-字典控制器
 *
 * <p>迁移说明: 源 {@code DictController} 直连 {@code IDictRepository} + {@code DictAssembler}
 * 做 VO→领域对象转换, 违反 action 层不得触达持久化的红线; 此处统一走
 * {@code DictDomain}, 装配下沉至 domain 内部。入参 {@code DictVO} 拆分为
 * {@code DictReq}/{@code DictRes}, JSON 字段 (id/value/desc) 与旧契约一致。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictDomain dictDomain;

    /**
     * 字典保存
     *
     * @param dictReq 字典请求, id 必填
     * @return 空结果
     */
    @PostMapping("dictSave")
    public PlatformResult<Void> dictSave(@RequestBody DictReq dictReq) {
        if (dictReq.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        dictDomain.dictSave(dictReq);
        return PlatformResult.success();
    }

    /**
     * 字典详情
     *
     * <p>常用 key: 1001 对公账户配置, 1002 在线支付二维码配置, 1003 登录页设置。</p>
     *
     * @param id 字典 id
     * @return 字典详情
     */
    @GetMapping("dict")
    public PlatformResult<DictRes> dict(@RequestParam("id") Long id) {
        return PlatformResult.success(dictDomain.dictVO(id));
    }
}
