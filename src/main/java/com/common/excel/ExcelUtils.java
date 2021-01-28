package com.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;


public class ExcelUtils {


    public static void main(String args[]){

        ExcelUtils excelUtils=new ExcelUtils();
        String xlsFileName = UUID.randomUUID().toString() + ".xls";
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("D:/tmp/" + xlsFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String,Object> data=new HashMap<String,Object>();
        data.put("createTime",new Date());
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        data.put("endCreateTime",calendar.getTime());
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("typeText","typeText1");
        map.put("cardName","cardName1");
        map.put("cardNo","cardNo1");
        Map<String,Object> map2=new HashMap<String,Object>();
        map2.put("typeText","typeText2");
        map2.put("cardName","cardName2");
        map2.put("cardNo","cardNo2");
        list.add(map);
        list.add(map2);
        data.put("data", list);
        data.put("nowDate",new Date());
        try {
//            new FileInputStream("E:/tmp/xlstpl/fuck.xls");
//            File file=new File("E:/tmp/xlstpl/fuck.xls");
            new ExcelUtils().renderTemplateAsExcel(new FileInputStream(ExcelUtils.class.getResource("").getPath()+"fuck.xls"), data).write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public Workbook renderTemplateAsExcel(String tplName, Map<String, Object> data) {
		InputStream is = null;
		try {
			is = new FileInputStream(tplName);
			return renderTemplateAsExcel(is, data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	public Workbook renderTemplateAsExcel(File tplFile, Map<String, Object> data) {
		InputStream is = null;
		try {
			is = new FileInputStream(tplFile);
			return renderTemplateAsExcel(is, data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	public Workbook renderTemplateAsExcel(InputStream tplIs, Map<String, Object> data) {
		try {
			return new XLSTransformer().transformXLS(tplIs, data);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}finally{
			try {
				tplIs.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	public static float getExcelCellAutoHeight(String str, float fontCountInline) {
        float defaultRowHeight = 12.00f;//每一行的高度指定
        float defaultCount = 0.00f;
        for (int i = 0; i < str.length(); i++) {
            float ff = getregex(str.substring(i, i + 1));
            defaultCount = defaultCount + ff;
        }
        return defaultCount * defaultRowHeight;//计算
    }

    public static float getregex(String charStr) {
        
        if(charStr==" ")
        {
            return 0.5f;
        }
        // 判断是否为字母或字符
        if (java.util.regex.Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
            return 0.5f;
        }
        // 判断是否为全角

        if (java.util.regex.Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
            return 1.00f;
        }
        //全角符号 及中文
        if (java.util.regex.Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
            return 1.00f;
        }
        return 0.5f;

    }

}