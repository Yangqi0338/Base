package com.newzkl.platform.base.biz.account.action.cmd;

import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 通用列编辑命令。
 *
 * <p>用于承载按列增量修改的入参: 目标实体 ID 与待修改列集合。</p>
 *
 * @author fang
 */
@Data
public class EditColumnCmd {

    /**
     * 目标实体 ID。
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 待修改列集合。
     */
    private List<EditColumnVO> editColumnList;
}
