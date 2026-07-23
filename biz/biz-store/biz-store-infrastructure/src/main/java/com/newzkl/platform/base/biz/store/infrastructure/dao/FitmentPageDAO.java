package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.model.fitment.req.FitmentPageQuery;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentPageVO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.FitmentPageDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FitmentPageDAO继承基类
 */
@Mapper
@Repository
public interface FitmentPageDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<FitmentPageDO> {


    /**
     * 查询装修页面
     * @param req
     * @return
     */
    List<FitmentPageVO> queryPage(FitmentPageQuery req);

    /**
     * 查询页面详情
     * @param id
     * @return
     */
    FitmentPageVO queryPageById(Long id);

    /**
     * 查询装修页面
     * @param templateId
     * @return
     */
    List<FitmentPageVO> queryPageList(Long templateId);

    /**
     * 根据模板id查询装修页面
     * @param templateId
     * @return
     */
    List<FitmentPageDO> queryPageByTemplateId(Long templateId);
}