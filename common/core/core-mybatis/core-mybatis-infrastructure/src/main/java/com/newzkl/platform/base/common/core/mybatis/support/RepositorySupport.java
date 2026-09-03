package com.newzkl.platform.base.common.core.mybatis.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author kc
 * @date 2023/4/13 11:50:06
 */
@Slf4j
public abstract class RepositorySupport {

    @Resource
    @Nullable
    @Lazy
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据id物理恢复数据
     *
     * @param id 主键id
     * @return 操作结果
     */
    public <T extends BaseIdDO> Boolean recoverDeleteById(Long id, Class<T> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        String tableName = tableInfo.getTableName();
        String sql = "UPDATE " + tableName + " SET del_flag = 0, update_time = NOW() WHERE id = ? AND del_flag IS NULL";
        if (jdbcTemplate == null) return false;
        int update = jdbcTemplate.update(sql,id);
        log.warn("=================> 物理恢复 SQL：" + sql + " | 返回值：" + update);
        return update > 0;
    }

    /**
     * 慎用！！！！！！！！
     * 根据id物理删除数据
     *
     * @param id 主键id
     * @return 操作结果
     */
    public <T extends BaseIdDO> Boolean physicalDeleteById(Long id, Class<T> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        String tableName = tableInfo.getTableName();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        if (jdbcTemplate == null) return false;
        int update = jdbcTemplate.update(sql, id);
        log.warn("=================> 物理删除 SQL：" + sql + " | 返回值：" + update);
        return update > 0;
    }
    /**
     * 慎用！！！！！！！！
     * 根据构造器物理删除数据
     *
     * @param queryWrapper 构造器
     * @return 删除的数量
     */
    public <A extends BaseIdDO, R, Children extends AbstractWrapper<A, R, Children>> Integer physicalDeleteQWrap(
            AbstractWrapper<A, R, Children> queryWrapper, Class<A> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        String tableName = tableInfo.getTableName();

        String customSqlSegment = queryWrapper.getCustomSqlSegment();

        Map<String, Object> paramNameValuePairs = queryWrapper.getParamNameValuePairs();
        ArrayList<Object> list = new ArrayList<>();
        int i = 1;
        for (String key : paramNameValuePairs.keySet()) {
            customSqlSegment = customSqlSegment.replace("#{ew.paramNameValuePairs.MPGENVAL" + i + "}", "?");
            list.add(paramNameValuePairs.get("MPGENVAL" + i));
            i++;
        }

        String sql = String.format("DELETE FROM %s %s", tableName, customSqlSegment);
        Object[] params = list.toArray();
        if (jdbcTemplate == null) return 0;
        int update = jdbcTemplate.update(sql, params);
        log.warn("=================> 物理删除 SQL： " + sql + " | 参数：" + Arrays.toString(params) + " | 删除的行数：" + update);
        return update;
    }

    /**
     * 仅查询一个
     */
    public static  <T, R, Children extends AbstractWrapper<T, R, Children>> AbstractWrapper<T, R, Children> buildOne(
            AbstractWrapper<T, R, Children> wrapper) {
        return wrapper.last("limit 1");
    }

    /**
     * 字符串id 删除
     */
    public <T extends BaseIdDO, M extends BaseMapper<T>> boolean deleteByIds(M mapper, String ids) {
        return mapper.deleteByIds(StrUtil.split(ids, ",")) > 0;
    }

    /**
     * 查出来的数量是否是期待值
     */
    public <T extends BaseIdDO, M extends BaseMapper<T>> boolean hasCount(M mapper, Wrapper<T> queryWrapper, long compare) {
        return mapper.selectCount(queryWrapper) == compare;
    }

    public <T extends BaseIdDO, M extends BaseMapper<T>> List<Long> listIds(M mapper, LambdaQueryWrapper<T> wrapper) {
        return listOneField(mapper, wrapper, T::getId);
    }

