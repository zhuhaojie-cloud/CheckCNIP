package com.zhj.test.service.impl;


import com.zhj.test.entity.ZhdjUserIp;
import com.zhj.test.mapper.ZhdjUserIpMapper;
import com.zhj.test.service.ZhdjUserIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZhdjUserIpServiceImpl implements ZhdjUserIpService {

    @Autowired
    private ZhdjUserIpMapper zhdjUserIpMapper;

    @Override
    public List<ZhdjUserIp> getZhdjUserIpByTable(String table,int start,int end) {
        return zhdjUserIpMapper.getZhdjUserIpByTable(table,start,end);
    }

    @Override
    public int getCountByTable(String table) {
        return zhdjUserIpMapper.getCountByTable(table);
    }

}
