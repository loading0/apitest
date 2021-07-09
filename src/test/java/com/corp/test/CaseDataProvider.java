package com.corp.test;

import com.corp.utils.ExcelUtil;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author loading
 * @date 2020/8/21 17:17
 */
public class CaseDataProvider {
    @DataProvider(name="caseData")
    Iterator<Object[]> dataProvider(Method method) throws IOException {
        String excelPath = "case/test_case.xls";
        File file = Paths.get(System.getProperty("user.dir"),excelPath).toFile();
        ExcelUtil e=new ExcelUtil(file.getAbsolutePath(), method.getName());
        List<HashMap<String, String>> dataList=e.getExcelData();
        //System.out.println("dataprovider get exceldata:"+dataList);
        List<Object[]> dataProvider = new ArrayList<Object[]>();
        for (HashMap<String, String> data : dataList) {
            if ("Y".equals(data.get("Run"))) {
                dataProvider.add(new Object[] { data });
            }
        }
        return dataProvider.iterator();
        //HashMap<String,String>[][]dataList = e.getExcelData();
        //return e.getExcelData();
    }
}
