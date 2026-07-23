package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.newzkl.platform.base.biz.store.model.fitment.req.FitmentTemplateQuery;
import com.newzkl.platform.base.biz.store.model.fitment.vo.FitmentTemplateVO;
import com.newzkl.platform.base.biz.store.infrastructure.entity.FitmentTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FitmentTemplateDAO继承基类
 */
@Mapper
@Repository
public interface FitmentTemplateDAO extends com.baomidou.mybatisplus.core.mapper.BaseMapper<FitmentTemplateDO> {

    /**
     * 查询模板列表
     * @param req
     * @return
     */
    List<FitmentTemplateVO> queryTemplate(FitmentTemplateQuery req);

    /**
     * 更新模板默认状态
     * @param channelId
     */
    void alterTemplateDefaultSate(Long channelId);

    /**
     * 查询模板
     * @param id
     * @return
     */
    FitmentTemplateVO queryTemplateById(Long id);

    /**
     * 查询默认模板
     * @return
     */
    FitmentTemplateVO queryTemplateDefault();

    /**
     * 查询样板店模板id
     * @param modelShopId
     * @return
     */
    Long queryModelShopTemplateId(Long modelShopId);
}