package com.common;


import com.main.pojo.platform.StateInfo;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类，存放大部分通用方法
 *
 * @author chenfuqiang
 */
public class CommonUtil {

	/**
	 * 判断是否为空字符串
	 *
	 * @param str 字符串
	 * @return true 空 false 非空
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || "null".equals(str.toLowerCase()) || "NaN".equals(str);
	}

	/**
	 * 判断是否不为空字符串
	 *
	 * @param str 字符串
	 * @return true 非空 false 空
	 */
	public static boolean isNotEmpty(String str) {
		return !CommonUtil.isEmpty(str);
	}

	/**
	 * 空字符串转为0
	 *
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String nullToZero(String str) {
		if (CommonUtil.isEmpty(str)) {
			return "0";
		} else {
			return str;
		}
	}

	/**
	 * 空字符串转换为""
	 *
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String nullToStr(String str) {
		if (CommonUtil.isEmpty(str)) {
			return Global.NULLSTRING;
		} else {
			return str;
		}
	}

	/**
	 * 判断list 是否是空的
	 *
	 * @param list 集合
	 * @return true 空 false 非空
	 */
	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 强转字符串
	 *
	 * @param object 对象
	 * @return 字符串
	 */
	public static String vo(Object object) {
		return String.valueOf(object);
	}

	/**
	 * 获取map的值，可以预防空指针问题。
	 *
	 * @param map 集合
	 * @param key 键
	 * @param <T> 泛型
	 * @return 值
	 */
	public static <T> T getMV(Map<String, T> map, String key) {
		if (map == null || map.size() == 0) {
			return null;
		} else {
			return map.get(key);
		}
	}

	/**
	 * 把List<Map> 结果集转换为 Map<Key,Value>形式返回
	 * 例如 [{key:1,value:2}] => {1,2}
	 *
	 * @param list  集合
	 * @param key   键
	 * @param value 值
	 * @return MAP集合
	 */
	public static Map<String, String> changetListToMap(List<Map<String, Object>> list, String key, String value) {
		Map<String, String> resultMap = new HashMap<>();
		if (list != null) {
			for (Map<String, Object> map : list) {
				resultMap.put(String.valueOf(map.get(key)), String.valueOf(map.get(value)));
			}
		}
		return resultMap;
	}

	/**
	 * 把获取的数据库list 变为 map
	 * 例如获取的数据为 [{id:1,name:ff},{id:2,name:dd}] => {1:true,2:true} or {1:<T> value,2...}
	 *
	 * @param list 需要转换的集合对象
	 * @param key list<Map<String,Object> 数据里要把哪个key的值当做新map的key。例如上面例子中的 id
	 * @param value Map的值
	 * @param <T> 泛型
	 * @return Map
	 */
	public static <T>Map<String,T> changetListToMap(List<Map<String, Object>> list, String key,T value) {
		Map<String,T> resultMap = new HashMap<>();
		if (list != null) {
			for (Map<String, Object> map : list) {
				resultMap.put(String.valueOf(map.get(key)), value);
			}
		}
		return resultMap;
	}

	/**
	 * 把List<Map> 结果集转换为 Map<Key,Object>形式返回
	 * 例如 [{key:1,value:2}] => {1,{key:1,value:2}}
	 *
	 * @param list 集合
	 * @param key  键
	 * @return MAP集合
	 */
	public static Map<String, Map<String, Object>> changeListToMap(List<Map<String, Object>> list, String key) {
		Map<String, Map<String, Object>> resultMap = new HashMap<>();
		for (Map<String, Object> map : list) {
			resultMap.put(String.valueOf(map.get(key)), map);
		}
		return resultMap;
	}

