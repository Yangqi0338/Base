package com.newzkl.platform.base.biz.goods.action.controller;

import com.newzkl.platform.base.biz.goods.action.cmd.CategoryCmd;
import com.newzkl.platform.base.biz.goods.action.cmd.CommonCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.SpuCategoryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品-分类控制器
 *
 * @author KC
 */
@RestController("goodsCategoryController")
@RequestMapping("/goods/category")
@RequiredArgsConstructor
public class CategoryController {

    private final SpuDomain spuDomain;
    private final SpuCategoryService spuCategoryService;

    /**
     * 创建分类
     *
     * <p>平台角色归属平台 (accountId=0), 渠道商归属自身账号, 其余角色不允许创建。</p>
     *
     * @param categoryReq 分类请求
     * @return 分类主键
     */
    @PostMapping("categoryCreate")
    public PlatformResult<Long> categoryCreate(@Validated @RequestBody SpuCategoryReq categoryReq) {
        if (categoryReq.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        Long roleId = SecurityUtils.getRoleId();
        if (RoleEnum.CompanyRole.PLATFORM.getCode().equals(roleId)) {
            categoryReq.setAccountId(0L);
        } else if (RoleEnum.CompanyRole.CHANNEL.getCode().equals(roleId)) {
            categoryReq.setAccountId(SecurityUtils.getAccountId());
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(spuDomain.categorySave(categoryReq));
    }

    /**
     * 删除分类
     *
     * @param idList 主键列表命令
     * @return 空结果
     */
    @PostMapping("categoryDelete")
    public PlatformResult<Void> categoryDelete(@RequestBody CommonCmd.IdList idList) {
        spuDomain.categoryDelete(idList.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改分类
     *
     * @param categoryReq 分类请求
     * @return 空结果
     */
    @PostMapping("categoryUpdate")
    public PlatformResult<Void> categoryUpdate(@Validated @RequestBody SpuCategoryReq categoryReq) {
        spuDomain.categorySave(categoryReq);
        return PlatformResult.success();
    }

    /**
     * 分类树列表
     *
     * @param categoryQuery 分类查询条件
     * @return 分类树
     */
    @PostMapping("categoryList")
    public PlatformResult<List<SpuCategoryVO>> categoryPage(@RequestBody SpuCategoryQuery categoryQuery) {
        return PlatformResult.success(spuDomain.categoryTree(categoryQuery));
    }

    /**
     * app 市场分类树列表
     *
     * <p>固定查平台归属分类 (accountId = 0)。</p>
     *
     * @param categoryQuery 分类查询条件
     * @return 分类树
     */
    @PostMapping("appCategoryList")
    public PlatformResult<List<SpuCategoryVO>> appCategoryList(@RequestBody SpuCategoryQuery categoryQuery) {
        return PlatformResult.success(BizUtil.listToTree(spuCategoryService.appCategoryList(categoryQuery)));
    }

    /**
     * 分类绑定/解绑品牌
     *
     * <p>分类侧以逗号串保存已绑定的品牌 ID 集合, {@code isBind=true} 追加, {@code false} 剔除。
     * {@code spu_category.brand_id_list} 列 (源 category 表已有, Base 建表时曾删) 已随本端点恢复,
     * DB 迁移脚本见 {@code rebuild/docs/sql}。</p>
     *
     * @param bindCategory 绑定命令 (分类 ID / 品牌 ID / 是否绑定)
     * @return 空结果
     */
    @PostMapping("bindBrand")
    public PlatformResult<Void> bindBrand(@Validated @RequestBody CategoryCmd.BindBrand bindCategory) {
        spuCategoryService.bindBrand(bindCategory.getCategoryId(), bindCategory.getBrandId(),
                Boolean.TRUE.equals(bindCategory.getIsBind()));
        return PlatformResult.success();
    }

    // 已迁移端点:
    // - palletCategoryList (POST /goods/category/palletCategoryList) → 已迁至 plugin-hdh PalletController,
    //   走会订货 HuiDingHuoApiUtils#getCategoryList 外链, 会订货能力聚于 plugin-hdh
    // 源 category (GET category) 已由既有 spuDomain#category 能力覆盖, 本次不在补迁范围。
}
