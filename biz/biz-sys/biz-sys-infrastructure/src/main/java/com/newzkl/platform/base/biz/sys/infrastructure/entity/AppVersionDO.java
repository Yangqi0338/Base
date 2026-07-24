package com.newzkl.platform.base.biz.sys.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * app 版本管理数据对象。
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_version")
public class AppVersionDO extends BaseDO {
    /**
     * app code 查询
     */
    @Index
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
