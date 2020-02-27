package com.pp.mapper;

import com.pp.bean.Department;
import org.apache.ibatis.annotations.Param;

public interface DepartmentMapper {
    /**
     * @param id
     * @return
     */
    Department getDepartmentById(@Param("id") int id);

    /**
     * 使用collection，从数据库中查询到该Department中所有的Employee
     *
     * @param id
     * @return
     */
    Department getDepartmentWithEmployeeListById(@Param("id") int id);

    /**
     * 使用分步查询，并利用collection标签查询Employee列表
     *
     * @param id
     * @return
     */
    Department getDepartmentWithEmployeeListStep(@Param("id") int id);

}
