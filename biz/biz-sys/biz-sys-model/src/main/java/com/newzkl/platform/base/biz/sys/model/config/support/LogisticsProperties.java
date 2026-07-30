package com.newzkl.platform.base.biz.sys.model.config.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 物流查询 (阿里云市场 · 极速快递查询) 配置属性
 *
 * <p>迁移说明: 源 {@code CommonController#queryLogistics} 把 appcode / host / path 三项
 * 硬编码在方法体内, 旧仓无对应配置类。此处外提为配置项, 默认值与旧硬编码逐字一致,
 * 故不加任何配置即与旧行为等价</p>
 *
 * <p>安全提示: {@code appcode} 属三方调用凭证, 默认值仅为保持迁移等价而内置,
 * 应尽快改为经 nacos / 密钥管理下发并轮换该凭证</p>
 *
 * @author KC
 */
@Data
@Component
@ConfigurationProperties(prefix = "scm.logistics")
public class LogisticsProperties {

    /**
     * 阿里云市场 appcode 凭证
     */
    private String appcode = "0d4e543f35894cc8bb19ede6df2dcbe4";

    /**
     * 服务域名
     */
    private String host = "https://jisukdcx.market.alicloudapi.com";

    /**
     * 查询路径
     */
    private String path = "/express/query";
}
