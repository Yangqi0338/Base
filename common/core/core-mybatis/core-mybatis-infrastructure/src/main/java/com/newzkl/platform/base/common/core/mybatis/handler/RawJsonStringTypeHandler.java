package com.newzkl.platform.base.common.core.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * String 字段存 JSON 直读直写, 不做二次转义
 * @ext 替代 JacksonTypeHandler 在 String 类型上的双重序列化
 * @ext 写: 值本身即视为存储内容, 是否合法 JSON 由业务保证
 * @ext 读: 列内容直接作为 String 返回
 * @ext 用法: @JsonSerializable(typeHandler = RawJsonStringTypeHandler.class), DO 需 @TableName(autoResultMap = true)
 */
public class RawJsonStringTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        /*
         * 值本身可能是: JSON 字符串 / 普通字符串 / 空串
         * 非 JSON 时保底走 JSONUtil.quote 包一层引号, 避免 DB 存进非法 JSON 破坏 json 列约束
         */
        if (StrUtil.isBlank(parameter)) {
            ps.setString(i, parameter);
            return;
        }
        if (JSONUtil.isTypeJSON(parameter)) {
            ps.setString(i, parameter);
        } else {
            ps.setString(i, JSONUtil.quote(parameter));
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
