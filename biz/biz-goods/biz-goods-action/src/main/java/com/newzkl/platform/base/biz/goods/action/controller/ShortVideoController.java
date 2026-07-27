package com.newzkl.platform.base.biz.goods.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.video.service.ShortVideoDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-短视频控制器。
 *
 * <p>迁移偏离: 旧 REST 风格 (GET/PUT/DELETE + PathVariable) 统一为 POST + RequestBody/RequestParam,
 * 分页壳 {@code PageInfo} 换为 MyBatis-Plus {@code Page}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/goods/shortVideo")
@RequiredArgsConstructor
public class ShortVideoController {

    private final ShortVideoDomain shortVideoDomain;

    /**
     * 新增短视频。
     *
     * @param req 短视频请求
     * @return 短视频 ID
     */
    @PostMapping("add")
    public PlatformResult<Long> add(@Validated @RequestBody ShortVideoReq req) {
        return PlatformResult.success(shortVideoDomain.add(req));
    }

    /**
     * 修改短视频。
     *
     * @param req 短视频请求 (id 必填)
     * @return 成功结果
     */
    @PostMapping("edit")
    public PlatformResult<Void> edit(@Validated(UpdateCommand.class) @RequestBody ShortVideoReq req) {
        shortVideoDomain.edit(req);
        return PlatformResult.success();
    }

    /**
     * 删除短视频。
     *
     * @param id 短视频 ID
     * @return 成功结果
     */
    @PostMapping("del")
    public PlatformResult<Void> del(@RequestParam("id") Long id) {
        shortVideoDomain.del(id);
        return PlatformResult.success();
    }

    /**
     * 短视频详情。
     *
     * @param id 短视频 ID
     * @return 短视频视图对象
     */
    @GetMapping("detail")
    public PlatformResult<ShortVideoVO> detail(@RequestParam("id") Long id) {
        return PlatformResult.success(shortVideoDomain.detail(id));
    }

    /**
     * 短视频分页。
     *
     * <p>按当前登录账号过滤 (照旧 {@code queryPageList} 逻辑)。</p>
     *
     * @param query 短视频查询
     * @return 短视频分页
     */
    @PostMapping("page")
    public PlatformResult<Page<ShortVideoVO>> page(@RequestBody ShortVideoQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(shortVideoDomain.page(query));
    }
}
