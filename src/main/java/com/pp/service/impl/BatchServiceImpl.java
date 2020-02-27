package com.pp.service.impl;

import com.pp.bean.Employee;
import com.pp.mapper.EmployeeMapper;
import com.pp.service.BatchService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("batchService")
public class BatchServiceImpl implements BatchService {

//    @Autowired
//    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void batchInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for (int i = 0; i < 10000; i++) {
            mapper.addEmployeeBatch(new Employee("batch-service-"+i,'0',"",null));
        }
        sqlSession.commit();
//        EmployeeMapper mapper = sqlSessionTemplate.getMapper(EmployeeMapper.class);
//        for (int i = 0; i < 10000; i++) {
//            mapper.addEmployeeBatch(new Employee("batch-service-" + i, '1', "aaa", null));
//        }
    }
}
