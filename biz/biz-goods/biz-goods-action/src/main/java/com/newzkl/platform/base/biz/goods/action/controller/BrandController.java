package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.action.cmd.CommonCmd;
import com.newzkl.platform.base.biz.goods.domain.brand.service.BrandDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.BrandPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.BrandReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-品牌控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandDomain brandDomain;

    /**
     * 创建品牌
     *
     * @param brandReq 品牌请求
     * @return 品牌主键
     */
    @PostMapping("brandCreate")
    public PlatformResult<Long> brandCreate(@Validated @RequestBody BrandReq brandReq) {
        if (brandReq.getId() != null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(brandDomain.brandSave(brandReq));
    }

    /**
     * 删除品牌
     *
     * @param idList 主键列表命令
     * @return 空结果
     */
    @PostMapping("brandDelete")
    public PlatformResult<Void> brandDelete(@RequestBody CommonCmd.IdList idList) {
        brandDomain.brandDelete(idList.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改品牌
     *
     * @param brandReq 品牌请求
     * @return 空结果
     */
    @PostMapping("brandUpdate")
    public PlatformResult<Void> brandUpdate(@Validated @RequestBody BrandReq brandReq) {
        if (brandReq.getId() == null) {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        brandDomain.brandSave(brandReq);
        return PlatformResult.success();
    }

    /**
     * 品牌详情
     *
     * @param id 品牌主键
     * @return 品牌详情
     */
    @GetMapping("brand")
    public PlatformResult<BrandVO> brandVO(@RequestParam("id") Long id) {
        return PlatformResult.success(brandDomain.brandById(id));
    }

    /**
     * 品牌分页
     *
     * @param brandQuery 品牌查询条件
     * @return 品牌分页
     */
    @PostMapping("brandPage")
    public PlatformResult<Page<BrandVO>> brandPage(@RequestBody BrandPageQuery brandQuery) {
        return PlatformResult.success(brandDomain.brandPage(brandQuery));
    }

    // TODO[service-gap]: 源 submitBrandAudit (POST submitBrandAudit) 未迁。
    // 源链路为 IBaseService#submitBrandAudit → IAuditFacade#submitBrand(审批模板 BRAND_CREATE),
    // Base 全仓无审批提交 facade/port, biz-goods 亦无 AuditDataBrandVO / AuditAccountVO 对等模型,
    // 接线只能造桩, 故不接。待审批提交端口建立后按原契约恢复。
}
