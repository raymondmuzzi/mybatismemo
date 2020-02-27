package com.pp.mbg;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class GeneratorTest {

    /**
     * 全局配置文件路径
     */
    private static final String RESOURCE = "mybatis-config-generator.xml";

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

//    @Test
//    public void testGenerator() {
//        SqlSession sqlSession = sqlSessionFactory.openSession(true);
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        int i = mapper.deleteByPrimaryKey(1);
//        System.out.printf("影响了%d行\n", i);
//        sqlSession.close();
//    }
//
//    @Test
//    public void testMyBatis3Generator() {
//        SqlSession sqlSession = sqlSessionFactory.openSession(true);
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        List<Employee> employeeList = mapper.selectByExample(null);
//        for (Employee employee : employeeList) {
//            System.out.println(employee);
//        }
//        sqlSession.close();
//    }
//
//    @Test
//    public void testMyBatis3GeneratorByExample() {
//        SqlSession sqlSession = sqlSessionFactory.openSession(true);
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        // 查询员工名字中有e字幕的，员工性别为1的
//        // 封装员工查询条件的Example
//        EmployeeExample example = new EmployeeExample();
//        // 创建一个Criteria，这个Criteria用来拼装查询条件
//        EmployeeExample.Criteria criteria = example.createCriteria();
//        criteria.andLastNameLike("%o%");
//        criteria.andGenderEqualTo("1");
//
//        // 当使用or条件时，需要新创建一个Criteria
//        EmployeeExample.Criteria criteria1 = example.createCriteria();
//        example.or(criteria1);
//
//        List<Employee> employees = mapper.selectByExample(example);
//        for (Employee employee : employees) {
//            System.out.println(employee);
//        }
//        sqlSession.close();
//    }
}
