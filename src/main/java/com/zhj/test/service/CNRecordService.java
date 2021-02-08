package com.zhj.test.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zhj.test.entity.ZhdjUserIp;
import com.zhj.test.mapper.CnipMapper;
import com.zhj.test.model.Cnip;
import com.zhj.test.model.CnipExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhj.test.util.CNIPRecognizer.ipToLong;
import static com.zhj.test.util.CNIPRecognizer.isValidIpV4Address;
@Service
public class CNRecordService {
    //一次最大获取条数
    int size=3000000;
    @Autowired
    private CnipMapper cnipMapper;
    @Autowired
    private ZhdjUserIpService zhdjUserIpService;
    long RANGE_SIZE = 10000000L;
    //更新最新ip数据
    public void parseOnline(String str1){
        Map<Integer, List<Cnip>> recordMap = getRecordMap(getCnip());
        List<Cnip> cnips = new ArrayList<>();
        String lists[] = str1.split("\n");
        try {
            for (String str:lists) {
                if (str.startsWith("apnic") && str.contains("|CN|ipv4|")){
                    String a[] = str.split("\\|");
                    Cnip cnip=new Cnip(ipToLong(a[3]),Integer.parseInt(a[4]));
                    long value=ipToLong(a[3]);
                    int key = (int)(value / RANGE_SIZE);
                    //如果key在内
                    if(recordMap.containsKey(key)){
                        List<Cnip> list = recordMap.get(key);
                        //list中没有，则更新
                        if(!list.stream().anyMatch((Cnip r) -> r.contains(value))){
                            cnipMapper.updateByPrimaryKey(cnip);
                        }
                    }else{  //如果key不在内，直接添加
                        cnipMapper.insert(cnip);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //ip是否在数据库中
    public boolean isCNIP(String ip,Map<Integer, List<Cnip>> recordMap){
        if(ip == null || ip.trim().isEmpty()){
            return false;
        }
        //判断是否为ipv4地址
        if(isValidIpV4Address(ip)){
            //将ip转换为长整型
            long value = ipToLong(ip);
            int key = (int)(value / RANGE_SIZE);
            //Map<Integer, List<Cnip>> recordMap = new HashMap<>();
            //recordMap=getRecordMap(getCnip());
            if(recordMap.containsKey(key)){
                List<Cnip> list = recordMap.get(key);
                for(Cnip cnip:list){
                    if(cnip.contains(value)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public List<Cnip> getCnip(){
        List<Cnip> list = new ArrayList<>();
        CnipExample cnipExample=new CnipExample();
        cnipExample.createCriteria().andStartIsNotNull();
        list=cnipMapper.selectByExample(cnipExample);
        return list;
    }

    //将list转换为keyvalue形式，方便查找
    public Map<Integer, List<Cnip>> getRecordMap(List<Cnip> list){
        Map<Integer, List<Cnip>> recordMap = new HashMap<>();
        list.forEach(r -> {
            int key1 = (int)(r.getStart() / RANGE_SIZE);
            int key2 = (int)((r.getStart() + r.getCount()) / RANGE_SIZE);
            //getOrDefault方法获取指定 key 对应对 value，如果找不到 key ，则返回设置的默认值
            List key1List = recordMap.getOrDefault(key1, new ArrayList<>());
            key1List.add(r);
            recordMap.put(key1, key1List);
            if(key2 > key1){
                List key2List = recordMap.getOrDefault(key2, new ArrayList<>());
                key2List.add(r);
                recordMap.put(key2, key2List);
            }
        });
        return recordMap;
    }
    public void page(int page, int totalCount, int totalPage, String table, ExcelWriter excelWriter){
        List<ZhdjUserIp> list=new ArrayList<>();
        if(page>totalPage){
            return;
        }
        int offset = size * (page - 1);
        if(page<totalPage){
            list=zhdjUserIpService.getZhdjUserIpByTable(table,offset,size);
        }else if(page==totalPage){
            list=zhdjUserIpService.getZhdjUserIpByTable(table,offset,totalCount-offset);
        }else{
            list=zhdjUserIpService.getZhdjUserIpByTable(table,offset,totalCount);
        }
        page++;
        List<ZhdjUserIp> fglist=new ArrayList<>();
        List<String> iplist=new ArrayList<>();
        Map<Integer, List<Cnip>> recordMap = new HashMap<Integer, List<Cnip>>();
        recordMap=getRecordMap(getCnip());
        for(ZhdjUserIp zhdjUserIp:list){
            //数据为空有两种情况"" 或者null,需要先判断null,否则isEmpty回报空指针异常
            if(zhdjUserIp.getIp()!=null){
                if(!zhdjUserIp.getIp().isEmpty()){
                    String[] ips=zhdjUserIp.getIp().split(", ");
                    for(String ip:ips){
                        if(!isCNIP(ip,recordMap)){
                            if(!iplist.contains(ip)){
                                iplist.add(ip);
                                zhdjUserIp.setFlag("国外");
                                zhdjUserIp.setIp(ip);
                                fglist.add(zhdjUserIp);
                            }
                        }
                    }
                }
            }
        }
        WriteSheet writeSheet = EasyExcel.writerSheet("国外ip报告").build();
        excelWriter.write(fglist, writeSheet);
        page(page,totalPage,totalCount,table, excelWriter);
    }
}
