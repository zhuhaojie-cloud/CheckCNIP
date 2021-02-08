package com.zhj.test.task;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.zhj.test.entity.ZhdjUserIp;
import com.zhj.test.service.CNRecordService;
import com.zhj.test.service.ZhdjUserIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.zhj.test.util.GetAPNIC.request;

@Component
public class SchedulerTask {
    //一次最大获取条数
    private int size=3000000;
    @Autowired
    CNRecordService cNRecordService;
    @Autowired
    ZhdjUserIpService zhdjUserIpService;

    //每30分钟更新一次
    @Scheduled(cron = "0 0/30 * * * ?")
    private void updateCNIP(){
        Map map = request("http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest");
        cNRecordService.parseOnline(String.valueOf(map.get("result")));
    }
    //每天晚上11点30点汇报
    @Scheduled(cron = "0 30 23 * * ?")
    private void report(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyyMMdd");
        //表名
        String table="zhdj_"+sdf.format(date);
        //先获取总条数，如果超过3000000条就分页查询。
        int totalCount=zhdjUserIpService.getCountByTable(table);
        int totalPage=0;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        String fileName = "C:\\FGIP.xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, ZhdjUserIp.class).build();
        //分页查询
        cNRecordService.page(1,totalCount,totalPage,table,excelWriter);
        excelWriter.finish();
    }
}