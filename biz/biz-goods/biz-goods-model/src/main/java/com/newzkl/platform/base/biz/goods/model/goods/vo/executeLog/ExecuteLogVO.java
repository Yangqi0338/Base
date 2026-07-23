package com.newzkl.platform.base.biz.goods.model.goods.vo.executeLog;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * 操作日志
 * @author fang
 */
@Data
public class ExecuteLogVO extends BaseVO {
     /**
     * ID
     */
     private Long id;
     /**
     * 操作类型
     */
     private Integer type;
     /**
     * 操作主键
     */
     private Long targetId;
     /**
     * 操作人名称
     */
     private String executeUserName;
     /**
     * 原数据
     */
     private String oldData;
     /**
     * 改动数据
     */
     private String updateData;
}