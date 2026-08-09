package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ChannelQuery extends BizPageQuery {
    /**
     * 主体类型 (查询)
     */
    private AccountEnum.BodyType bodyType;
    /**
     * 状态 (查询)
     */
    private List<ChannelEnum.State> stateList;
    /**
     * 联系人
     */
    private String contactsName;
    /**
     * 模糊查询渠道商名称、渠道商ID、门店名称
     */
    private String searchNameOrId;
    /**
     * 状态大于
     */
    private ChannelEnum.State stateOver;
    /**
     * 账号名称 (查询)
     */
    private String username;
    /**
     * 渠道商名称
     */
    private String nickname;
    /**
     * 渠道商名称
     */
    private String channelName;
    /**
     * 渠道商名称/主题账号 复合查询
     */
    private String search;
    /**
     * 上级id
     */
    private Long invitedId;

    /**
     * 设置单个状态 (内部包装为列表)
     *
     * @param state 渠道商状态
     */
    public void setState(ChannelEnum.State state) {
        this.stateList = doWrapperList(this.stateList, state);
    }
}
