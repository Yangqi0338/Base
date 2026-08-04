package com.newzkl.platform.base.biz.content.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.content.infrastructure.entity.VideoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 视频 DAO
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.dao.VideoDAO}。类名加 {@code Content}
 * 域前缀以规避跨域 mapper bean 重名。</p>
 *
 * <p><b>旧自定义 XML 未迁移</b>: 旧 {@code VideoDAO.xml} 的 {@code selectVideoPage} 靠
 * {@code LEFT JOIN scm_goods.store_target_interaction_stat} 跨库取点赞/分享数, Base 内容域
 * 与商品域已分库, 该跨库 JOIN 运行期不可用。分页改由 {@code VideoRepositoryImpl} 用
 * {@code BaseLambdaQueryWrapper} 纯条件重写, 点赞/分享数走跨域降级(暂不填充)。
 * 详见 {@code VideoRepositoryImpl} TODO[cross-domain]。</p>
 *
 * @author KC
 */
@Mapper
@Repository
public interface ContentVideoDAO extends BaseMapper<VideoDO> {
}
