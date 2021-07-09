package com.corp.tech;

import com.alibaba.fastjson.JSON;
import com.corp.utils.RSAUtil;
import org.testng.Reporter;
import java.util.HashMap;
import java.util.Map;

public class Encrypt {
    static final String priKey = "";
    static final String pubKey = "";
    public static String encrypt(String source){
        //System.out.println("未加密请求参数: "+source);
        //Reporter.log("未加密请求参数：" + source);
        /*加密*/
        String responseParam = RSAUtil.encrypt(source, pubKey);
        /*签名*/
        String responseSign = RSAUtil.sign(responseParam, priKey);
        Map<String, String> map = new HashMap<>();
        map.put("data", responseParam);
        map.put("sign", responseSign);
        return JSON.toJSONString(map);
    }
    public static String decrypt(String source){
        return RSAUtil.decrypt(source,priKey);
    }
}
