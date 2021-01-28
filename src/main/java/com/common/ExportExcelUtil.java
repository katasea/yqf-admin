package com.common;

import org.apache.poi.hssf.usermodel.*;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class ExportExcelUtil<T> {
	public void export(String sheetName,String[] headers, String[] columns,List<T> lists, OutputStream os) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		sheet.setDefaultColumnWidth(15);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFRow row = sheet.createRow(0);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell headerCell = row.createCell(i);
			headerCell.setCellValue(headers[i]);
			headerCell.setCellStyle(style);
		}
		Iterator<T> it = lists.iterator();
		int rowIndex = 0;
		while (it.hasNext()) {
			rowIndex++;
			row = sheet.createRow(rowIndex);
			T t = it.next();
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				for (int j = 0; j < columns.length; j++) {
					if (fieldName.equals(columns[j])) {
						String getMethodName = "get" +fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Class cls = t.getClass();
						Method getMethod = cls.getMethod(getMethodName, new Class[]{});
						Object val = getMethod.invoke(t, new Object[]{});
						String textVal = null;
						if (null != val) {
							textVal = val.toString();
						} else {
							textVal = null;
						}
						row.createCell(j).setCellValue(textVal);
					}
				}
			}
		}
		wb.write(os);
	}


}