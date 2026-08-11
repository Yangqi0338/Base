package com.newzkl.platform.base.biz.socialbang.model.bonus.res;

import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityOtherConfigVO;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 活动查询返回对象
 * @Author: niu
 * @Date: 2024/1/23 12:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityQueryRes {

    /**
     * 活动信息
     */
    private ActivityVO activityVO;

    /**
     * 其他配置
     */
    private ActivityOtherConfigVO configVO;
}
