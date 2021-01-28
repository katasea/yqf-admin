/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import com.common.StringUtil;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.CommonDao;

/**
 * 描述 解析Excel工具类
 * 
 * @author Fandy Liu
 * @created 2014年11月3日 上午11:59:09
 */
@Component
public class ParseExcelUtil {
	/**
	 * 描述 excel文件中标题所占的行数
	 */
	private static final Integer HEAD_TITILE_ROW_NUM = 0;
	/**
	 * 描述 唯一值验证容器
	 */
	private Map<String, String> uniqueMap = new HashMap<String, String>();
	/**
	 * 描述 输入流
	 */
	private FileInputStream fis;
	/**
	 * 描述 HSSFWorkbook
	 */
	private HSSFWorkbook workBook;
	/**
	 * 描述 HSSFSheet
	 */
	private HSSFSheet sheet;
	/**
	 * 描述 ParseXMLUtil
	 */
	private ParseXMLUtil parseXmlUtil;
	/**
	 * 描述 存放错误信息的StringBuffer
	 */
	private StringBuffer errorString;
	/**
	 * 错误行数
	 */
	private int errorLineCount;

	/**
	 * 描述 当前实体类的code
	 */
	private String curEntityCode;
	/**
	 * 描述 表头map对象：key:entityCode, value:headMap(index,headTitle)
	 */
	private Map curEntityHeadMap;

	/**
	 * 描述 字段的必填：key:entityCode+headTitle, value:true(必填),false(不必填)
	 * 
	 */
	private Map curEntityColRequired;

	/**
	 * 描述 存放每一行的数据
	 */
	private List listDatas;

	/**
	 * 描述 存放每一行错误的数据
	 */
	private List failListDatas;
	/**
	 * 系统登录用户名
	 */
	private String loginName;
	/**
	 * 允许导入的数据所在的行数组
	 */
	private List<Integer> listRows;
	@Resource
	private CommonDao commonDao;

	public List getListRows() {
		return listRows;
	}

	public void setListRows(List listRows) {
		this.listRows = listRows;
	}

	public ParseExcelUtil() {

	}

