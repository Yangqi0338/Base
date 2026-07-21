package com.newzkl.platform.base.biz.account.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author niu
 * @description: 更新客户贡献数据请求对象
 * @date 2024/1/25 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelContactReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 名字
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;
}
