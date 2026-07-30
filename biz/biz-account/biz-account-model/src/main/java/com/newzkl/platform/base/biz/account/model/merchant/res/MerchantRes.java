package com.newzkl.platform.base.biz.account.model.merchant.res;

import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户出参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.vo.MerchantVO} 的对外出参角色。
 * 旧出参 {@code wxMpConfig} 为原始 JSON 字符串, 本仓改为强类型值对象;
 * 前端若只读 {@code appId} 等字段, 结构层级不变。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantRes extends BaseRes {

    /**
     * 名称
     */
    private String name;

    /**
     * 账号名称
     */
    private String username;

    /**
     * 营业执照
     */
    private String license;

    /**
     * 数字门店权限: 0 无 1 有
     */
    private Integer storePermission;

    /**
     * 渠道商 ID
     */
    private Long channelId;

    /**
     * 微信公众号配置
     */
    private WxMpConfigVO wxMpConfig;
}
