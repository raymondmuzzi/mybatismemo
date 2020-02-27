package com.pp.handler;

import com.pp.bean.EmployeeStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义的TypeHandler
 */
public class EmployeeStatusTypeHandler extends BaseTypeHandler<EmployeeStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EmployeeStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public EmployeeStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return EmployeeStatus.getEmployeeStatusByCode(rs.getInt(columnName));
    }

    @Override
    public EmployeeStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return EmployeeStatus.getEmployeeStatusByCode(rs.getInt(columnIndex));
    }

    @Override
    public EmployeeStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return EmployeeStatus.getEmployeeStatusByCode(cs.getInt(columnIndex));
    }
}
