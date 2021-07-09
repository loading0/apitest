package com.corp.test;

import com.corp.utils.HttpClientUtils;
import org.testng.annotations.Test;
import org.testng.Reporter;
import java.util.HashMap;


/**
 * @author loading
 * @date 2020/8/21 16:15
 */
public class Query extends BaseTest {
    @Test(dataProvider = "caseData", dataProviderClass = CaseDataProvider.class)
    public void query(HashMap<String, String> excelData){

        String encryptedParam = excelData.get("Parameter");
        Reporter.log("读取excel请求参数：" + excelData.get("Parameter"));
        String encryptedResp = HttpClientUtils.sendRequest(processParam(encryptedParam),excelData.get("ApiName"),excelData.get("Method"));
        Reporter.log("响应原始报文：" + encryptedResp);
        System.out.println("==请求响应原始报文：==" + encryptedResp);
        Reporter.log("期望数据：" + excelData.get("Expect"));
        saveParams(excelData.get("Parameter"), encryptedResp,excelData.get("Save"));
        caseVerifyResult(encryptedResp,excelData.get("Expect"));
        /*
        String encryptedParam = Encrypt.encrypt(excelData.get("Parameter"));
        System.out.println("encrypted request jsonstr: "+encryptedParam);
        Reporter.log("未加密请求参数：" + excelData.get("Parameter"));
        //解密
        String decryptResp = Encrypt.decrypt(JSONObject.parseObject(encryptedResp).getString("data"));
        System.out.println("请求响应解密后报文：==" + decryptResp);
        Reporter.log("请求响应解密后报文：" + decryptResp);
        saveParams(excelData.get("Parameter"), decryptResp,excelData.get("Save"));
        if (StringUtils.isEmpty(resBodyData)){
            caseVerifyResult(encryptedResp,excelData.get("Expect"));
        }
        else {
            String decryptResp = Encrypt.decrypt(resBodyData);
            System.out.println("请求响应解密后报文：==" + decryptResp);
            Reporter.log("请求响应解密后报文：" + decryptResp);
            Reporter.log("期望数据：" + excelData.get("Expect"));
            caseVerifyResult((JSONObject.parseObject(decryptResp)).getString("data"),excelData.get("Expect"));
        }
         */
    }


    @Test(dataProvider = "caseData", dataProviderClass = CaseDataProvider.class)
    public void delivery(HashMap<String, String> excelData){

    }
}
