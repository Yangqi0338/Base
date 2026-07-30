package com.newzkl.platform.base.biz.account.model.merchant.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户领域视图对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.entity.Merchant}。
 * 旧领域实体与旧 {@code MerchantVO} 在 {@code wxMpConfig} 上类型不一致 (实体为
 * {@code WxMpConfigVO}, VO 为 String), 本仓统一为强类型值对象, 由 DO 的 JSON
 * 列 typeHandler 负责序列化。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantVO extends BaseRes {

    /**
     * 名称 (查询)
     */
    private String name;

    /**
     * 账号名称 (查询)
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
     * 微信公众号配置
     */
    private WxMpConfigVO wxMpConfig;

    /**
     * 渠道商 ID
     */
    private Long channelId;
}
