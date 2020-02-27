package com.pp.mapper;

import com.pp.bean.Employee;
import org.apache.ibatis.annotations.Select;

public interface EmployeeMapperAnnotation {

    @Select("select * from tbl_employee where id = #{id}")
    Employee getEmployeeById(int id);
}
