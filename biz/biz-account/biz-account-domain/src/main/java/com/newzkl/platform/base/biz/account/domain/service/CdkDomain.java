package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkReq;
import com.newzkl.platform.base.biz.account.model.cdk.req.ToCdkCommand;
import com.newzkl.platform.base.biz.account.model.cdk.res.CdkRes;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;

import java.util.List;

/**
 * 开通码领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.service.ICdkDomain}。
 * 语义为角色/门店开通码 (表 {@code cdk}), 与商品域的虚拟商品兑换码无关。
 * 旧接口的 {@code buyCreateCdk}(购买生成, 由订单支付回调触发)、{@code jfCreateCdk}(按手机号分配,
 * 依赖账号远程查询) 与 {@code testCdk}(调试入口) 未随本切片迁移。</p>
 *
 * @author KC
 */
public interface CdkDomain {

    /**
     * 开通码保存
     *
     * @param req 开通码入参
     * @return 主键 ID
     */
    Long save(CdkReq req);

    /**
     * 开通码修改
     *
     * @param id  开通码 ID
     * @param req 开通码入参
     * @return 影响行数
     */
    int edit(Long id, CdkReq req);

    /**
     * 开通码删除
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 开通码领域视图
     *
     * @param id 开通码 ID
     * @return 开通码领域视图, 无则 null
     */
    CdkVO cdk(Long id);

    /**
     * 开通码详情 (对外出参)
     *
     * @param id 开通码 ID
     * @return 开通码出参, 无则 null
     */
    CdkRes detail(Long id);

    /**
     * 开通码分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CdkRes> pageList(CdkQuery query);

    /**
     * 按查询条件取开通码 ID 列表
     *
     * @param query 查询条件
     * @return ID 列表, 无数据返回空集合
     */
    List<Long> idByQuery(CdkQuery query);

    /**
     * 随机生成开通码
     *
     * <p>保留旧语义: 单次上限 1000 个; 生成 12 位随机值并与库内同系统类型已有值去重 (递归补足);
     * 归属人角色固定为运营商。</p>
     *
     * @param belowId    归属运营商 ID
     * @param number     生成数量
     * @param systemType 系统类型
     * @return 生成的开通码值列表
     */
    List<String> randomCreateCdk(Long belowId, Integer number, Integer systemType);

    /**
     * 按给定值直接生成开通码
     *
     * @param belowId    归属运营商 ID
     * @param systemType 系统类型
     * @param valueList  开通码值列表
     * @return 生成的开通码 ID 列表
     */
    List<Long> directCreateCdk(Long belowId, Integer systemType, List<String> valueList);

    /**
     * 分配开通码
     *
     * <p>保留旧语义: 运营商可分配给交易师或渠道商, 交易师仅可分配给渠道商, 其余组合抛业务异常。</p>
     *
     * @param command 分配命令
     * @return 影响行数
     */
    int toCdk(ToCdkCommand command);

    /**
     * 修改开通码兑换状态
     *
     * <p>保留旧语义: 用户已使用 ({@code useType == 0}) 的开通码不允许改回未使用。</p>
     *
     * @param id       开通码 ID
     * @param useState 兑换状态: 0 未兑换 1 已兑换
     */
    void cdkStateEdit(Long id, Integer useState);
}
