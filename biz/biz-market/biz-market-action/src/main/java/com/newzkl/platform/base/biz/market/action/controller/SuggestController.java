package com.newzkl.platform.base.biz.market.action.controller;

import com.newzkl.platform.base.biz.market.domain.suggest.service.SuggestDomain;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 建议标签控制器。
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.other.SuggestController}, 端点路径不变。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/suggest")
@RequiredArgsConstructor
public class SuggestController {

    /**
     * 手机号"全部"哨兵值。
     *
     * <p>迁移保留旧行为: 前端以路径变量 {@code "0"} 表示不按手机号过滤,
     * 后端转 null。属前后端既定约定, 勿"优化"掉。</p>
     */
    static final String MOBILE_ALL_SENTINEL = "0";

    private final SuggestDomain suggestDomain;

    /**
     * 运营商保存标签配置。
     *
     * @param req 标签配置请求
     * @return 成功结果
     */
    @PostMapping("/saveTagConfig")
    public PlatformResult<Boolean> saveTagConfig(@RequestBody TagConfigReq req) {
        suggestDomain.saveTagConfig(req);
        return PlatformResult.success();
    }

    /**
     * 运营商查询自身标签配置。
     *
     * @return 标签配置视图
     */
    @PostMapping("/queryTagConfig")
    public PlatformResult<TagConfigVO> queryTagConfig() {
        return PlatformResult.success(suggestDomain.queryTagConfig(true));
    }

    /**
     * 运营商查询旗下所有建议。
     *
     * @param mobile 提交人手机号; 传 {@code "0"} 表示不过滤
     * @return 提交视图列表
     */
    @PostMapping("/queryOperatorAllSuggest/{mobile}")
    public PlatformResult<List<CommitTagVO>> queryOperatorAllSuggest(@PathVariable String mobile) {
        return PlatformResult.success(suggestDomain.queryOperatorAllSuggest(normalizeMobile(mobile)));
    }

    /**
     * 归一化手机号路径变量。
     *
     * @param mobile 原始路径变量
     * @return 过滤用手机号; 哨兵值 {@code "0"} 或 null 时返回 null
     */
    static String normalizeMobile(String mobile) {
        return MOBILE_ALL_SENTINEL.equals(mobile) ? null : mobile;
    }
}
