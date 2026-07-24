package com.newzkl.platform.base.biz.sys.action.controller;

import com.newzkl.platform.base.biz.sys.domain.service.DictDomain;
import com.newzkl.platform.base.biz.sys.model.dict.query.DictQuery;
import com.newzkl.platform.base.biz.sys.model.dict.req.DictReq;
import com.newzkl.platform.base.biz.sys.model.dict.res.DictRes;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
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
 * 平台字典控制器。
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictDomain dictDomain;

    /**
     * 字典创建/更新。
     *
     * @param req 字典请求
     * @return 字典 id
     */
    @PostMapping("/dictSave")
    public ScmResult<Long> dictSave(@Validated @RequestBody DictReq req) {
        return ScmResult.success(dictDomain.dictSave(req));
    }

    /**
     * 字典详情。
     *
     * @param id 字典 id
     * @return 字典视图对象
     */
    @GetMapping("/dict")
    public ScmResult<DictRes> dict(@RequestParam("id") Long id) {
        return ScmResult.success(dictDomain.dictVO(id));
    }

    /**
     * 字典列表。
     *
     * @param query 字典查询
     * @return 字典列表
     */
    @PostMapping("/dictList")
    public ScmResult<List<DictRes>> dictList(@Validated @RequestBody DictQuery query) {
        return ScmResult.success(dictDomain.dictList(query));
    }

    /**
     * 取指定 key 的下一个序列值。
     *
     * @param id 字典 key (id)
     * @return 下一个序列值
     */
    @GetMapping("/nextCode")
    public ScmResult<String> nextCode(@RequestParam("id") Long id) {
        return ScmResult.success(dictDomain.nextCode(id));
    }
}
