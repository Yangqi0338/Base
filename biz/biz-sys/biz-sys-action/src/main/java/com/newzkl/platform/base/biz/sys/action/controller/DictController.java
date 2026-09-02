package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.DictDomain;
import com.newzkl.platform.base.biz.sys.domain.service.DictItemDomain;
import com.newzkl.platform.base.biz.sys.model.dict.req.DictReq;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import com.newzkl.platform.base.biz.sys.model.dictitem.req.DictItemReq;
import com.newzkl.platform.base.biz.sys.model.dictitem.res.DictItemRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@FuncPermission("字典管理")
public class DictController {

    private final DictDomain dictDomain;
    private final DictItemDomain dictItemDomain;

    /**
     * 字典保存
     *
     * @param dictReq 字典请求, id 必填
     * @return 空结果
     */
    @PostMapping("dictSave")
    @FuncPermission("字典保存")
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

    /**
     * 字典条目保存
     *
     * @param req 条目请求 (id 传则更新, 否则新增)
     * @return 条目 id
     */
    @PostMapping("dictItemSave")
    @FuncPermission("字典条目保存")
    @Deprecated
    public PlatformResult<Long> dictItemSave(@Validated({UpdateCommand.class, Default.class}) @RequestBody DictItemReq req) {
        return PlatformResult.success(dictItemDomain.itemSave(req));
    }

    /**
     * 字典条目列表 (按父字典 id)
     *
     * @param dictId 父字典 id
     * @return 条目列表
     */
    @GetMapping("dictItemList")
    @Deprecated
    public PlatformResult<List<DictItemRes>> dictItemList(@RequestParam("dictId") Long dictId) {
        return PlatformResult.success(dictItemDomain.itemList(dictId));
    }

    /**
     * 字典条目删除
     *
     * @param idList 条目 id 列表
     * @return 空结果
     */
    @PostMapping("dictItemDelete")
    @FuncPermission("字典条目删除")
    @Deprecated
    public PlatformResult<Void> dictItemDelete(@Validated @RequestBody IdCommand idList) {
        dictItemDomain.itemDelete(idList.getIdList());
        return PlatformResult.success();
    }
}