    /**
     * 由分页查询构建 mybatis-plus 分页对象
     * <p>新 common {@code PageQuery} 不再耦合 mybatis-plus {@code Page},
     * 转换逻辑收敛于此静态方法。</p>
     *
     * @param query 分页查询
     * @param <T>   记录类型
     * @return mybatis-plus 分页对象
     */
    public static <T> Page<T> page(PageQuery query) {
        Integer pageSize = query.getPageSize();
        return new Page<>(query.getPageNo(), pageSize, pageSize != null && pageSize != Integer.MAX_VALUE);
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> Page<R> page(M mapper, PageQuery pageQuery, BaseLambdaQueryWrapper<T> wrapper, Class<R> clazz) {
        return TransferUtils.transferPage(mapper.selectPage(page(pageQuery),
                wrapper.unwrap().setEntityClass(mapper).select(clazz)
        ), clazz);
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> List<R> list(M mapper, BaseLambdaQueryWrapper<T> wrapper, Class<R> clazz) {
        return TransferUtils.transfers(mapper.selectList(
                wrapper.unwrap().setEntityClass(mapper).select(clazz)
        ), clazz);
    }

    public <T extends BaseIdDO, M extends BaseMapper<T>> T getOne(M mapper, BaseLambdaQueryWrapper<T> wrapper) {
        return CollUtil.getFirst(mapper.selectList(
                buildOne(wrapper.unwrap().setEntityClass(mapper))
        ));
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> R getOne(M mapper, BaseLambdaQueryWrapper<T> wrapper, Class<R> clazz) {
        return TransferUtils.transfer(CollUtil.getFirst(mapper.selectList(
                buildOne(wrapper.unwrap().setEntityClass(mapper).select(clazz))
        )), clazz);
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> List<R> listOneField(M mapper, LambdaQueryWrapper<T> wrapper, SFunction<T, R> function) {
        return mapper.selectList(wrapper.clone()
                .select(function)).stream().map(function).collect(Collectors.toList());
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> List<R> listOneField(M mapper, QueryWrapper<T> wrapper, SFunction<T, R> function) {
        LambdaMeta extract = LambdaUtils.extract(function);
        // TODO
        String implMethodName = extract.getImplMethodName();
        return mapper.selectList(wrapper.clone().select(implMethodName)).stream().map(function).collect(Collectors.toList());
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> List<R> listByIds2OneField(M mapper, List<Long> ids, SFunction<T, R> function) {
        return mapper.selectList(new LambdaQueryWrapper<T>()
                .select(function).in(T::getId, ids)).stream().map(function).collect(Collectors.toList());
    }

    public <K, R, T extends BaseIdDO, M extends BaseMapper<T>> Map<K, R> mapOneField(
            M mapper, LambdaQueryWrapper<T> wrapper, SFunction<T, K> keyFunction, SFunction<T, R> valueFunction) {
        return mapper.selectList(wrapper.clone().select(keyFunction, valueFunction))
                .stream().filter(it -> ObjectUtil.isNotEmpty(valueFunction.apply(it)))
                .collect(CommonUtil.toMap(keyFunction, valueFunction));
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> Map<Long, R> mapOneField(M mapper, LambdaQueryWrapper<T> wrapper, SFunction<T, R> function) {
        return mapOneField(mapper, wrapper, T::getId, function);
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> Map<Long, R> mapByIds2OneField(M mapper, List<Long> ids, SFunction<T, R> valueFunction) {
        return mapOneField(mapper, new LambdaQueryWrapper<T>().in(T::getId, ids), valueFunction);
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> R findOneField(M mapper, LambdaQueryWrapper<T> wrapper, SFunction<T, R> function) {
        return Opt.ofNullable(
                mapper.selectOne(wrapper.clone().select(function), false)
        ).map(function).orElse(null);
    }

    public <T extends BaseIdDO, M extends BaseMapper<T>> Long getId(M mapper, LambdaQueryWrapper<T> wrapper) {
        return findOneField(mapper, wrapper, T::getId);
    }

    public <R, T extends BaseIdDO, M extends BaseMapper<T>> R findById2OneField(M mapper, Long id, SFunction<T, R> function) {
        return findOneField(mapper, new LambdaQueryWrapper<T>().eq(T::getId, id), function);
    }


}
