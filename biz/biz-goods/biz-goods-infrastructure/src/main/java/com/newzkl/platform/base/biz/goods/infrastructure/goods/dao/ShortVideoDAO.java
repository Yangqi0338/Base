package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ShortVideoDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品-短视频 DAO
 *
 * <p>CRUD 走 MyBatis-Plus, 仅列表 join 查询落 {@code ShortVideoDAO.xml}。</p>
 *
 * @author KC
 */
@Mapper
public interface ShortVideoDAO extends BaseMapper<ShortVideoDO> {

    /**
     * 短视频分页 (join video_spu_relation + spu)
     *
     * @param page  分页对象
     * @param query 短视频查询
     * @return 短视频视图分页
     */
    Page<ShortVideoVO> queryPage(Page<?> page, @Param("query") ShortVideoQuery query);
}
