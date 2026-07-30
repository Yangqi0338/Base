package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.level.req.LevelQuery;
import com.newzkl.platform.base.biz.account.model.level.vo.LevelVO;

import java.util.List;

/**
 * 等级仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.repository.ILevelRepository}。
 * 旧 {@code calList} (等级计算列表, 含角色向上兼容合并) 属升级引擎, 未随本切片迁移。</p>
 *
 * @author KC
 */
public interface LevelRepository {

    /**
     * 保存等级 (有 ID 则更新, 无 ID 则新建)
     *
     * @param level 等级领域视图
     * @return 主键 ID
     */
    Long save(LevelVO level);

    /**
     * 等级详情
     *
     * @param id 等级 ID
     * @return 等级领域视图, 无则 null
     */
    LevelVO detail(Long id);

    /**
     * 等级列表 (不分页)
     *
     * @param query 查询条件
     * @return 等级列表, 无数据返回空集合
     */
    List<LevelVO> list(LevelQuery query);

    /**
     * 等级分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<LevelVO> pageList(LevelQuery query);
}
