package com.zhj.test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetAPNIC {
    public static Map request(String url) {
        Map response = new HashMap();
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //设置请求方式
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            response.put("code",responseCode + "");
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer responseBuffer = new StringBuffer();
                while((inputLine = in.readLine())!=null){
                    responseBuffer.append(inputLine).append("\n");
                }
                in.close();
                response.put("result",responseBuffer.toString());
            }else{
                response.put("result","");
            }
        }catch (Exception e){
            response.put("result","failed");
        }
        return response;
    }
}
