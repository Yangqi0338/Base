package com.newzkl.platform.base.biz.socialbang.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.socialbang.infrastructure.entity.ActivityDO;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivateActivityReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityQueryPageReq;
import com.newzkl.platform.base.biz.socialbang.model.event.req.ActivityQueryReq;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityQueryDetailVO;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityQueryPageVO;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 活动数据访问
 *
 * @author niu
 */
@Mapper
@Repository
public interface ActivityDAO extends BaseMapper<ActivityDO> {

    /**
     * 查询活动数据
     *
     * @param req 查询条件
     * @return 活动
     */
    ActivityVO queryActivity(ActivityQueryReq req);


    ActivityVO queryNewActivity(@Param("req") ActivateActivityReq req);

    /**
     * 更新活动状态
     *
     * @param req   更新条件
     * @param state 状态值
     */
    void alterActivityState(@Param("req") ActivityQueryReq req, @Param("state") Integer state);

    void alterNewActivityState(@Param("activityId") String activityId, @Param("state") String state);

    List<ActivityQueryPageVO> pageList(@Param("req") ActivityQueryPageReq req);


    ActivityQueryDetailVO activityDetail(@Param("id") Long id);
}
