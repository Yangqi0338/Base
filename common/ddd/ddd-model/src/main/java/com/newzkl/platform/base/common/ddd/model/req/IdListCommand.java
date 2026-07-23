package com.newzkl.platform.base.common.ddd.model.req;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * ID 列表通用入参。
 *
 * @author muc_fang
 */
@Data
public class IdListCommand implements Serializable {

    /**
     * ID 列表。
     */
    @NotEmpty
    private List<Long> idList;

    /**
     * 设置单个 ID (置于列表首位)。
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.idList = CollUtil.setOrAppend(Opt.ofNullable(this.idList).orElse(CollUtil.newArrayList()), 0, id);
    }
}
