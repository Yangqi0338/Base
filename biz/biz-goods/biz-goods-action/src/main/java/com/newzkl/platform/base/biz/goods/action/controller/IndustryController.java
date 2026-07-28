package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.action.cmd.IndustryCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.domain.brand.service.IndustryDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.BooleanUtil;

/**
 * 商品-行业控制器。
 *
 * @author fang
 */
@RestController
@RequestMapping("/goods/catalog/industry")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryDomain industryDomain;
    private final SpuCategoryService spuCategoryService;

    /**
     * 创建行业。
     *
     * @param industryReq 行业请求
     * @return 行业 ID
     */
    @PostMapping("createIndustry")
    public PlatformResult<Long> industryCreate(@Validated @RequestBody IndustryReq industryReq) {
        return PlatformResult.success(industryDomain.industrySave(industryReq));
    }

    /**
     * 删除行业。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("deleteIndustry")
    public PlatformResult<Void> industryDelete(@RequestBody IdListCommand idListObj) {
        industryDomain.industryDelete(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改行业。
     *
     * @param industryReq 行业请求
     * @return 成功结果
     */
    @PostMapping("updateIndustry")
    public PlatformResult<Void> industryUpdate(@RequestBody IndustryReq industryReq) {
        industryDomain.industrySave(industryReq);
        return PlatformResult.success();
    }

    /**
     * 行业分页。
     *
     * @param industryQuery 分页查询
     * @return 行业分页
     */
    @PostMapping("pageIndustry")
    public PlatformResult<Page<IndustryVO>> industryPage(@RequestBody IndustryPageQuery industryQuery) {
        return PlatformResult.success(industryDomain.industryPage(industryQuery));
    }

    /**
     * 行业绑定分类。
     *
     * @param bindCategory 绑定命令
     * @return 成功结果
     */
    @PostMapping("bindCategory")
    public PlatformResult<Void> bindCategory(@Validated @RequestBody IndustryCmd.BindCategory bindCategory) {
        spuCategoryService.bindIndustry(bindCategory.getIndustryId(), bindCategory.getCategoryId(),
                BooleanUtil.isTrue(bindCategory.getIsBind()));
        return PlatformResult.success();
    }
}
