package com.newzkl.platform.base.biz.account.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 更新客户联系人请求对象
 *
 * @author niu
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