	/**
	 * 处理带有中文的关键字。
	 *
	 * @param T       报错提示类 e.g : this.getClass();
	 * @param keyword 关键字
	 * @return 处理结果
	 */
	public static String decodeKeyWord(Class T, String keyword) {
		if (CommonUtil.isEmpty(keyword)) {
			keyword = Global.NULLSTRING;
		} else {
			try {
				keyword = URLDecoder.decode(keyword, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Logger.getLogger(T).error("keyword:" + keyword + "解码失败！");
				keyword = Global.NULLSTRING;
			}
			keyword = keyword.replace("\'", "%");
		}
		return keyword;
	}

	/**
	 * 处理主键规则
	 *
	 * @param mapOfID  主键集合
	 * @param parentId 父节点
	 * @return 字符串主键
	 */
	public static String dealPKRule(Map<String, Object> mapOfID, String parentId) {
		String result;
		if (mapOfID == null) {
			if (CommonUtil.isEmpty(parentId)) {
				result = "1";
			} else {
				result = parentId + "01";
			}
		} else {
			result = String.valueOf(Integer.parseInt(String.valueOf(mapOfID.get("id"))) + 1);
		}
		//统一按规则 01  0101  010101 来
		if(result.length()==1) {
			result = '0'+result;
		}
		return result;
	}

	/**
	 * 根据excel单元格类型获取excel单元格值
	 *
	 * @param cell 单元格
	 * @return 单元格值
	 */
	public static String getCellValue(Cell cell) {
		String cellvalue;
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
				// 如果当前Cell的Type为NUMERIC
				case HSSFCell.CELL_TYPE_NUMERIC: {
					short format = cell.getCellStyle().getDataFormat();
					if (format == 14 || format == 31 || format == 57 || format == 58) {    //excel中的时间格式
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						double value = cell.getNumericCellValue();
						Date date = DateUtil.getJavaDate(value);
						cellvalue = sdf.format(date);
					}
					// 判断当前的cell是否为Date
					else if (HSSFDateUtil.isCellDateFormatted(cell)) {  //先注释日期类型的转换，在实际测试中发现HSSFDateUtil.isCellDateFormatted(cell)只识别2014/02/02这种格式。
						// 如果是Date类型则，取得该Cell的Date值           // 对2014-02-02格式识别不出是日期格式
						Date date = cell.getDateCellValue();
						DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						cellvalue = formater.format(date);
					} else { // 如果是纯数字
						// 取得当前Cell的数值
						cellvalue = NumberToTextConverter.toText(cell.getNumericCellValue());

					}
					break;
				}
				// 如果当前Cell的Type为STRIN
				case HSSFCell.CELL_TYPE_STRING:
					// 取得当前的Cell字符串
					cellvalue = cell.getStringCellValue().replaceAll("'", "''");
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					cellvalue = null;
					break;
				// 默认的Cell值
				default: {
					cellvalue = Global.NULLSTRING;
				}
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;
	}

	/**
	 * 处理临时的上传文件，例如通过excel上传数据
	 * @param file
	 * @return
	 */
	public static File uploadFileDeal(MultipartFile file) {
		String path = System.getProperty("user.dir") + Global.FILE_UPTEMPPATH;
		if (!file.isEmpty()) {
			File tempFile = new File(path);
			if (!tempFile.exists()) {tempFile.mkdirs();}
			String tou = "TempUploadDataExcel" + new Random().nextLong(); //
			String wei = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
			String fileName = tou + wei;
			File targetFile = new File(path, fileName);
			try {
				file.transferTo(targetFile);
			} catch (IOException e) {
				Logger.getLogger(CommonUtil.class).error(e);
			}
			return targetFile;
		}else {
			return null;
		}
	}

	/**
	 * 把状态信息转换为表单需要的map数据
	 * @param stateInfo 状态对象
	 * @return 符合结果的map
	 */
	public static Map<String,Object> changeFormResult(StateInfo stateInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", stateInfo.getFlag());
		map.put("message", stateInfo.getMsg());
		return map;
	}

	/**
	 * DATE:[{key:"yyyyMMdd",value:"/"}]
	 * 获取日期的键值对
	 * @param date 日期
	 * @param info 信息
	 * @return
	 */
	public static Map<String, String> getDateKVList(String date,Map<String, Object> info) {
		String key = String.valueOf(info.get("key"));
		if(key.contains("dd")) {
			List<String> keyList = CommonUtil.getYearMonthDayList(date,Global.NULLSTRING);
			List<String> valueList = CommonUtil.getYearMonthDayList(date,String.valueOf(info.get("value")));
			Map<String,String> result = new HashMap<>();
			for(int i=0; i< keyList.size(); i++) {
				result.put(keyList.get(i),valueList.get(i));
			}
			return result;
		}else if(key.contains("MM")) {
			List<String> keyList = CommonUtil.getYearMonthList(date,Global.NULLSTRING);
			List<String> valueList = CommonUtil.getYearMonthList(date,String.valueOf(info.get("value")));
			Map<String,String> result = new HashMap<>();
			for(int i=0; i< keyList.size(); i++) {
				result.put(keyList.get(i),valueList.get(i));
			}
			return result;
		}else {
			return null;
		}
	}

	//date = 20180910
	private static List<String> getYearMonthDayList(String date, String split) {
		if (CommonUtil.isEmpty(date)) {
			date = Global.year_month_day_time_no_.format(new Date());
		}else {
			date = date.replace("-","");
		}
		int year = Integer.parseInt(date.substring(0, 4));
		String month = date.substring(4, 6);
		int day = 0;
		try {
			day = CommonUtil.getDays(Global.year_month_day_no_.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<>();
		for (int i = 1; i <= day; i++) {
			if (i < 10) {
				result.add(String.valueOf(year) + split + month + split + "0" + i);
			} else {
				result.add(String.valueOf(year) + split + month + split + i);
			}
		}
		return result;
	}

	private static List<String> getYearMonthList(String date, String split) {
		if (CommonUtil.isEmpty(date)) {
			date = Global.year_month_day_time_no_.format(new Date());
		}else {
			date = date.replace("-","");
		}
		int year = Integer.parseInt(date.substring(0, 4));
		List<String> list = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			if (i < 10) {
				list.add(String.valueOf(year) + split + "0" + i);
			} else {
				list.add(String.valueOf(year) + split + i);
			}
		}
		return list;
	}

	private static int getDays(Date date) {
		Calendar cal = Calendar.getInstance(); //调用Calendar 中的方法；
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1); // 把时间调整为当月的第一天；
		cal.add(Calendar.MONTH, 1); // 月份调至下个月；
		cal.add(Calendar.DAY_OF_MONTH, -1); // 时间减去一天（就等于上个月的最后一天）
		int month = cal.get(Calendar.MONTH) + 1; //调取月份（月份在表示中会少 1，如：1月份得出数字是 0；
		int days = cal.get(Calendar.DAY_OF_MONTH);//调取当月的天数。
		return days;
	}

	/**
	 * 相加两个公式计算的结果并返回最终结果集合
	 *
	 * @param tempOfResult 待加集合
	 * @param result 待加集合
	 * @param scale 小数位
	 * @return 最终结果集合
	 */
	public static Map<String, Map<String,String>> addMap(Map<String, Map<String,String>> tempOfResult, Map<String,Map<String,String>> result,int scale) {
		if(tempOfResult == null) return result;
		if(result == null) return tempOfResult;
		Map<String,Map<String,String>> finalResult = new HashMap<>();
		Set<String> keys = new HashSet<>();
		keys.addAll(tempOfResult.keySet());
		keys.addAll(result.keySet());

		for(String key : keys) {
			String res = MathUtil.cacComplex(
			    CommonUtil.strFormatScale(tempOfResult.get(key).get("data"),scale)
					+"+"+
					CommonUtil.strFormatScale(result.get(key).get("data"),scale),scale);
			String desc = CommonUtil.nullToZero(tempOfResult.get(key).get("desc"))+"+"+CommonUtil.nullToZero(result.get(key).get("desc"));
			Map<String,String> temp = new HashMap<>();
			temp.put("data",res);
			temp.put("desc",desc);
			finalResult.put(key,temp);
		}
		return finalResult;
	}

	/**
	 * 格式化字符串  23 保留两位  输出 23.00
	 *
	 * @param data 字符串数字
	 * @param scale 保留几位小数
	 * @return 格式化后字符串
	 */
	public static String strFormatScale(String data, int scale) {
		StringBuilder sb = new StringBuilder();
		for(int i =0 ; i < scale; i++) {
			if(sb.length() == 0) {sb.append(".");}
			sb.append("0");
		}
		return new DecimalFormat(",##0"+sb.toString()).format(Double.parseDouble(CommonUtil.nullToZero(data)));
	}
}
