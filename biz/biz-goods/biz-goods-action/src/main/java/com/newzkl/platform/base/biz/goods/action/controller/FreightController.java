package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.freight.service.FreightDomain;
import com.newzkl.platform.base.biz.goods.model.goods.entity.freight.FreightTemplate;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.freight.FreightTemplateReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.req.IdCommand;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-运费模板控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/freight")
@RequiredArgsConstructor
@FuncPermission("运费模板管理")
public class FreightController {

    /**
     * 系统内置运费模板主键, 仅平台可改删
     */
    private static final long SYSTEM_TEMPLATE_ID = 100000000000001L;

    private final FreightDomain freightDomain;

    /**
     * 创建运费模板
     *
     * @param freightTemplateReq 运费模板请求
     * @return 运费模板主键
     */
    @PostMapping("freightTemplateCreate")
    @FuncPermission("创建运费模板")
    public PlatformResult<Long> freightTemplateCreate(@Validated @RequestBody FreightTemplateReq freightTemplateReq) {
        if (freightTemplateReq.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        AccountEnum.Identity identity = SecurityUtils.getIdentity();
        if (AccountEnum.Identity.SUPPLIER == identity
                || AccountEnum.Identity.CHANNEL == identity) {
            freightTemplateReq.setAccountId(SecurityUtils.getAccountId());
        }
        return PlatformResult.success(freightDomain.freightTemplateSave(freightTemplateReq));
    }

    /**
     * 删除运费模板
     *
     * <p>非平台角色不允许删除系统运费模板。</p>
     *
     * @param idList 主键列表命令
     * @return 空结果
     */
    @PostMapping("freightTemplateDelete")
    @FuncPermission("删除运费模板")
    public PlatformResult<Void> freightTemplateDelete(@RequestBody IdCommand idList) {
        if (AccountEnum.Identity.PLATFORM != SecurityUtils.getIdentity()
                && idList.getIdList().contains(SYSTEM_TEMPLATE_ID)) {
            ThrowsException.exception(BaseErrorCode.CUSTOM, "无法删除系统运费模板");
        }
        freightDomain.freightTemplateDelete(idList.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改运费模板
     *
     * <p>非平台角色不允许修改系统运费模板。</p>
     *
     * @param freightTemplateReq 运费模板请求
     * @return 空结果
     */
    @PostMapping("freightTemplateUpdate")
    @FuncPermission("修改运费模板")
    public PlatformResult<Void> freightTemplateUpdate(@Validated @RequestBody FreightTemplateReq freightTemplateReq) {
        if (freightTemplateReq.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        if (AccountEnum.Identity.PLATFORM != SecurityUtils.getIdentity()
                && freightTemplateReq.getId() == SYSTEM_TEMPLATE_ID) {
            ThrowsException.exception(BaseErrorCode.CUSTOM, "无法修改系统运费模板");
        }
        freightDomain.freightTemplateEdit(freightTemplateReq);
        return PlatformResult.success();
    }

    /**
     * 运费模板详情
     *
     * @param id 运费模板主键
     * @return 运费模板详情
     */
    @GetMapping("freightTemplate")
    public PlatformResult<FreightTemplate> freightTemplate(@RequestParam("id") Long id) {
        return PlatformResult.success(freightDomain.freightTemplate(id));
    }

    /**
     * 运费模板分页
     *
     * <p>供应商与渠道商仅可见自身及平台模板。</p>
     *
     * @param freightTemplateQuery 运费模板查询条件
     * @return 运费模板分页
     */
    @PostMapping("freightTemplatePage")
    public PlatformResult<Page<FreightTemplateVO>> freightTemplatePageVOList(@RequestBody FreightTemplateQuery freightTemplateQuery) {
        AccountEnum.Identity identity = SecurityUtils.getIdentity();
        freightTemplateQuery.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(freightDomain.freightTemplatePage(freightTemplateQuery));
    }
}
