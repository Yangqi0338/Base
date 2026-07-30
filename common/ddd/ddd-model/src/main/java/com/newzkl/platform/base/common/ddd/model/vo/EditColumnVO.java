package com.newzkl.platform.base.common.ddd.model.vo;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * 列增量编辑项
 *
 * @param name  列名
 * @param count 增量值
 * @author fang
 */
@RecordBuilder
public record EditColumnVO(String name, Integer count) {
}
