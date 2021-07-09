package com.corp.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author loading
 * @date 2020/8/18 18:36
 */


public class ExcelUtil {
	public Workbook workbook;
    public Sheet sheet;
    public Cell cell;
    int rows;
    int columns;
    public String fileName;
    public String caseName;
    public ArrayList<String> arrkey = new ArrayList<String>();
    String sourceFile;
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * @param fileName   excel文件名
     * @param caseName   sheet名
     */
    public ExcelUtil(String fileName, String caseName) {
        super();
        this.fileName = fileName;
        this.caseName = caseName;
    }

    /**
     * 获得excel表中的数据
     */
    public List<HashMap<String, String>> getExcelData() throws IOException {
    	
    	System.out.println(fileName);
		if (null == fileName || "".equals(fileName)) {
			return null;
		}
		
    	Workbook workbook = null;
        FileInputStream inputStream = null;
        
        try {
        	inputStream = new FileInputStream(fileName);
			if (fileName.endsWith(".xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}
			inputStream.close();
        } catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("转换excel文件失败：" + e.getMessage());
		}
        //
        sheet = workbook.getSheet(caseName);
    	rows = sheet.getPhysicalNumberOfRows();
    	//System.out.println("表格行数为："+rows);
        columns = sheet.getRow(0).getPhysicalNumberOfCells();
        List<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();

        // 获得首行的列名，作为hashmap的key值
        Row row = sheet.getRow(0);
        for (Cell cell : row) {
            String cellvalue = getCellValueByCell(cell);
            //System.out.println("首行列名：" + cellvalue);
            arrkey.add(cellvalue);
        }
        // 遍历所有的单元格的值添加到hashmap中
        for (int r = 1; r <rows; r++) {
            row=sheet.getRow(r);
            //mapRowData.clear();
            HashMap<String, String> mapRowData = new HashMap<String, String>();
            //读取table中所有案例
            for(int c = 0; c < columns; c++) {
                /*
                if("N".equals(getCellValueByCell(row.getCell(1)))){
                    break;
                }
                 */
                //System.out.println("单元格：" + getCellValueByCell(row.getCell(c)));
                mapRowData.put(arrkey.get(c), getCellValueByCell(row.getCell(c)));
                if(c == columns-1){
                    //System.out.println("mapRowData: " +mapRowData);
                    listMap.add(mapRowData);
                }
            }
        }
        workbook.close();
        return listMap;
    }
    /**
     * 获得excel文件的路径
     * @return
     * @throws IOException
     */
    public String getPath() throws IOException {
        File directory = new File(".");
        sourceFile = directory.getCanonicalPath() + "\\data\\"
                + fileName + ".xls";
        return sourceFile;
    }

    private static String getCellValueByCell(Cell cell) {

        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {

            return "";
        }
        String cellValue = "";
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();

                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                cellValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                cellValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                //cellValue = "";
                break;
            case FORMULA:   // 公式
                cellValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return cellValue;
    }

}
