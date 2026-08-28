package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 账号详情查询(多 key)
 *
 * <p>合并旧 {@code detail}(按 id) 与 {@code getByPhone}(按 phone) 两端点的统一入参,
 * 二者出参同为 {@code AccountOutRes}。id 与 phone 至少传一个, id 优先</p>
 *
 * @author KC
 */
@Data
public class AccountDetailQuery implements Serializable {

    /**
     * 账号 ID, 端类型取当前登录端
     */
    private Long id;

    /**
     * 手机号, 无 id 时按手机号定位账号
     */
    private String phone;
}
