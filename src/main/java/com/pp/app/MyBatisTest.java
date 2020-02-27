package com.pp.app;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pp.bean.Department;
import com.pp.bean.Employee;
import com.pp.bean.EmployeeStatus;
import com.pp.mapper.DepartmentMapper;
import com.pp.mapper.EmployeeMapper;
import com.pp.mapper.EmployeeMapperAnnotation;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MyBatisTest {

    /**
     * 全局配置文件路径
     */
    private static final String RESOURCE = "mybatis-config.xml";

    /**
     * SqlSessionFactory对象
     */
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 对SqlSessionFactory对象进行初始化
     */
    static {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(RESOURCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 第一个MyBatis案例
     */
    @Test
    public void helloMyBatis() {
        // 获取sqlSession实例
        SqlSession sqlSession = null;
        try {
            // 一个sqlSession代表一个数据库连接会话，用完需要关闭
            sqlSession = sqlSessionFactory.openSession();
            // param1:sql语句的唯一标识，需要使用mapper.xml文件的  namespace.sql的id + 方法名  来指定sql语句【和Tesla框架中的selectOne方法很类似】
            // param2:执行sql需要使用的参数
            Employee employee = sqlSession.selectOne("com.pp.mapper.EmployeeMapper.selectEmp", 1);
            System.out.println(employee);
        } finally {
            if (sqlSession != null) {
                // 关闭session
                sqlSession.close();
            }
        }
    }

    /**
     * 利用Mapper文件进行操作
     * <p>
     * 可以利用MyBatis 实现接口式编程
     * <p>
     * 原生:        Dao      --->     DaoImpl
     * MyBatis:     Mapper   --->     xxxMapper.xml
     * <p>
     * SqlSession：代表和数据库的一次会话
     * SqlSession底层和Connection一样，都是非线程安全的。
     * 每次使用都应该获取新的对象，不能放在共享的成员变量中。
     * <p>
     * Mapper接口没有实现类，但是MyBatis会为这个接口生成一个代理对象（将接口和xml文件绑定）：
     * $Proxy employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
     * <p>
     * 两个重要的配置文件：
     * 1、mybatis-config.xml 全局配置文件（数据库连接池信息，事务管理器类信息，系统运行环境信息等等...）
     * 2、xxxMapper.xml Mapper文件（保存了每一个sql语句的映射信息，将sql抽取出来）
     */
    @Test
    public void helloMapperTest() {
        // 获取SqlSession实例
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            System.out.println("MapperClass:" + mapper.getClass());
            Employee employee = mapper.getEmployeeById(1);
            System.out.println("employee:" + employee);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    /**
     * 利用@Select注解，不使用EmployeeMapper.xml文件，进行sql映射
     */
    @Test
    public void helloMapperAnnotationTest() {
        // 获取SqlSession实例
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            EmployeeMapperAnnotation mapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);
            System.out.println("MapperClass:" + mapper.getClass());
            Employee employee = mapper.getEmployeeById(1);
            System.out.println("mapper:" + employee);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    /**
     * 测试增删改查方法
     * <p>
     * MyBatis允许我们定义以下类型的返回值：
     * 1、int：返回影响到行数
     * 2、long：返回影响到行数
     * 3、boolean：如果影响到函数大于0，返回true，否则返回false
     * 4、void：不返回任何数据
     * <p>
     * 注意：我们需要手动提交数据
     * <p>
     * 可以使用如下方式实现自动提交：
     * sqlSession = sqlSessionFactory.openSession(true);
     */
    @Test
    public void fourMethod() {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = new Employee();
            employee.setEmail("pipi@qq.com");
            employee.setGender('1');
            employee.setLastName("Raymond");
//            employee.setId(3);

            // 测试添加
            mapper.addEmployee(employee);
            System.out.println("新添加员工的主键为：" + employee.getId());

            // 测试修改
            // mapper.updateEmployee(employee);

            // 测试删除
//            boolean flag = mapper.deleteEmployeeById(5);
//            System.out.println("是否删除成功：" + flag);

            // 一定要手动提交
            sqlSession.commit();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    /**
     * 方式1：通过#{param1} #{param2} 传入参数
     * <p>
     * 方式2：通过@Param注解，指定传参的名称
     */
    @Test
    public void testParameterPass() {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(true);
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmployeeByIdAndLastName(9, "james");
            System.out.println("查询的结果为:" + employee);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    /**
     * 方式1：通过pojo传递参数
     * <p>
     * 方式2：通过map传递参数
     */
    @Test
    public void testParameterPassByPojoAndMap() {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(true);
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            // map
            // Map<String,Object> map = new HashMap<>();
            // map.put("id","4");
            // map.put("lastName","jerry");
            // Employee employee = mapper.getEmployeeByMap(map);
            // System.out.println(employee);

            // pojo
            Employee where = new Employee();
            where.setId(4);
            where.setLastName("jerry");
            Employee employee = mapper.getEmployeeByPojo(where);
            System.out.println(employee);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    @Test
    public void testParamSrc() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeByIdAndLastName(1, "tom");
        System.out.println(employee);
        sqlSession.close();
    }

    /**
     * 测试${}的使用场景：
     * 对一些不能预编译字段的占位符，比如表名，排序顺序等等...
     */
    @Test
    public void testDollarBracket() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeTestDollarBracket("tbl_employee", 1, "tom");
        System.out.println(employee);
        sqlSession.close();
    }

    /**
     * 测试select标签，及like的使用
     */
    @Test
    public void testSelectLike() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<Employee> employeeList = mapper.getEmployeesByLastNameLike("o");
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
        sqlSession.close();
    }

    /**
     * 测试select标签：将对象封装成map返回
     */
    @Test
    public void testSelectReturnMap() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Map<String, Object> employeeList = mapper.getEmployeeReturnMap("tom");
        for (Map.Entry<String, Object> entry : employeeList.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        sqlSession.close();
    }

    /**
     * 测试select标签：返回对象格式为，Map<Integer,Employee>，将id作为key，JavaBean作为value
     * 在Mapper接口中需要使用@MapKey指定key的列名字
     * 在Mapper.xml中的returnType为employee
     */
    @Test
    public void testSelectReturnKeyMap() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Map<Integer, Employee> employeeList = mapper.getEmployeeReturnMultiMap("o");
        for (Map.Entry<Integer, Employee> entry : employeeList.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        sqlSession.close();
    }

    /**
     * 利用ResultMap查询数据
     */
    @Test
    public void testResultMap() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeUseResultMap("tom");
        System.out.println(employee);
        sqlSession.close();
    }

    /**
     * 测试级联查询：使用自定义的ResultMap
     */
    @Test
    public void testCascadeSelect() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeAndDepartmentById(4);
        System.out.println(employee);
        sqlSession.close();
    }

    /**
     * 测试级联查询：使用自定义的ResultMap，并在ResultMap中定义<association>标签
     */
    @Test
    public void testCascadeSelect2() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeAndDepartmentById2(4);
        System.out.println(employee);
        sqlSession.close();
    }

    /**
     * 使用ResultMap的association标签实现分步查询
     * <p>
     * 好处：可以组合已有的方法来实现相关功能；
     * <p>
     * 并且分步查询支持
     * """延迟加载 【当我们不需要查询部门信息时，可以不查询部门信息】"""
     * <p>
     * 当开启了延迟加载：
     * 执行该段代码时：System.out.println(employee.getLastName());
     * Preparing: select * from tbl_employee where id = ?
     * <p>
     * 当未开启延迟加载：
     * 执行该段代码时：System.out.println(employee.getLastName());
     * Preparing: select * from tbl_employee where id = ?
     * Preparing: select id,dept_name departmentName from tbl_dept where id = ?
     * <p>
     * <p>
     * 通过以上分析，我们可以发现：开启延迟加载可以减少sql查询的次数，提高查询效率！！！
     */
    @Test
    public void testCascadeSelectByStep() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeByIdStep(4);
//        Department department = employee.getDepartment();
        System.out.println(employee.getLastName());
//        System.out.println(department);
        sqlSession.close();
    }

    /**
     * 测试查询部门信息，并将该部门下所有员工信息查询出来
     */
    @Test
    public void testDepartmentWithEmployeeList() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        Department department = mapper.getDepartmentWithEmployeeListById(2);
        System.out.println(department);
        sqlSession.close();
    }

    @Test
    public void testDepartmentEmployeeWithStep() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        Department department = mapper.getDepartmentWithEmployeeListStep(2);
        System.out.println(department);
        sqlSession.close();
    }

    /**
     * 使用Discriminator -> 封装查询返回后的结果
     */
    @Test
    public void testGetEmployeeByDiscriminator() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeByIdDiscriminator(4);
        System.out.println(employee);
        sqlSession.close();
    }

    /**
     *
     */
    @Test
    public void testGetEmployeeConditionIf() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee condition = new Employee();
        condition.setId(1);
        condition.setLastName("tom");
        condition.setEmail("tom@qq.com");
        List<Employee> result = mapper.getEmployeeListByConditionIf(condition);
        System.out.println(result);
        sqlSession.close();
    }

    @Test
    public void testGetEmployeeConditionOtherwise() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee condition = new Employee();
        List<Employee> employeeList = mapper.getEmployeeListByConditionOtherwise(condition);
        System.out.println(employeeList);
        sqlSession.close();
    }

    @Test
    public void testUpdateEmployeeBySet() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee update = new Employee();
        update.setId(1);
        update.setEmail("ddddd@gmail.com");
        mapper.updateEmployeeBySet(update);
        sqlSession.close();
    }

    @Test
    public void testGetEmployeeByForeach() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(4);
        ids.add(6);
        List<Employee> employeeList = mapper.getEmployeeListByConditionForeach(ids);
        System.out.println(employeeList);
        sqlSession.close();
    }

    /**
     * 使用foreach一次性插入多条数据
     */
    @Test
    public void testAddEmployeeList() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Department department = new Department();
        department.setId(1);
        department.setDepartmentName("人力资源部");
        List<Employee> employeeList = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setEmail("emp1@qq.com");
        employee1.setLastName("email1");
        employee1.setGender('1');
        employee1.setDepartment(department);
        employeeList.add(employee1);

        Employee employee2 = new Employee();
        employee2.setEmail("emp2@qq.com");
        employee2.setLastName("email2");
        employee2.setGender('0');
        employee2.setDepartment(department);
        employeeList.add(employee2);

        mapper.addEmployeeList2(employeeList);
        sqlSession.close();
    }

    /**
     * 测试两个内置参数：_parameter _databaseId
     */
    @Test
    public void testInnerParameter() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Map<String, Object> param = new HashMap<>();
        param.put("gender", '0');
        param.put("lastName", "mySecondName");
        int inner = mapper.addEmployeesTestInnerParameter(param);
        System.out.printf("影响了%d行\n", inner);
        sqlSession.close();
    }

    @Test
    public void testGetEmployeesByBind() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<Employee> employeeList = mapper.getEmployeeByBind("o");
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
        sqlSession.close();
    }

    @Test
    public void testGetEmployeeBySql() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<Employee> employeesBySql = mapper.getEmployeesBySql();
        for (Employee employee : employeesBySql) {
            System.out.println(employee);
        }
        sqlSession.close();
    }

    /**
     * 测试一级缓存
     * 不过该方式存在风险。。。【当在会话期间，如果有人修改了数据，第二次查询的结果和第一次查询的结果一样】
     */
    @Test
    public void testFirstLevelCache() throws InterruptedException {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee01 = mapper.getEmployeeById(1);
        System.out.println(employee01);
        sqlSession.clearCache();
        Employee employee02 = mapper.getEmployeeById(1);
        System.out.println(employee02);
    }

    /**
     * 测试二级缓存
     * <p>
     * 测试方法：开启两个sqlSession，执行同样的sql，查看发送了几条sql
     * 注意：要将第一个sqlSession关闭后，第二个sqlSession才能生效
     * <p>
     * 当命中了缓存，会在以DEBUG方式的日志中输出：
     * Cache Hit Ratio [com.pp.mapper.EmployeeMapper]: 0.5 【0.5为命中率，第二次查询缓存命中】
     */
    @Test
    public void testSecondLevelCache() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
        SqlSession sqlSession2 = sqlSessionFactory.openSession(true);

        EmployeeMapper mapper1 = sqlSession1.getMapper(EmployeeMapper.class);
        Employee employee1 = mapper1.getEmployeeById(1);
        sqlSession1.close();
        /********************* 注意：要将第一sqlSession关闭后，二级缓存才生效 ********************/
        // 第二次查询是从缓存中取到的，并没有发送sql去数据库中查询
        EmployeeMapper mapper2 = sqlSession2.getMapper(EmployeeMapper.class);
        Employee employee2 = mapper2.getEmployeeById(1);
        sqlSession2.close();

        System.out.println(employee1 == employee2);
    }

    /**
     * 测试ehcache作为二级缓存
     */
    @Test
    public void testEhcacheAsSecondLevelCache() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeById(1);
        System.out.println(employee);
        sqlSession.close();
        SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper1 = sqlSession1.getMapper(EmployeeMapper.class);
        Employee employee1 = mapper1.getEmployeeById(1);
        System.out.println(employee1);
        sqlSession1.close();
    }

    /**
     * 测试自定义MyBatis插件
     * <p>
     * 四大对象创建的时候，每个创建出来的对象不是直接返回的，还会再调用：
     * interceptorChain.pluginAll()
     * 也就是获取到所有的interceptor，然后执行所有interceptor的plugin方法
     * 然后返回target包装后的对象
     * 我们可以为target对象创建一个代理对象（AOP面向切面）
     * 我们的插件可以为四大对象创建出代理对象
     * 代理对象就可以拦截到四大对象的每一个执行
     */
    @Test
    public void testAddPlugin() {
        sqlSessionFactory.getConfiguration().addInterceptor(new Interceptor() {
            @Override
            public Object intercept(Invocation invocation) throws Throwable {
                return null;
            }

            @Override
            public Object plugin(Object target) {
                if (target instanceof Executor) {
                    System.out.println("------------>事故管理器类：" + ((Executor) target).getTransaction());
                }
                if (target instanceof ParameterHandler) {
                    System.out.println("------------>ParameterHandler的plugin方法：" + ((ParameterHandler) target).getParameterObject());
                }
                return target;
            }

            @Override
            public void setProperties(Properties properties) {

            }
        });
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employeeById = mapper.getEmployeeById(6);
        System.out.println(employeeById);
        sqlSession.close();
    }

    /**
     * 插件编写：
     * 1、编写Interceptor的实现类
     * 2、使用@Intercepts注解完成插件拦截方法的签名【也就是拦截哪个对象的哪个方法】
     * 3、将写好的配置文件，注册在全局配置文件中
     * 在mybatis-config.xml中，使用plugin标签，注册该插件
     */
    @Test
    public void testPlugin() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employeeById = mapper.getEmployeeById(6);
        System.out.println(employeeById);
        sqlSession.close();
    }

    @Test
    public void testPageHelper() {

        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Page<Object> page = PageHelper.startPage(1, 3);
        List<Employee> allEmployees = mapper.getAllEmployees();
        for (Employee employee : allEmployees) {
            System.out.println(employee);
        }
        System.out.println("当前页码：" + page.getPageNum());
        System.out.println("总记录数：" + page.getTotal());
        System.out.println("每页记录数：" + page.getPageSize());
        sqlSession.close();

    }

    /**
     * 测试批处理
     * <p>
     * 以批量的方式执行sql：
     *
     * 预编译1次-> 设置参数10000次-> 执行sql1次
     */
    @Test
    public void testBatch() {
        // 在openSession时，指定execType，从而使用batch的方式执行
        long start = System.currentTimeMillis();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

        for (int i = 0; i < 10000; i++) {
            mapper.addEmployeeBatch(new Employee(UUID.randomUUID().toString().substring(0, 8), '1', "bb" + i + "@qq.com", null));
        }

        sqlSession.commit();
        long end = System.currentTimeMillis();
        System.out.println("执行耗时：" + (end - start));
        sqlSession.close();
    }

    /**
     * 默认情况下，MyBatis在处理枚举的时候，保存的是枚举的名字（也就是name()方法的返回值）
     * 使用的EnumTypeHandler来对枚举进行处理的
     * 我们可以改变使用EnumOrdinalTypeHandler对枚举进行处理
     * 如何使用？
     *      在mybatis-config.xml中，指定typeHandlers标签的内容
     */
    @Test
    public void testEnum(){
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = new Employee("enum",'1',"sss",null);
        employee.setEmployeeStatus(EmployeeStatus.LOGIN);
        mapper.addEmployee(employee);
        sqlSession.commit();
        System.out.println("保存成功，"+employee.getId());
        sqlSession.close();
    }

    @Test
    public void testEnumUse(){
        EmployeeStatus login = EmployeeStatus.LOGIN;
        System.out.println(login.ordinal());
        System.out.println(login.name());
        System.out.println(login.getCode());
        System.out.println(login.getDesc());
    }

    @Test
    public void testSelfDefinedTypeHandler(){
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<Employee> employeeList = mapper.getEmployeeListByEnum(EmployeeStatus.LOGIN);
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
        sqlSession.close();
    }
}