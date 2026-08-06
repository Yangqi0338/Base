package com.newzkl.platform.base.common.core.mybatis.handler;



import com.newzkl.platform.base.common.core.model.money.Money;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Money 与 DB BIGINT 互转的 TypeHandler
 * <p>读: DB null → Money.nullVal(); 非 null → Money.of.</p>
 * <p>写: Money.nullVal() → SQL NULL; 否则 ps.setLong).</p>
 * @ext 分
 * @ext cent
 * @ext getCent(
 */
@MappedTypes(Money.class)
@MappedJdbcTypes(JdbcType.BIGINT)
public class MoneyTypeHandler extends BaseTypeHandler<Money> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType) throws SQLException {
        if (parameter.isNull()) {
            ps.setNull(i, java.sql.Types.BIGINT);
        } else {
            ps.setLong(i, parameter.getCent());
        }
    }

    @Override
    public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
        long cent = rs.getLong(columnName);
        return rs.wasNull() ? Money.nullVal() : Money.of(cent);
    }

    @Override
    public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        long cent = rs.getLong(columnIndex);
        return rs.wasNull() ? Money.nullVal() : Money.of(cent);
    }

    @Override
    public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long cent = cs.getLong(columnIndex);
        return cs.wasNull() ? Money.nullVal() : Money.of(cent);
    }
}
