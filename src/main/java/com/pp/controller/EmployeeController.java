package com.pp.controller;

import com.pp.bean.Employee;
import com.pp.mapper.EmployeeMapper;
import com.pp.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private BatchService batchService;

    @RequestMapping("employeeList")
    public String getEmployeeList(Map<String, Object> map) {
        Employee employee = employeeMapper.getEmployeeById(28);
        map.put("employeeList", employee);
        return "list";
    }

    @RequestMapping("batch")
    public void batchInsertEmployees(){
        batchService.batchInsert();
    }
}