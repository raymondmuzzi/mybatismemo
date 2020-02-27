package com.pp.bean;

/**
 * 我们希望数据库保存的是100，200，300这些状态码
 * 而不是索引或者枚举名
 */
public enum EmployeeStatus {
    LOGIN(100, "用户登录"), LOGOUT(200, "用户登出"), REMOVED(300, "用户不存在");

    private int code;
    private String desc;

    EmployeeStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static EmployeeStatus getEmployeeStatusByCode(int code){
        for (EmployeeStatus employeeStatus : EmployeeStatus.values()) {
            if (code==employeeStatus.code){
                return employeeStatus;
            }
        }
        return null;
    }

}
