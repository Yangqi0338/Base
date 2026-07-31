package com.newzkl.platform.base.biz.activity.application.bonus.deploy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.activity.model.bonus.res.ActivityQueryRes;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivateActivityReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityConfigSaveReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityQueryPageReq;
import com.newzkl.platform.base.biz.activity.model.event.req.ActivityQueryReq;
import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityQueryDetailVO;
import com.newzkl.platform.base.biz.activity.model.event.vo.ActivityQueryPageVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

/**
 * 奖金池配置应用接口
 *
 * @author niu
 */
public interface BonusDeploy {

    /**
     * 查询活动信息
     *
     * @param req 活动查询请求
     * @return 活动信息聚合
     */
    ActivityQueryRes queryActivity(ActivityQueryReq req);

    /**
     * 保存奖金池配置
     *
     * @param req 活动配置保存请求
     */
    void saveBonusPoolConfig(ActivityConfigSaveReq req);

    /**
     * 开启活动
     *
     * @param req 活动查询请求
     * @return 处理结果
     */
    PlatformResult<Object> startBonus(ActivityQueryReq req);

    /**
     * 开启新版活动
     *
     * @param req 激活活动请求
     */
    void startNewBonus(ActivateActivityReq req);

    /**
     * 关闭活动
     *
     * @param req 活动查询请求
     * @return 处理结果
     */
    PlatformResult<Object> closeBonus(ActivityQueryReq req);

    /**
     * 关闭新版活动
     *
     * @param req 激活活动请求
     */
    void closeNewBouns(ActivateActivityReq req);

    /**
     * 分页查询活动列表
     *
     * @param req 分页查询请求
     * @return 活动分页
     */
    Page<ActivityQueryPageVO> listActivity(ActivityQueryPageReq req);

    /**
     * 查询活动详情
     *
     * @param id 活动主键
     * @return 活动详情
     */
    ActivityQueryDetailVO activityDetail(Long id);

    /**
     * 新增活动配置
     *
     * @param req 活动配置保存请求
     */
    void addActivityConfig(ActivityConfigSaveReq req);

    /**
     * 更新活动配置
     *
     * @param req 活动配置保存请求
     */
    void updateActivityConfig(ActivityConfigSaveReq req);

    /**
     * 取消活动
     *
     * @param req 激活活动请求
     */
    void cancelActivity(ActivateActivityReq req);

    /**
     * 校验活动是否可取消
     *
     * @param req 激活活动请求
     * @return 是否可取消
     */
    Boolean checkCancelActivity(ActivateActivityReq req);
}
