package com.newzkl.platform.base.biz.market.action.controller;

import com.newzkl.platform.base.biz.market.domain.suggest.service.SuggestDomain;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 建议标签控制器
 *
 * <p>迁移自 {@code com.zkl.scm.market.interfaces.other.SuggestController},
 * 类级与方法级路径、HTTP verb 逐字沿用。</p>
 *
 * <p>旧 {@code commitTag} / {@code tradeQueryTagConfig} / {@code queryCommitTag} 三个
 * {@code @Deprecated} 死端点不迁。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/suggest")
@RequiredArgsConstructor
@Slf4j
public class SuggestController {

    private final SuggestDomain suggestDomain;

    /**
     * 运营商保存标签
     *
     * @param req 标签配置请求
     * @return 操作结果
     */
    @PostMapping("/saveTagConfig")
    public PlatformResult<Object> saveTagConfig(@RequestBody TagConfigReq req) {
        suggestDomain.saveTagConfig(req);
        return PlatformResult.success();
    }

    /**
     * 运营商查询标签配置
     *
     * @return 标签配置
     */
    @PostMapping("/queryTagConfig")
    public PlatformResult<TagConfigVO> queryTagConfig() {
        return PlatformResult.success(suggestDomain.queryTagConfig(true));
    }

    /**
     * 运营商查询所有建议
     *
     * <p>路径参数 {@code mobile} 传 {@code "0"} 表示不按手机号过滤, 逐字沿用旧语义。</p>
     *
     * @param mobile 手机号
     * @return 建议列表
     */
    @PostMapping("/queryOperatorAllSuggest/{mobile}")
    public PlatformResult<List<CommitTagVO>> queryOperatorAllSuggest(@PathVariable String mobile) {
        if (mobile.equals("0")) {
            mobile = null;
        }
        return PlatformResult.success(suggestDomain.queryOperatorAllSuggest(mobile));
    }
}
