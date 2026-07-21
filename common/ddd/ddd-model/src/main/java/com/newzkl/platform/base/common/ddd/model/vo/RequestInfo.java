package com.newzkl.platform.base.common.ddd.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访问信息。
 *
 * @author muc_fang
 */
@Data
@AllArgsConstructor
public class RequestInfo {
    /**
     * 访问时间
     */
    private LocalDateTime requestTime;
    /**
     * 访问IP
     */
    private String ip;
}
