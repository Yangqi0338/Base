package com.newzkl.platform.base.common.core.model.req;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ID 列表通用入参
 *
 * @author muc_fang
 */
@Data
public class CodeCommand implements Serializable {

    /**
     * ID 列表
     */
    @NotEmpty
    private List<String> codeList;

    /**
     * 设置单个 ID (置于列表首位)
     */
    public void setCode(String code) {
        this.codeList = CollUtil.setOrAppend(Opt.ofNullable(this.codeList).orElse(CollUtil.newArrayList()), 0, code);
    }

    public String getCode() {
        return CollUtil.getFirst(this.codeList);
    }
}
