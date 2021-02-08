package com.zhj.test.service;

import com.zhj.test.entity.ZhdjUserIp;

import java.util.List;

public interface ZhdjUserIpService {

     List<ZhdjUserIp> getZhdjUserIpByTable(String table,int start,int end);

     int getCountByTable(String table);
}
