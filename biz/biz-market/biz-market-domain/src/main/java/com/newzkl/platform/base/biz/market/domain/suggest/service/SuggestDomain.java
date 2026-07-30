package com.newzkl.platform.base.biz.market.domain.suggest.service;

import com.newzkl.platform.base.biz.market.model.suggest.req.CommitTagReq;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;

import java.util.List;

/**
 * 建议标签领域服务
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.other.service.ISuggestService},
 * 按新规范去 {@code I} 前缀。</p>
 *
 * @author KC
 */
public interface SuggestDomain {

    /**
     * 运营商保存标签配置
     *
     * @param req 标签配置请求
     */
    void saveTagConfig(TagConfigReq req);

    /**
     * 交易师提交建议标签
     *
     * @param req 提交请求
     */
    void commitTag(CommitTagReq req);

    /**
     * 查询标签配置
     *
     * @param operator {@code true} 运营商查自身配置; {@code false} 交易师查其上级运营商配置
     * @return 标签配置视图, 无配置时返回 null
     */
    TagConfigVO queryTagConfig(boolean operator);

    /**
     * 查询当前账号的提交记录
     *
     * @return 提交视图, 无记录时返回 null
     */
    CommitTagVO queryCommitTag();

    /**
     * 运营商查询旗下全部建议
     *
     * @param mobile 提交人手机号过滤, 可为 null
     * @return 提交视图列表, 恒非 null
     */
    List<CommitTagVO> queryOperatorAllSuggest(String mobile);
}
