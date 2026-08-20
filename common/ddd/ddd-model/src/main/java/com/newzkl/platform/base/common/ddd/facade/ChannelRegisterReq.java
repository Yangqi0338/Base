package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/1316:14
 */
@Data
public class ChannelRegisterReq implements Serializable {

    private Long accountId;

    private AccountEnum.Identity identity;

    private CommonEnum.YesOrNo storePermission;

    @NotBlank
    private String contactName;

    @NotBlank
    private String storeName;

    @NotBlank
    private String contactPhone;


}
