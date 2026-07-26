package com.newzkl.platform.base.biz.market.domain.suggest.repository;

import com.newzkl.platform.base.biz.market.model.suggest.req.CommitTagReq;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;

import java.util.List;

/**
 * 建议标签仓储端口。
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.other.repository.ISuggestRepository},
 * 按新规范去 {@code I} 前缀。</p>
 *
 * @author KC
 */
public interface SuggestRepository {

    /**
     * 保存 (或更新) 运营商标签配置。
     *
     * @param req 标签配置请求
     */
    void saveTagConfig(TagConfigReq req);

    /**
     * 提交建议标签。
     *
     * @param req 提交请求
     */
    void commitTag(CommitTagReq req);

    /**
     * 查询标签配置。
     *
     * @param operator {@code true} 查当前账号(运营商)自身配置;
     *                 {@code false} 查当前账号直属上级运营商的配置
     * @return 标签配置视图, 无配置时返回 null
     */
    TagConfigVO queryTagConfig(boolean operator);

    /**
     * 查询当前账号的建议提交记录。
     *
     * @return 提交视图, 无记录时返回 null
     */
    CommitTagVO queryCommitTag();

    /**
     * 运营商查询旗下全部建议提交。
     *
     * @param mobile 提交人手机号过滤, 可为 null
     * @return 提交视图列表, 恒非 null
     */
    List<CommitTagVO> queryOperatorAllSuggest(String mobile);
}
