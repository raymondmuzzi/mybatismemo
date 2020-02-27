package com.pp.bean;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("employee")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1l;
    private int id;
    private String lastName;
    private char gender;
    private String email;
    private Department department;
    private EmployeeStatus employeeStatus;

    public Employee() {
    }

    public Employee(String lastName, char gender, String email, Department department) {
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public EmployeeStatus getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(EmployeeStatus employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", department=" + department +
                ", employeeStatus=" + employeeStatus +
                '}';
    }
}
