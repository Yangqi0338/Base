package com.newzkl.platform.base.common.ddd.infrastructure.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * DAO公共基类，由MybatisGenerator自动生成请勿修改
 *
 * @param <Model> The Model Class 这里是泛型不是Model类
 * @param <PK>    The Primary Key Class 如果是无主键，则可以用Model来跳过，如果是多主键则是Key类
 */
public interface MyBatisPlusBaseDao<Model, PK extends Serializable> extends BaseMapper<Model> {
    /*
     * 实现见 RepositorySupport#listIds
     * */
    List<PK> idByQuery(@Param("ew") Wrapper<Model> wrapper);

    int columnByQuery(@Param("columnList") List<EditColumnVO> columnList, @Param("ew") Wrapper<Model> wrapper);
}