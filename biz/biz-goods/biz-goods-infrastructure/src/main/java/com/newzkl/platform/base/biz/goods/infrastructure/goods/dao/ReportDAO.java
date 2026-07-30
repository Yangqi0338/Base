package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ReportDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报告DAO
 *
 * @author kc
 */
@Mapper
public interface ReportDAO extends BaseMapper<ReportDO> {

    int countByQuery(@Param("query") ReportQuery query);

    List<Long> idByQuery(@Param("query") ReportQuery query);

    int updateByQuery(@Param("model") ReportDO reportDO, @Param("query") ReportQuery query);

    List<ReportVO> listByQuery(@Param("query") ReportQuery query);

    Page<ReportVO> queryPage(Page<?> page, @Param("query") ReportQuery query);
}