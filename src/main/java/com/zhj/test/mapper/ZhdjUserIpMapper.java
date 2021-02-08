package com.zhj.test.mapper;

import com.zhj.test.entity.ZhdjUserIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ZhdjUserIpMapper {
    List<ZhdjUserIp> getZhdjUserIpByTable(@Param(value = "table") String table,@Param(value = "start") int start,@Param(value = "end") int end);
    int getCountByTable(@Param(value = "table") String table);
}