	/**
	 * 工具初始化
	 * @param excelFile
	 * @param xmlFile
	 * @return
	 */
	public ParseExcelUtil init(File excelFile, File xmlFile, String loginName) {
		try {
			if (excelFile == null) {
				throw new FileNotFoundException();
			}
			fis = new FileInputStream(excelFile);
			workBook = new HSSFWorkbook(fis);
			parseXmlUtil = new ParseXMLUtil(xmlFile);
			errorString = new StringBuffer();
			this.loginName = loginName;
			errorLineCount = 0;
			readExcelData();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 描述 构造函数
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 上午11:59:59
	 * @param excelFile
	 * @param xmlFile
	 */
	/*   public ParseExcelUtil(File excelFile, File xmlFile) {
	       try {
	           if (excelFile == null) {
	               throw new FileNotFoundException();
	           }
	           fis = new FileInputStream(excelFile);
	           workBook = new HSSFWorkbook(fis);
	           parseXmlUtil = new ParseXMLUtil(xmlFile);
	           errorString = new StringBuffer();
	           readExcelData();

	       } catch (FileNotFoundException e) {
	           e.printStackTrace();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	   }*/

	/**
	 * 描述 开始从excel读取数据
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:00:19
	 */
	public void readExcelData() {
		int sheetSize = workBook.getNumberOfSheets();
		for (int i = 0; i < sheetSize; i++) {
			sheet = workBook.getSheetAt(i);
			String entityName = workBook.getSheetName(i);
			if ("bean".equals(entityName) || "省市".equals(entityName)
					|| "市县".equals(entityName))
				continue;
			readSheetData(sheet, entityName);
		}

	}

	/**
	 * 描述 读每个sheet页的数据
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:00:30
	 * @param sheet
	 * @param entityName
	 */
	public void readSheetData(HSSFSheet sheet, String entityName) {

		int rowNumbers = sheet.getPhysicalNumberOfRows();
		int aa = sheet.getLastRowNum();
		Map ent = (Map) parseXmlUtil.getEntityMap().get(entityName);
		if (ent == null) {
			errorString.append(ParseConstans.ERROR_EXCEL_COLUMN_NOT_EQUAL);
			return;
		}
		this.setCurEntityCode((String) ent.get("code"));
		if (rowNumbers == 0) {
			errorString.append(ParseConstans.ERROR_EXCEL_NULL);
			return;
		}
		if (rowNumbers > 100004) {
			errorString.append(ParseConstans.ERROR_EXCEL_DATA_NUM);
			return;
		}
		List colList = (List) parseXmlUtil.getColumnListMap().get(entityName);
		int xmlRowNum = colList.size();
		HSSFRow excelRow = sheet.getRow(0);
		int excelFirstRow = excelRow.getFirstCellNum();
		int excelLastRow = excelRow.getLastCellNum();
		if (xmlRowNum != (excelLastRow - excelFirstRow)) {
			System.out.println("==================xml列数与excel列数不相符，请检查");
			errorString.append(ParseConstans.ERROR_EXCEL_COLUMN_NOT_EQUAL);
			return;
		}
		readSheetHeadData(sheet);
		readSheetColumnData(sheet, entityName);

	}

	/**
	 * 描述 读取sheet页中的表头信息
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:00:58
	 * @param sheet
	 */
	@SuppressWarnings( { "unchecked", "static-access" })
	public void readSheetHeadData(HSSFSheet sheet) {

		Map headMap = new HashMap();
		curEntityHeadMap = new HashMap();
		curEntityColRequired = new HashMap();
		HSSFRow excelheadRow = sheet.getRow(HEAD_TITILE_ROW_NUM);
		int excelLastRow = excelheadRow.getLastCellNum();
		String headTitle = "";
		for (int i = 0; i < excelLastRow; i++) {
			HSSFCell cell = excelheadRow.getCell(i);
			headTitle = this.getStringCellValue(cell).trim();
			if (headTitle.endsWith("*")) {
				curEntityColRequired.put(this.getCurEntityCode() + "_"
						+ headTitle, true);
			} else {
				curEntityColRequired.put(this.getCurEntityCode() + "_"
						+ headTitle, false);
			}
			headMap.put(i, headTitle);
		}
		curEntityHeadMap.put(this.getCurEntityCode(), headMap);
	}

	/**
	 * 描述 读取sheet页里面的数据
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:01:15
	 * @param sheet
	 * @param entityName
	 */
	@SuppressWarnings( { "unchecked", "static-access" })
	public void readSheetColumnData(HSSFSheet sheet, String entityName) {

		HSSFRow excelheadRow = sheet.getRow(0);
		int excelLastcell = excelheadRow.getLastCellNum(); // excel总列数
		int excelRowNum = sheet.getLastRowNum() + 1; // excel总行数
		Map headMap = (Map) this.getCurEntityHeadMap().get(
				this.getCurEntityCode());
		Map colMap = parseXmlUtil.getColumnMap();
		listDatas = new ArrayList();
		failListDatas = new ArrayList();
		listRows = new ArrayList();
		Map<String, Integer> areaMap = new HashMap<String, Integer>();// 网格缓存map
		for (int i = HEAD_TITILE_ROW_NUM + 1; i < excelRowNum + 1; i++) {// 行循环
			System.out.println("开始导入数据:" + i);
			boolean isPass = true;
			HSSFRow columnRow = sheet.getRow(i);
			buildname = "";
			unitname = "";
			if (columnRow != null) {
				Map curRowCellMap = new HashMap();
				boolean allNull = true;
				for (int k = 0; k < excelLastcell; k++) { //解决所有列都是null值的问题
					HSSFCell colCell = columnRow.getCell(k);
					String value = this.getStringCellValue(colCell);
					if (value != null) {
						allNull = false;
						break;
					}
				}
				if (!allNull) {
					for (int j = 0; j < excelLastcell; j++) { // 列循环
						int cout = headMap.get(j).toString().indexOf("*");
						String headTitle = "";
						if (cout == -1) {
							headTitle = headMap.get(j).toString();
						} else {
							headTitle = headMap.get(j).toString().substring(0,
									cout);
						}
						Map curColMap = (Map) colMap.get(entityName + "_"
								+ headTitle);
						if (curColMap == null) {
							// System.out.println(headTitle);
						}
						String curColCode = (String) curColMap.get("code");
						String curColType = (String) curColMap.get("type");
						HSSFCell colCell = columnRow.getCell(j);
						String value = this.getStringCellValue(colCell);
						if (value != null) {
							value = value.trim();
						}
						String xmlColType = (String) curColMap.get("type");
						if (xmlColType.equals("int")) {
							curRowCellMap
									.put(curColCode, value != null ? Integer
											.valueOf(value) : "");
						} else if (xmlColType.equals("Dict")) {
							String dictType = (String) curColMap
									.get("dictType");
							value = DictUtil.dictValueMap.get(dictType).get(
									value + "");
							curRowCellMap.put(curColCode, value);
						} else {
							curRowCellMap.put(curColCode, value);
						}
						/** 验证cell数据 **/
						Boolean pass = validateCellData(i + 1, j + 1, colCell,
								entityName, headTitle, curColType);
						if (pass.equals(false)) { //一行中有一个列的值没通过验证,后面不需要再做验证，并不导入数据库
							isPass = false;
							break;
						}
					}
					if (isPass) {
						listDatas.add(curRowCellMap);
						listRows.add(i + 1);//把数据对应的行放入list中
					} else {
						failListDatas.add(curRowCellMap);
					}
				}
			}
			buildname = "";
			unitname = "";
		}

	}

	/**
	 * 描述 buildname
	 */
	public static String buildname = "";
	/**
	 * 描述 unitname
	 */
	public static String unitname = "";

	/**
	 * 描述 验证单元格数据
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:01:44
	 * @param curRow
	 * @param curCol
	 * @param colCell
	 * @param entityName
	 * @param headName
	 * @param curColType
	 */
	@SuppressWarnings("static-access")
	public boolean validateCellData(int curRow, int curCol, HSSFCell colCell,
			String entityName, String headName, String curColType) {
		boolean pass = true;
		List rulList = (List) parseXmlUtil.getColumnRulesMap().get(
				entityName + "_" + headName);
		Regex reg = new Regex();// 生成验证对象

		if (rulList != null && rulList.size() > 0) {// 其他验证规则，自己添加
			for (int i = 0; i < rulList.size(); i++) {
				Map rulM = (Map) rulList.get(i);
				String rulName = (String) rulM.get("name");
				String rulMsg = (String) rulM.get("message");
				String cellValue = this.getStringCellValue(colCell);
				boolean isNotEmpty = StringUtil.isNotEmpty(cellValue);
				if (rulName.equals(ParseConstans.RULE_NAME_NULLABLE)) {// 非空判断
					if (StringUtil.isEmpty(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty && rulName.equals("buildunit")) {// 非空判断
					unitname = cellValue;
				} else if (isNotEmpty && rulName.equals("buildunit2")) {// 非空判断
					buildname = cellValue;
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_UNIQUE)) {
					String tableName = (String) rulM.get("tableName");
					String colName = (String) rulM.get("colName");
					String sql = "select count(*) CNT from " + tableName + " where " + colName + "='" + cellValue + "'";
					List<String> resultList = commonDao.getList(sql);
					if(Integer.parseInt(resultList.get(0))>0){
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_LENGTH)) {
					String length = (String) rulM.get("length");
					String[] arrlen = length.split(",");
					if (!reg.checkLength(cellValue, arrlen)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_DATE)) {
					String formater = (String) rulM.get("formater");
					if (!reg.checkDate(cellValue, formater)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_IDCARD)) {
					try {
						if (!reg.checkIDCard(cellValue)) {
							pass = false;
							appendEorrMsg(curRow, curCol, rulMsg);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_INT)) {
					if (!reg.checkIntValue(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_EMAIL)) {
					if (!reg.checkEmail(cellValue, 60)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_TEL)) {
					if (!reg.checkTel(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_MOBILE)) {
					if (!reg.checkMobile(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_MOBILEANDTEL)) {
					if (!reg.checkMobileAndTel(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_CHINESE)) {
					if (!reg.checkChinese(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_IP)) {
					if (!reg.checkIP(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_QQ)) {
					if (!reg.checkQQ(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				} else if (isNotEmpty
						&& rulName.equals(ParseConstans.RULE_NAME_POSTCODE)) {
					if (!reg.checkPostCode(cellValue)) {
						pass = false;
						appendEorrMsg(curRow, curCol, rulMsg);
					}
				}
				if (pass == false) {
					Logger.getLogger(this.getClass()).error("第"+curRow+"行,第"+curCol+"列"+","+rulMsg);
					/*daoRuRiZhiService.save(loginName, headName, cellValue,
							curRow, curCol, rulMsg);*/
				}
			}
		}
		return pass;

		/*     if ("楼栋名称".equals(headName) && StringUtils.isNotBlank(buildname) && StringUtils.isNotBlank(unitname)) {
		         String sql = "select count(*) CNT from " + " T_BUILDING_BUILDING t1, T_BUILDING_UNIT t2"
		                 + " where t1.BDNAME = '" + buildname + "'" + " AND t2.UNITNAME = '" + unitname + "'"
		                 + " AND t1.BDID = t2.BDID";
		         Record rec = Db.findFirst(sql);
		         if (rec.getBigDecimal("CNT").intValue() == 0) {
		             appendEorrMsg(curRow, curCol, "楼栋名称单元不匹配");
		         }
		     }
		     if ("楼栋单元".equals(headName) && StringUtils.isNotBlank(buildname) && StringUtils.isNotBlank(unitname)) {
		         String sql = "select count(*) CNT from " + " T_BUILDING_BUILDING t1, T_BUILDING_UNIT t2"
		                 + " where t1.BDNAME = '" + buildname + "'" + " AND t2.UNITNAME = '" + unitname + "'"
		                 + " AND t1.BDID = t2.BDID";
		         Record rec = Db.findFirst(sql);
		         if (rec.getBigDecimal("CNT").intValue() == 0) {
		             appendEorrMsg(curRow, curCol, "楼栋名称单元不匹配");
		         }
		     }*/
	}

	/**
	 * 描述 错误信息中加入
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:02:04
	 * @param curRow
	 * @param curCol
	 * @param rulMsg
	 */
	public void appendEorrMsg(int curRow, int curCol, String rulMsg) {
		errorLineCount = errorLineCount + 1;
		if (errorLineCount <= 11) {
			if (errorLineCount <= 10) {
				errorString.append("第" + curRow + "行,第" + curCol + "列:"
						+ rulMsg + "<br>");
			} else {
				errorString.append("...");
			}
		}
	}

	/**
	 * 描述 获得单元格字符串
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:02:20
	 * @param cell
	 * @return
	 */
	public static String getStringCellValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }

        String result = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    java.text.SimpleDateFormat TIME_FORMATTER = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    result = TIME_FORMATTER.format(cell.getDateCellValue());
                } else {
                    double doubleValue = cell.getNumericCellValue();
                    // 是否为数值型  
	                double d = cell.getNumericCellValue();  
		             if (doubleValue - (int) doubleValue < Double.MIN_VALUE) {   
		             // 是否为int型  
		            	 result = Integer.toString((int) doubleValue);  
		             } else {   
		                 System.out.println("double.....");  
		              // 是否为double型  
		                 result = Double.toString(cell.getNumericCellValue());  
		                 DecimalFormat df = new DecimalFormat("#");  
		                result= df.format(cell.getNumericCellValue());  
		                 System.out.println("phone=="+result);  }
                }
                 break;
            case HSSFCell.CELL_TYPE_STRING:
                if (cell.getRichStringCellValue() == null) {
                    result = null;
                } else {
                    result = cell.getRichStringCellValue().getString();
                }
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                result = null;
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                try {
                    result = String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    result = cell.getRichStringCellValue().getString();
                }
                break;
            default:
                result = "";
        }

        return result;
    }

	/**
	 * 描述 测试
	 * 
	 * @author Fandy Liu
	 * @created 2014年11月3日 下午12:02:35
	 * @param args
	 */
	public static void main(String[] args) {

		/*        File excelFile = new File("src/net/evecom/newplat/utils/excel/常住人口信息登记表.xls");
		 File xmlFile = new File("src/net/evecom/newplat/utils/excel/常住人口信息登记表.xml");
		 ParseExcelUtil excleUtil = new ParseExcelUtil(excelFile, xmlFile);

		 for (int i = 0; i < excleUtil.getListDatas().size(); i++) {
		 // T_PERSON_PERSON t1; 人口基类表
		 // T_PERSON_HOUSEHOLD t2 判断户号存在？更新：插入;
		 // T_PERSON_FAMILY t3 家庭表
		 // T_PERSON_REGISTER t4 户籍人口
		 Map excelCol = (Map) excleUtil.getListDatas().get(i); // 得到第 i 行的数据
		 final Record person = new Record();// 人员基类表
		 final Record houseHold = new Record();// 户籍表
		 final Record family = new Record();// 家庭表
		 final Record residents = new Record();// 户籍人员表
		 @SuppressWarnings("unchecked")
		 Set<String> keyset = excelCol.keySet();
		 for (Iterator it = keyset.iterator(); it.hasNext();) {// 遍历map
		 String key = (String) it.next();
		 int index = key.indexOf(".");
		 String left = key.substring(0, index);
		 String right = key.substring(index + 1, key.length());
		 if ("t1".equals(left)) {
		 person.set(right, excelCol.get(key));
		 } else if ("t2".equals(right)) {
		 houseHold.set(right, excelCol.get(key));
		 } else if ("t3".equals(left)) {
		 family.set(right, excelCol.get(key));
		 } else if ("t4".equals(left)) {
		 residents.set(right, excelCol.get(key));
		 }
		 }
		 boolean succeed = Db.tx(new IAtom() {// 多张表操作的时候进行事物控制。。。切记
		 @Override
		 public boolean run() throws SQLException {
		 try {
		 String seqPerson = UUIDGenerator.getUUID();
		 Integer personId = Db.queryBigDecimal(seqPerson).intValue();
		 person.set("personId", personId);
		 // 0-常住人口1-非常住人口
		 person.set("PERSONTYPE", Constant.ZERO);
		 // 保存人员基类表
		 Db.save("T_PERSON_PERSON", person);
		 // 户号存在？更新：插入;
		 String houseHoldId = person.get("HOUSEHOLDID").toString();
		 int count = Db.queryBigDecimal(
		 " select count(*) from " + " T_PERSON_HOUSEHOLD where HOUSEHOLDID=?",
		 new Object[] { houseHoldId }).intValue();
		 if (count == 0) {
		 // 保存户籍表
		 Db.save("T_PERSON_HOUSEHOLD", houseHold);
		 } else {
		 Db.update("T_PERSON_HOUSEHOLD", "HOUSEHOLDID", houseHold);
		 }
		 // 保存家庭表
		 // Db.save("T_PERSON_FAMILY", family);
		 // 保存户籍人员表
		 residents.set("PERSONID", personId);
		 Db.save("T_PERSON_RESIDENTS", residents);
		 return true;
		 } catch (Exception e) {
		 e.printStackTrace();
		 return false;
		 }
		 }
		 });
		 }
		 */
	}

	public String getCurEntityCode() {
		return curEntityCode;
	}

	public void setCurEntityCode(String curEntityCode) {
		this.curEntityCode = curEntityCode;
	}

	public Map getCurEntityHeadMap() {
		return curEntityHeadMap;
	}

	public void setCurEntityHeadMap(Map curEntityHeadMap) {
		this.curEntityHeadMap = curEntityHeadMap;
	}

	public ParseXMLUtil getParseXmlUtil() {
		return parseXmlUtil;
	}

	public void setParseXmlUtil(ParseXMLUtil parseXmlUtil) {
		this.parseXmlUtil = parseXmlUtil;
	}

	public Map getCurEntityColRequired() {
		return curEntityColRequired;
	}

	public void setCurEntityColRequired(Map curEntityColRequired) {
		this.curEntityColRequired = curEntityColRequired;
	}

	public List getListDatas() {
		return listDatas;
	}

	public void setListDatas(List listDatas) {
		this.listDatas = listDatas;
	}

	public StringBuffer getErrorString() {
		return errorString;
	}

	public void setErrorString(StringBuffer errorString) {
		this.errorString = errorString;
	}

	public int getErrorLineCount() {
		return errorLineCount;
	}

	public void setErrorLineCount(int errorLineCount) {
		this.errorLineCount = errorLineCount;
	}

}
