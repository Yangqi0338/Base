package com.newzkl.platform.base.biz.account.model.res;

import lombok.Data;

/**
 * 市场运营商
 *
 * @author fang
 */
@Data
public class OperatorInfo {

    private String companyName;

    private String logo;

    private String background;

    private Long icpNum;

    private Long companyNum;

    private String phone;
}