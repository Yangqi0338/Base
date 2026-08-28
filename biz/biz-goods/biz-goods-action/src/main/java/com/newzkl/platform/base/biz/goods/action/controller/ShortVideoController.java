package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.video.service.ShortVideoDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-短视频控制器
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/shortVideo")
@RequiredArgsConstructor
@FuncPermission("短视频管理")
public class ShortVideoController {

    private final ShortVideoDomain shortVideoDomain;

    /**
     * 新增短视频
     *
     * @param req 短视频请求
     * @return 短视频主键
     */
    @PostMapping("/add")
    @FuncPermission("新增短视频")
    public PlatformResult<Long> add(@Validated @RequestBody ShortVideoReq req) {
        return PlatformResult.success(shortVideoDomain.add(req));
    }

    /**
     * 短视频详情
     *
     * @param id 短视频主键
     * @return 短视频详情
     */
    @GetMapping("/{id}")
    public PlatformResult<ShortVideoVO> detail(@PathVariable Long id) {
        return PlatformResult.success(shortVideoDomain.detail(id));
    }

    /**
     * 编辑短视频
     *
     * @param req 短视频请求 (id 必填)
     * @return 空结果
     */
    @PutMapping("/edit")
    @FuncPermission("编辑短视频")
    public PlatformResult<Void> edit(@Validated(UpdateCommand.class) @RequestBody ShortVideoReq req) {
        shortVideoDomain.edit(req);
        return PlatformResult.success();
    }

    /**
     * 删除短视频
     *
     * @param id 短视频主键
     * @return 空结果
     */
    @DeleteMapping("/del/{id}")
    @FuncPermission("删除短视频")
    public PlatformResult<Void> del(@PathVariable Long id) {
        shortVideoDomain.del(id);
        return PlatformResult.success();
    }

    /**
     * 短视频分页
     *
     * @param query 短视频查询条件
     * @return 短视频分页
     */
    @PostMapping("/queryPageList")
    public PlatformResult<Page<ShortVideoVO>> queryPageList(@RequestBody ShortVideoQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(shortVideoDomain.page(query));
    }
}
