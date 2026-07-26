package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkEditReq;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;

import java.util.List;
import java.util.Set;

/**
 * 开通码仓储端口。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.repository.ICdkRepository}。
 * 旧接口的 {@code cdkListForApi} (openapi 分页) 与 {@code jfCreateCdk} (按手机号分配, 直连
 * 账号 Dubbo facade) 依赖未迁的 openapi/账号远程能力, 未随本切片迁移。</p>
 *
 * @author KC
 */
public interface CdkRepository {

    /**
     * 保存单个开通码。
     *
     * @param cdk 开通码领域视图 (需带 id)
     * @return 主键 ID
     */
    Long save(CdkVO cdk);

    /**
     * 批量保存开通码。
     *
     * @param cdkList 开通码领域视图列表 (需各自带 id)
     */
    void saveBatch(List<CdkVO> cdkList);

    /**
     * 按主键更新开通码 (仅更新非 null 列)。
     *
     * @param cdk 开通码领域视图 (需带 id)
     * @return 影响行数
     */
    int edit(CdkVO cdk);

    /**
     * 按 ID 列表删除开通码。
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 开通码详情。
     *
     * @param id 开通码 ID
     * @return 开通码领域视图, 无则 null
     */
    CdkVO detail(Long id);

    /**
     * 按开通码值取 ID。
     *
     * @param value 开通码值
     * @return 开通码 ID, 无则 null
     */
    Long idByValue(String value);

    /**
     * 按查询条件批量更新开通码。
     *
     * @param cdk   待更新字段
     * @param query 查询条件
     * @return 影响行数
     */
    int editByQuery(CdkVO cdk, CdkQuery query);

    /**
     * 在指定系统类型下筛出已存在的开通码值。
     *
     * @param systemType 系统类型
     * @param valueList  待校验的开通码值集合
     * @return 已存在的开通码值集合, 无则空集合
     */
    Set<String> existValue(Integer systemType, Set<String> valueList);

    /**
     * 按查询条件取开通码 ID 列表。
     *
     * @param query 查询条件
     * @return ID 列表, 无则空集合
     */
    List<Long> idByQuery(CdkQuery query);

    /**
     * 开通码分配更新。
     *
     * <p>保留旧 SQL 的幂等保护: 指定交易师时要求 {@code dealer_id} 为空,
     * 指定渠道商时要求 {@code channel_id} 为空, 避免重复分配。</p>
     *
     * @param edit   分配字段
     * @param idList 开通码 ID 列表
     * @return 影响行数
     */
    int editForToCdk(CdkEditReq edit, List<Long> idList);

    /**
     * 开通码分页。
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CdkVO> pageList(CdkQuery query);
}
