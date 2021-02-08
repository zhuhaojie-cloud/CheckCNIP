package com.zhj.test.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.zhj.test.entity.ZhdjUserIp;
import com.zhj.test.model.Cnip;
import com.zhj.test.service.CNRecordService;
import com.zhj.test.service.ZhdjUserIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhj.test.util.GetAPNIC.request;

@Controller
public class IndexController {
    //一次最大获取条数
    int size=3000000;
    @Autowired
    CNRecordService cNRecordService;
    @Autowired
    ZhdjUserIpService zhdjUserIpService;

    @GetMapping("/")
    public String index(){
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
        String fileName = "F:\\FGIP.xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, ZhdjUserIp.class).build();
        //分页查询
        cNRecordService.page(1,totalCount,totalPage,table,excelWriter);
        excelWriter.finish();
        return "index";
    }
    @PostMapping("/sreach")
    public String sreach(@RequestParam(name="ip") String ip, Model model){
        //cn 1.0.2.0 AU 1.0.4.0
        Map<Integer, List<Cnip>> recordMap = new HashMap<>();
        recordMap=cNRecordService.getRecordMap(cNRecordService.getCnip());
        if(cNRecordService.isCNIP(ip,recordMap)){
            System.out.println("国内ip");
            model.addAttribute("result","国内ip");
        }else{
            System.out.println("国外ip");
            model.addAttribute("result","国外ip");
        }
        return "hello";
    }
    //手动更新ip
    @GetMapping("/getAPNIC")
    public void getAPNIC(){
        Map map = request("http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest");
        cNRecordService.parseOnline(String.valueOf(map.get("result")));
    }

}
