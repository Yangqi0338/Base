package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.freight.service.FreightDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.freight.FreightTemplateReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-运费控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/goods/freight")
@RequiredArgsConstructor
public class FreightController {

    private final FreightDomain freightDomain;

    /**
     * 创建运费模板。
     *
     * @param req 运费模板请求
     * @return 模板 ID
     */
    @PostMapping("freightTemplateCreate")
    public PlatformResult<Long> freightTemplateCreate(@Validated @RequestBody FreightTemplateReq req) {
        return PlatformResult.success(freightDomain.freightTemplateSave(req));
    }

    /**
     * 删除运费模板。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("freightTemplateDelete")
    public PlatformResult<Void> freightTemplateDelete(@RequestBody IdListCommand idListObj) {
        freightDomain.freightTemplateDelete(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改运费模板。
     *
     * @param req 运费模板请求
     * @return 成功结果
     */
    @PostMapping("freightTemplateUpdate")
    public PlatformResult<Void> freightTemplateUpdate(@Validated @RequestBody FreightTemplateReq req) {
        freightDomain.freightTemplateEdit(req);
        return PlatformResult.success();
    }

    /**
     * 运费模板分页。
     *
     * @param freightTemplateQuery 查询条件
     * @return 运费模板分页
     */
    @PostMapping("freightTemplatePage")
    public PlatformResult<Page<FreightTemplateVO>> freightTemplatePage(@RequestBody FreightTemplateQuery freightTemplateQuery) {
        return PlatformResult.success(freightDomain.freightTemplatePage(freightTemplateQuery));
    }
}
