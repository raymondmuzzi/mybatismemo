package com.pp.mapper;

import com.pp.bean.Employee;
import com.pp.bean.EmployeeStatus;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    /**
     * @param id
     * @return
     */
    Employee getEmployeeById(int id);

    /**
     * @param id
     * @param lastName
     * @return
     */
    Employee getEmployeeByIdAndLastName(@Param("id") int id, @Param("lastName") String lastName);

    /**
     * @param employee
     * @return
     */
    Employee getEmployeeByPojo(Employee employee);

    /**
     * @param param
     * @return
     */
    Employee getEmployeeByMap(Map<String, Object> param);

    /**
     * @param tableName
     * @param id
     * @param lastName
     * @return
     */
    Employee getEmployeeTestDollarBracket(@Param("tableName") String tableName,
                                          @Param("id") int id,
                                          @Param("lastName") String lastName);

    /**
     * @param lastName
     * @return
     */
    List<Employee> getEmployeesByLastNameLike(@Param("lastName") String lastName);

    /**
     * @param lastName
     * @return
     */
    Map<String, Object> getEmployeeReturnMap(@Param("lastName") String lastName);


    /**
     * 多条记录封装成一个map：Map<Integer,Employee>：键是这条记录的主键，值是记录封装后的JavaBean
     * <p>
     * 使用@MapKey，表示返回的map中，哪个字段作为key
     *
     * @param lastName
     * @return
     */
    @MapKey("id")
    Map<Integer, Employee> getEmployeeReturnMultiMap(@Param("lastName") String lastName);

    /**
     * @param lastName
     * @return
     */
    Employee getEmployeeUseResultMap(@Param("lastName") String lastName);

    /**
     * 根据id查询员工的员工和部门信息
     * 需要自定义ResultMap，将数据库返回信息封装到ResultMap中
     * <p>
     * 此处可以说明，在一个xxxMapper.xml文件中，可以定义多个ResultMap
     *
     * @param id
     * @return
     */
    Employee getEmployeeAndDepartmentById(@Param("id") int id);

    /**
     * 使用xxxMapper.xml中的<resultMap>标签中的<association/> 标签指定联合的JavaBean对象
     *
     * @param id
     * @return
     */
    Employee getEmployeeAndDepartmentById2(@Param("id") int id);

    /**
     * 使用association进行分步查询
     *
     * @param id
     * @return
     */
    Employee getEmployeeByIdStep(@Param("id") int id);

    /**
     * 根据部门id查询该部门的所有员工
     *
     * @param did
     * @return
     */
    List<Employee> getEmployeeListByDepartmentId(@Param("id") int did);

    /**
     * 使用Discriminator查询员工信息
     *
     * @param id
     * @return
     */
    Employee getEmployeeByIdDiscriminator(@Param("id") int id);

    /**
     * 测试PageHelper
     * @return
     */
    List<Employee> getAllEmployees();

    /**
     * @param employee
     */
    boolean addEmployee(Employee employee);

    /**
     * @param employee
     */
    int updateEmployee(Employee employee);

    /**
     * @param id
     */
    boolean deleteEmployeeById(int id);

    /**
     * 携带了哪个字段，拼接sql的时候就拼接上哪个字段
     *
     * @param employee
     * @return
     */
    List<Employee> getEmployeeListByConditionIf(Employee employee);

    /**
     * 使用otherwise标签查询员工信息
     *
     * @param employee
     * @return
     */
    List<Employee> getEmployeeListByConditionOtherwise(Employee employee);

    /**
     * 使用set标签封装修改操作
     *
     * @param employee
     * @return
     */
    int updateEmployeeBySet(Employee employee);

    /**
     * 使用传递的id集合查询Employee
     *
     * @param ids
     * @return
     */
    List<Employee> getEmployeeListByConditionForeach(@Param("ids") List<Integer> ids);

    /**
     * 利用foreach标签批量添加Employee【一条sql】
     *
     * @param employeeList
     * @return
     */
    int addEmployeeList(@Param("employeeList") List<Employee> employeeList);

    /**
     * 利用foreach标签批量添加Employee【多条sql，循环遍历这多条sql】
     *
     * @param employeeList
     * @return
     */
    int addEmployeeList2(@Param("employeeList") List<Employee> employeeList);

    /**
     * 测试内置参数
     *
     * @param param
     * @return
     */
    int addEmployeesTestInnerParameter(Map<String, Object> param);

    /**
     * 使用bind标签进行模糊查询
     *
     * @param lastName
     * @return
     */
    List<Employee> getEmployeeByBind(@Param("lastName") String lastName);

    /**
     * 使用sql标签，对重复sql代码块实现可重用
     *
     * @return
     */
    List<Employee> getEmployeesBySql();

    /**
     * 测试一级缓存：在同一次会话中，相同的查询操作，只会执行一次
     *
     * @return
     */
    List<Employee> getEmployeeByFirstLevelCache1();

    List<Employee> getEmployeeByFirstLevelCache2();

    /**
     * 使用批量操作，插入数据
     *
     * @return
     */
    int addEmployeeBatch(Employee employee);

    /**
     * 测试自定义TypeHandler
     *
     * @param employeeStatus
     * @return
     */
    List<Employee> getEmployeeListByEnum(@Param("employeeStatus") EmployeeStatus employeeStatus);
}
