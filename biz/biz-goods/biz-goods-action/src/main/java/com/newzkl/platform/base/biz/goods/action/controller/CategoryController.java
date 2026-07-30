package com.newzkl.platform.base.biz.goods.action.controller;

import com.newzkl.platform.base.biz.goods.action.cmd.CommonCmd;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.spu.SpuCategoryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.BizUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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

    // TODO[service-gap]: 源 palletCategoryList (POST palletCategoryList) 与 bindBrand (POST bindBrand) 未迁。
    // palletCategoryList 源走会订货外部接口 HuiDingHuoApiUtils#getCategoryList, Base 无该外部链路;
    // bindBrand 需 CategoryVO/CategoryReq/CategoryLayerDO 新增 brandIdList 字段并做 DB 迁移。
    // 二者对应的 SpuCategoryService 方法当前均抛 UnsupportedOperationException, 接线即为假端点, 故不接。
    // 源 category (GET category) 已由既有 spuDomain#category 能力覆盖, 本次不在补迁范围。
}
