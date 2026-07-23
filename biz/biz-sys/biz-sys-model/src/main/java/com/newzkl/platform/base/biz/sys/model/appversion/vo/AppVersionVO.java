package com.newzkl.platform.base.biz.sys.model.appversion.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * app 版本视图对象。
 *
 * @author niu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppVersionVO extends BaseRes {
    /**
     * app code 查询
     */
    private String appCode;
    /**
     * app 名称
     */
    private String appName;
    /**
     * app 版本
     */
    private String appVersion;
    /**
     * 更新配置
     */
    private Integer updateConfig;
    /**
     * 资源地址
     */
    private String resourceUrl;
}
