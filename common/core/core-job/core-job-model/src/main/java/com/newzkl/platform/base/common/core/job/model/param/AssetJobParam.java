package com.newzkl.platform.base.common.core.job.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 资产任务参数
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AssetJobParam extends SecondLevelJobParam {

    /**
     * 填充执行日志
     */
    private boolean fillExecuteLog = false;

}
