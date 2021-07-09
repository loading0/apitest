package com.corp.test;

import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseTest {
    public static Map<String, String> commParams = new HashMap<String, String>(); // 公共参数池
    public static Pattern replaceParamPattern = Pattern.compile("\\$\\{(.*?)}");

    /**
     * 对Parameter中带有$前缀的参数进行实例
     * @param
     */
    public static String processParam(String param) {
        if (param == null || param == "") {
            return "";
        }
        Matcher m = replaceParamPattern.matcher(param);
        while (m.find()) {
            String replaceKey = m.group(1);
            String value;
            // 从公共参数池中获取值
            value = getSaveData(replaceKey);
            param = param.replace(m.group(), value);
        }
        return param;
    }

    //公共参数池取$param对应的value
    public static String getSaveData(String key) {
        if ("".equals(key) || !commParams.containsKey(key)) {
            return null;
        } else {
            return commParams.get(key);
        }
    }
    //获取excel中expect字段中的key在response中对应的value
    public static String getCompareValue(String sourceData, String key) {
        key = key.trim();
        String value = "";
        String searchPattern = "\""+ key + "\":\"" + "([^,]*)" + "\""; //"\""+ key +"\":\"" + "([^,]*)" + "\""
        Pattern pattern = Pattern.compile(searchPattern);
        Matcher matcher = pattern.matcher(sourceData.trim());
        if (matcher.find()) {
            value = matcher.group(1);
        }
        return value;
    }

    //对excel中saved字段中需要保存的参数存入公共参数池
    protected void saveParams(String jsonRequest, String jsonResponse, String saveParam) {
        if (null == jsonResponse || "".equals(jsonResponse) || null == saveParam
                || "".equals(saveParam)) {
            return;
        }
        String[] saves = saveParam.split(";");
        for (String key : saves) {
            String value = getCompareValue(jsonRequest, key);
            if ("" == value) {
                value = getCompareValue(jsonResponse, key);
            }
            commParams.put(key,value);
        }
        System.out.println("commParams: "+commParams);
    }

    //用例结果判断
    public static void caseVerifyResult(String responseData, String excelExpectData) {
        //System.out.println("respData:"+responseData);
        Pattern pattern = Pattern.compile("\"([^,]*)\":\"([^,]*)\""); // "\"([^,]*)\":\"([^,]*)\""
        Matcher m = pattern.matcher(excelExpectData.trim());
        while (m.find()) {
            String actualValue = getCompareValue(responseData, m.group(1));
            String exceptValue = m.group(2);
            Assert.assertEquals(actualValue,exceptValue,"验证结果失败");
        }
    }
}
