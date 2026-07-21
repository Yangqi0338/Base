package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 甄选师
 *
 * @author fang
 */
@Data
@NoArgsConstructor
public class SelectorQuery extends PageQuery {
    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 甄选师账号
     */
    private String username;
    /**
     * 上级甄选师ID
     */
    private Long inviteId;
    /**
     * 上级甄选师账号
     */
    private String inviteUsername;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeBegin;
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
    /**
     * 状态大于
     */
    private Integer stateOver;

    public SelectorQuery(Long id) {
        this.id = id;
    }
}
