package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.action.cmd.IndustryCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.domain.brand.service.IndustryDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;
import com.newzkl.platform.base.common.ddd.model.req.IdListCommand;
import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ScmResult<Long> industryCreate(@Validated @RequestBody IndustryReq industryReq) {
        return ScmResult.success(industryDomain.industrySave(industryReq));
    }

    /**
     * 删除行业。
     *
     * @param idListObj ID 列表
     * @return 成功结果
     */
    @PostMapping("deleteIndustry")
    public ScmResult<Void> industryDelete(@RequestBody IdListCommand idListObj) {
        industryDomain.industryDelete(idListObj.getIdList());
        return ScmResult.success();
    }

    /**
     * 修改行业。
     *
     * @param industryReq 行业请求
     * @return 成功结果
     */
    @PostMapping("updateIndustry")
    public ScmResult<Void> industryUpdate(@RequestBody IndustryReq industryReq) {
        industryDomain.industrySave(industryReq);
        return ScmResult.success();
    }

    /**
     * 行业详情。
     *
     * @param id 行业 ID
     * @return 行业 VO
     */
    @GetMapping("getIndustry")
    public ScmResult<IndustryVO> industryVO(@RequestParam("id") Long id) {
        return ScmResult.success(industryDomain.industry(id));
    }

    /**
     * 行业分页。
     *
     * @param industryQuery 分页查询
     * @return 行业分页
     */
    @PostMapping("pageIndustry")
    public ScmResult<Page<IndustryVO>> industryPage(@RequestBody IndustryPageQuery industryQuery) {
        return ScmResult.success(industryDomain.industryPage(industryQuery));
    }

    /**
     * 行业绑定分类。
     *
     * @param bindCategory 绑定命令
     * @return 成功结果
     */
    @PostMapping("bindCategory")
    public ScmResult<Void> bindCategory(@Validated @RequestBody IndustryCmd.BindCategory bindCategory) {
        spuCategoryService.bindIndustry(bindCategory.getIndustryId(), bindCategory.getCategoryId(),
                BooleanUtil.isTrue(bindCategory.getIsBind()));
        return ScmResult.success();
    }
}
