package com.zhj.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;

public class ZhdjUserIp {
    @ExcelProperty(value = "id",index = 0)
    private Long id;
    @ExcelProperty(value = "登录时间",index = 1)
    private String loginTime;
    @ExcelProperty(value = "用户名",index = 2)
    private String userName;
    @ExcelProperty(value = "ip地址",index = 3)
    private String ip;
    @ExcelProperty(value = "ip归属",index = 4)
    private String flag; //是否是国内ip

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
