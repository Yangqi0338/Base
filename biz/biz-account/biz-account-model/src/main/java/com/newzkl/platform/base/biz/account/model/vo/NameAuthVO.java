package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 实名认证信息
 * @date 2023/12/1916:59
 */
@Data
public class NameAuthVO implements Serializable {
    /**
     * 正面图片地址
     */
    private String upImg;
    /**
     * 背面图片地址
     */
    private String downImg;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String code;
    /**
     * 居住地址
     */
    private String address;
    /**
     * 有效期开始
     */
    private String timeStart;
    /**
     * 有效期结束
     */
    private String timeEnd;
}
