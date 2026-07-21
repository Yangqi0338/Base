package com.newzkl.platform.base.biz.account.model.auth.req;


import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import lombok.Data;

@Data
public class FunctionReq {

    /**
     * id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口路径
     */
    private String urlPath;

    /**
     * 接口类型
     */
    private String urlMethod;

    /**
     * 类型
     *
     * @see AuthEnum.FunctionType
     */
    private String type;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long menderId;

}
