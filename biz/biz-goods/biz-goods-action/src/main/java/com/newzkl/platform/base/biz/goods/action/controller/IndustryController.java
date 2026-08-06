package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.action.cmd.CommonCmd;
import com.newzkl.platform.base.biz.goods.action.cmd.IndustryCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.domain.brand.service.IndustryDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-行业控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/catalog/industry")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryDomain industryDomain;
    private final SpuCategoryService spuCategoryService;

    /**
     * 分类绑定/解绑行业
     *
     * @param bindCmd 绑定命令 (行业主键 / 分类主键 / 是否绑定)
     * @return 空结果
     */
    @PostMapping("bindCategory")
    public PlatformResult<Void> bindCategory(@Validated @RequestBody IndustryCmd.BindCategory bindCmd) {
        spuCategoryService.bindIndustry(bindCmd.getIndustryId(), bindCmd.getCategoryId(),
                Boolean.TRUE.equals(bindCmd.getIsBind()));
        return PlatformResult.success();
    }

    /**
     * 创建行业
     *
     * @param industryReq 行业请求
     * @return 行业主键
     */
    @PostMapping("createIndustry")
    public PlatformResult<Long> industryCreate(@Validated @RequestBody IndustryReq industryReq) {
        if (industryReq.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(industryDomain.industrySave(industryReq));
    }

    /**
     * 删除行业
     *
     * @param idList 主键列表命令
     * @return 空结果
     */
    @PostMapping("deleteIndustry")
    public PlatformResult<Void> industryDelete(@RequestBody CommonCmd.IdList idList) {
        industryDomain.industryDelete(idList.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改行业
     *
     * @param industryReq 行业请求
     * @return 空结果
     */
    @PostMapping("updateIndustry")
    public PlatformResult<Void> industryUpdate(@RequestBody IndustryReq industryReq) {
        if (industryReq.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        industryDomain.industrySave(industryReq);
        return PlatformResult.success();
    }

    /**
     * 行业分页
     *
     * @param industryQuery 行业查询条件
     * @return 行业分页
     */
    @PostMapping("pageIndustry")
    public PlatformResult<Page<IndustryVO>> industryPage(@RequestBody IndustryPageQuery industryQuery) {
        return PlatformResult.success(industryDomain.industryPage(industryQuery));
    }

    // 源 getIndustry (GET getIndustry) 标注 @Deprecated, 按规则不迁。
}
