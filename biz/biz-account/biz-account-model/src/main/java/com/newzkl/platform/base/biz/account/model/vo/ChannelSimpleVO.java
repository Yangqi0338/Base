package com.newzkl.platform.base.biz.account.model.vo;


import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
public class ChannelSimpleVO extends BaseVO {
    /**
     * 头像
     */
    private String headImg;
    /**
     * 适配
     */
    private String channelName;

}