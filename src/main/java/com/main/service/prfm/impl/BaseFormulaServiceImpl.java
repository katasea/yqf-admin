package com.main.service.prfm.impl;

import com.alibaba.fastjson.JSONObject;
import com.common.AccessOptUtil;
import com.common.CommonUtil;
import com.common.Global;
import com.common.MathUtil;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.BaseFormulaDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.BaseFormula;
import com.main.pojo.prfm.ParamBean;
import com.main.service.prfm.BaseFormulaService;
import com.main.service.prfm.CommonService;
import com.main.service.prfm.ParamService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对象服务层实现类
 *
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class BaseFormulaServiceImpl implements BaseFormulaService {
	@Resource
	private BaseFormulaDao baseFormulaDao;
	@Resource
	private CommonDao comDao;
	@Resource
	private ParamService paramService;
	@Resource
	private CommonService commonService;

	@LogAnnotation(moduleName = "基本公式", option = "查询")
	@Override
	public BaseFormula selectByname(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.baseFormulaDao.selectByname(map);
	}

	@LogAnnotation(moduleName = "基本公式", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(baseFormulaDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "已录入重复的name编号，请修改！", null);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public int getCount(String keyword, String node, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		Map<String, Object> resultMap = this.baseFormulaDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@LogAnnotation(moduleName = "基本公式", option = "查询列表数据")
	@Override
	public List<BaseFormula> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		return this.baseFormulaDao.getPage(map);
	}

	/**
	 * 获取 中文名称 对应的 公式信息
	 *
	 * @param keyword 关键词
	 * @param bridge  桥梁
	 * @return 结果
	 */
	@LogAnnotation(moduleName = "基本公式", option = "查询集合")
	@Override
	public Map<String, BaseFormula> getFormulaInfoByName(String keyword, Bridge bridge) {
		List<BaseFormula> list = this.get(keyword, null, bridge, "0", String.valueOf(Global.TABLE_MAX_RECORD));
		Map<String, BaseFormula> resultMap = new HashMap<>();
		for (BaseFormula bean : list) {
			resultMap.put("[" + bean.getName() + "]", bean);
		}
		return resultMap;
	}

	@LogAnnotation(moduleName = "基本公式", option = "删除")
	@Override
	public StateInfo delete(String code, String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(code)) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id", code);
				//顺序不能乱
				this.baseFormulaDao.deleteByPrimaryKey(map);
				if (!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node", parentId);
					map.put("companyid", bridge.getCompanyid());

				}
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		} else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "传递的id为空无法删除，请刷新后重试！", null);
		}
		return stateInfo;
	}

	/**
	 * 获取bean 信息的map 用于传递给mybatis
	 */
	private Map<String, Object> getBeanInfoMap(BaseFormula baseFormula, Bridge bridge) {
		baseFormula.setCompanyid(bridge.getCompanyid());

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid", baseFormula.getCompanyid());
		map.put("name", baseFormula.getName());
		map.put("formula", baseFormula.getFormula());
		map.put("isstop", baseFormula.getIsstop());
		map.put("type", baseFormula.getType());
		return map;
	}

	@LogAnnotation(moduleName = "基本公式", option = "修改")
	@Override
	public StateInfo edit(String name, BaseFormula baseFormula, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(name) && baseFormula != null) {
			Map<String, Object> map = this.getBeanInfoMap(baseFormula, bridge);
			map.put("whereId", name);
			try {
				this.baseFormulaDao.update(map);
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		} else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "传递的关键字段为空或者对象不存在，请刷新页面重试", null);
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "基本公式", option = "新增")
	@Override
	public StateInfo add(String parentId, BaseFormula baseFormula, Bridge bridge) {
		//tree init
		Map<String, Object> map = this.getBeanInfoMap(baseFormula, bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.baseFormulaDao.insert(map);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	/**
	 * 初始化内置数据
	 */
	@LogAnnotation(moduleName = "基本公式", option = "初始化数据")
	@Override
	public StateInfo autoInitFromAccess(Bridge bridge) {

		StateInfo stateInfo = paramService.initData();
		if (stateInfo.getFlag()) {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				this.baseFormulaDao.deleteAll(bridge.getYear());
				conn = AccessOptUtil.connectAccessFile();
				if (conn != null) {
					stmt = conn.createStatement();
					rs = stmt.executeQuery("Select * From PD_BASEFORMULA");
					List<String> sqlList = new ArrayList<>();
					StringBuilder sqlBuffer = new StringBuilder();
					while (rs.next()) {
						sqlBuffer.setLength(0);
						sqlBuffer.append("Insert Into PD_BASEFORMULA");
						sqlBuffer.append("(companyid,name,formula,isstop,type) values (");
						sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
						sqlBuffer.append(",");
						sqlBuffer.append("'").append(rs.getString("name")).append("'");
						sqlBuffer.append(",");
						sqlBuffer.append("'").append(rs.getString("formula")).append("'");
						sqlBuffer.append(",");
						sqlBuffer.append("'").append(rs.getString("isstop")).append("'");
						sqlBuffer.append(",");
						sqlBuffer.append("'").append(rs.getString("type")).append("'");
						sqlBuffer.append(")");
						sqlList.add(sqlBuffer.toString());
					}
					comDao.transactionUpdate(sqlList);
				}
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			} finally {
				Global.close(conn, stmt, rs);
			}
		}
		return stateInfo;
	}

	//===================================================================================//
	//************************* 开始解析公式代码块 *****************************************//
	//===================================================================================//
	@LogAnnotation(moduleName = "基本公式", option = "解析公式方法")
	@Override
	public StateInfo getData(final String formula, Map<String, Object> paramMap) throws Exception{
		StateInfo stateInfo = new StateInfo();
		try {
			Logger.getLogger(this.getClass()).info("原始公式为：" + formula + "，参数如下：" + JSONObject.toJSONString(paramMap));

			//获取 公式中文名称  匹配的 公式字典信息 集合
			Map<String, BaseFormula> formulaInfoMap = (Map<String, BaseFormula>) paramMap.get("formulaInfoMap");

			//1 拆分公式
			Set<String> formulaSingles = this.getSingleFormulas(formula);

			//2 解析单体公式
			Map<String, Map<String, String>> formula_key_value = this.getSingleFormulasData(formulaSingles, formulaInfoMap, paramMap);

			//3 获取key 最大集
			Set<String> keys = new HashSet<>();
			for (String key : formula_key_value.keySet()) {
				keys.addAll(formula_key_value.get(key).keySet());
			}

			//4 字符串四则运算最终结果
			Map<String, Map<String, String>> key_datadescMap = new HashMap<>();
			String formulaTemp = null;
			String formulaDescTemp = null;
			for (String key : keys) {
				formulaTemp = formula;
				formulaDescTemp = formula;
				for (String singleFormula : formula_key_value.keySet()) {
					formulaDescTemp = formulaTemp.replace(singleFormula,CommonUtil.nullToZero(CommonUtil.getMV(formula_key_value.get(singleFormula), key)) + singleFormula);
					formulaTemp = formulaTemp.replace(singleFormula, CommonUtil.nullToZero(CommonUtil.getMV(formula_key_value.get(singleFormula), key)));
				}
				Map<String, String> dd = new HashMap<>();
				dd.put("data", MathUtil.cacComplex(formulaTemp, Integer.parseInt(CommonUtil.nullToZero(String.valueOf(paramMap.get("dec"))))));
				dd.put("desc", formulaDescTemp + " = " + dd.get("data"));
				Logger.getLogger(this.getClass()).info("公式：" + formula + " 主键：" + key + " 计算结果：" + dd.get("desc"));
				key_datadescMap.put(key, dd);
			}

			//5 返回结果
			stateInfo.setData(key_datadescMap);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	private Map<String, Map<String, String>> getSingleFormulasData(Set<String> formulaSingles, Map<String, BaseFormula> formulaInfoMap, Map<String, Object> paramMap) {
		Map<String, Map<String, String>> formula_key_value = new HashMap<>();
		for (String formula : formulaSingles) {
			//[公式()] => [公式]
			String formulaWithoutParam = formula.substring(0,formula.indexOf("(")) + "]";
			Logger.getLogger(this.getClass()).info("当前解析单个公式：" + formula);
			Map<String, String> result = this.dealSingleFormulasData(formula, CommonUtil.getMV(formulaInfoMap, formulaWithoutParam), paramMap);
			formula_key_value.put(formula, result);
		}
		return formula_key_value;
	}

	/**
	 * 获取单体公式集合
	 *
	 * @param formula 公式中文名称集合 [公式1()]+ [公式2()] / [公式3()]
	 * @return 单个公式中文名称集合 [[公式1()],[公式2()],[公式3()]]
	 */
	@Override
	public Set<String> getSingleFormulas(String formula) throws Exception {
		Set<String> formulaSet = new HashSet<>();
		while (formula.contains("[")) {
			formulaSet.add(formula.substring(formula.indexOf("["), formula.indexOf("]") + 1));
			formula = formula.substring(formula.indexOf("]") + 1);
		}
		Logger.getLogger(this.getClass()).info("1、解析单体公式组：" + JSONObject.toJSONString(formulaSet));
		return formulaSet;
	}

	@Override
	public Map<String, Object> getParamMap(Bridge bridge, String month_begin, String month_end) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("formulaInfoMap", this.getFormulaInfoByName(null, bridge));
		paramMap.put("month_begin", CommonUtil.isEmpty(month_begin) ? bridge.getMonth() : month_begin);
		paramMap.put("month_end", CommonUtil.isEmpty(month_end) ? bridge.getMonth() : month_end);
		paramMap.put("date_begin", bridge.getYear()+String.valueOf(paramMap.get("month_begin"))+"-01");
		paramMap.put("date_end", bridge.getYear()+String.valueOf(paramMap.get("month_end"))+"-01");
		paramMap.put("companyid", bridge.getCompanyid());
		paramMap.put("yearmonth", bridge.getYmstr());
		paramMap.put("year", bridge.getYear());
		paramMap.put("userid", bridge.getUserid());
		paramMap.put("bridge",bridge);
		return paramMap;
	}

	/**
	 * 获取单个公式的计算结果。
	 *
	 * @param formulaBean 公式信息
	 * @param paramMap 参数集合
	 * @return
	 */
	private Map<String, String> dealSingleFormulasData(String formulaWithParam, BaseFormula formulaBean, Map<String, Object> paramMap) {
		String singleFormula = null;
		if (formulaWithParam.contains("[")) {
			singleFormula = formulaWithParam.substring(formulaWithParam.indexOf("[") + 1, formulaWithParam.indexOf("]"));
		} else {
			singleFormula = formulaWithParam;
		}

		String date_begin = String.valueOf(paramMap.get("date_begin"));
		String date_end = String.valueOf(paramMap.get("date_end"));
		Map<String, String> result4SingleFormula = null;
		String singleFormula_with_params = singleFormula;
		// 判断是否为特殊列运算
		if (!singleFormula.matches("(C[0-9]+)")) {
			//据获得的公式名判断是否获取过该公式
			//普通公式字典
			String[] paramsArr = null;
			if (singleFormula.contains("(")) {
				//截取括号后面参数
				String params = singleFormula.substring(singleFormula.indexOf("(") + 1, singleFormula.indexOf(")"));
				//去掉括号的中文公式
				singleFormula = singleFormula.substring(0, singleFormula.indexOf("("));
				if (params.contains(",")) {
					paramsArr = params.split(",");
				} else if (CommonUtil.isNotEmpty(params)) {
					paramsArr = new String[1];
					paramsArr[0] = params;
				}
			}
			//获取对应的SQL  eg: select key, value from table where xx = [dd]
			String sqlstr = formulaBean.getFormula();
			if (CommonUtil.isEmpty(sqlstr)) {
				throw new RuntimeException("公式对应的SQL为空！公式名称：" + singleFormula);
			}
			//paramsArr 按顺序替换 sqlstr 里的参数
			if (formulaBean.getType().equals(Global.SQLMODEL)) {
				//处理动态条件，替换条件占位符为具体参数值
				sqlstr = this.dealIfParam(sqlstr, paramsArr, paramMap);
				//处理含权限语句，处理日期查询@date参数
				sqlstr = this.dealDateParam(sqlstr, date_begin, date_end);
				//处理二级报表或者三级报表的@key参数
				sqlstr = this.dealSecondReportKeyParam(sqlstr, null, paramMap);
				//常用参数替换
				sqlstr = this.dealBridgeParam(sqlstr,paramMap);
				//强行增加选取字段的别名 mkey,mvalue
				//截取select 到 from 那一段
				String tempSelect = sqlstr.substring(0, sqlstr.toUpperCase().indexOf("FROM"));
				//加上别名
				if (!tempSelect.contains(",")) {
					// 如果查询中没有“,”，意味着仅仅查询一个字段。这样就不能以key/value的形式存储在map中
					Logger.getLogger(this.getClass()).error("该公式不符合正确格式！未解析到 , 无法获取到MKEY MVALUE" + singleFormula + " FORMULASQL:" + sqlstr);
				}
				//处理含有函数名的 例如 select substr(xx,1,4),sum(x2)  增加别名
				if (tempSelect.contains("),")) {
					tempSelect = tempSelect.substring(0, tempSelect.indexOf(")") + 1) + " AS MKEY," +
							tempSelect.substring(tempSelect.indexOf(")") + 2) + " AS MVALUE ";
				} else {
					//单纯处理普通的 select xx,x2 增加别名
					tempSelect = tempSelect.substring(0, tempSelect.indexOf(",")) + " AS MKEY," +
							tempSelect.substring(tempSelect.indexOf(",") + 1) + " AS MVALUE ";
				}
				//重新拼上SQL FROM后面的部分
				sqlstr = tempSelect + sqlstr.substring(sqlstr.toUpperCase().indexOf("FROM"));

				//普通列公式的日期解析
				Logger.getLogger(this.getClass()).info(singleFormula + " 解析后SQL: \r\n" + sqlstr);

				//验证SQL安全
				StateInfo stateInfo = commonService.valiadSelectSQL(sqlstr);
				if (stateInfo.getFlag()) {
					result4SingleFormula = CommonUtil.changetListToMap(commonService.getListInfos(sqlstr), "MKEY", "MVALUE");
				} else {
					throw new RuntimeException(stateInfo.getMsg());
				}
			} else if (formulaBean.getType().equals(Global.JSONMODEL)) {
				//直接获取定义的JSON值
				Logger.getLogger(this.getClass()).info(singleFormula + " 获取" + sqlstr);
				List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONObject.parse(sqlstr.replace(Global.FORMULAJSONTOP, ""));
				result4SingleFormula = new HashMap<>();
				for (Map<String, Object> map : obj) {
					result4SingleFormula.put(String.valueOf(map.get("key")), String.valueOf(map.get("value")));
				}
			} else if (formulaBean.getType().equals(Global.DATEMODEL)) {
				Logger.getLogger(this.getClass()).info(singleFormula + " 获取" + sqlstr);
				List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONObject.parse(sqlstr.replace(Global.FORMULADATETOP, ""));
				result4SingleFormula = CommonUtil.getDateKVList(date_end, obj.get(0));
			}
		}
		Logger.getLogger(this.getClass()).info("单体公式：" + singleFormula + " 获取结果：" + result4SingleFormula);
		//TODO 千分位，看具体情况
		if (result4SingleFormula != null) {
			for (String key : result4SingleFormula.keySet()) {
				if (Integer.parseInt(String.valueOf(paramMap.get("dec"))) >= 0) {
					try {
						//开始精确小数位
						BigDecimal bg = new BigDecimal(CommonUtil.nullToZero(result4SingleFormula.get(key)));
						result4SingleFormula.put(key, bg.setScale(Integer.parseInt(String.valueOf(paramMap.get("dec"))), BigDecimal.ROUND_HALF_UP).toPlainString());
					} catch (Exception e) {
						//如果不为数字就手动帮他把小数位设置为 -1。
						Logger.getLogger(this.getClass()).info("当前无法转换为数字，自动变更为非数字列。KEY：" + key + " VALUE:" + CommonUtil.nullToZero(result4SingleFormula.get(key)));
					}
					if ("null".equals(result4SingleFormula.get(key))) {
						result4SingleFormula.put(key, CommonUtil.nullToStr("0"));
					}
				}
			}
		}
		return result4SingleFormula;
	}

	public String dealBridgeParam(String sqlstr, Map<String, Object> paramMap) {
		Bridge bridge = (Bridge) paramMap.get("bridge");
		sqlstr = sqlstr.replace("@yearmonth",bridge.getYmstr());
		sqlstr = sqlstr.replace("@YEARMONTH",bridge.getYmstr());
		sqlstr = sqlstr.replace("@year",bridge.getYear());
		sqlstr = sqlstr.replace("@YEAR",bridge.getYear());
		sqlstr = sqlstr.replace("@companyid",bridge.getCompanyid());
		sqlstr = sqlstr.replace("@COMPANYID",bridge.getCompanyid());
		sqlstr = sqlstr.replace("@month",bridge.getMonth());
		sqlstr = sqlstr.replace("@MONTH",bridge.getMonth());
		sqlstr = sqlstr.replace("@keyword",String.valueOf(paramMap.get("keyword")));
		sqlstr = sqlstr.replace("@KEYWORD",String.valueOf(paramMap.get("keyword")));
		return sqlstr;
	}

	/**
	 * 处理动态条件，替换条件具体参数值
	 *
	 * @param sqlOriginal 原始SQL
	 * @param paramsArr   动态条件公式参数信息
	 * @param paramMap    页面动态条件参数信息
	 * @return 处理后的SQL
	 */
	private String dealIfParam(String sqlOriginal, String[] paramsArr, Map<String, Object> paramMap) {
		String ifText = null;
		//获取定义公式时候的动态条件参数并替换
		Map<String, ParamBean> ifText_paramBeanMap = paramService.getMap();
		//临时字符串变量
		StringBuilder temp = new StringBuilder();
		temp.append(sqlOriginal);
		if (paramsArr != null && paramsArr.length != 0) {
			for (String param : paramsArr) {
				ifText = temp.substring(temp.indexOf("["), temp.indexOf("]") + 1);
				temp.delete(temp.indexOf("["), temp.indexOf("]") + 1);
				ParamBean paramInfo = CommonUtil.getMV(ifText_paramBeanMap, ifText);
				//替换参数
				if (CommonUtil.isEmpty(param) && paramInfo != null) {
					param = String.valueOf(paramMap.get(paramInfo.getReverse1()));
				}
				if (CommonUtil.isNotEmpty(param)) {
					sqlOriginal = sqlOriginal.replace(ifText, param);
				}
			}
		}
		StringBuilder resultBuffer = new StringBuilder();
		//替换and 为大写的AND
		sqlOriginal = sqlOriginal.replace("and", "AND");
		sqlOriginal = sqlOriginal.replace("group", "GROUP");
		sqlOriginal = sqlOriginal.replace("union", "UNION");

		//先按UNION把SQL串拆分。
		String[] sqlItems = sqlOriginal.split("UNION");
//		if(sqlItems.length == 0) {
//			sqlItems = new String[1];
//			sqlItems[0] = sqlOriginal;
//		}
		//循环解析Union 子语句
		for (String sqlstr : sqlItems) {
			//当循环超过一次的时候，说明有union语句。
			if (resultBuffer.length() > 0) {
				resultBuffer.append("UNION");
			}

			//拆出 group by 的后面字符串，如果有的话
			String sqlstr_groupby = null;
			if (sqlstr.contains("GROUP")) {
				sqlstr_groupby = sqlstr.substring(sqlstr.indexOf("GROUP"));
				sqlstr = sqlstr.substring(0, sqlstr.indexOf("GROUP"));
			}
			//换行符拆分
			String[] nstrs = sqlstr.split("\r");
			StringBuilder stringBuilder = new StringBuilder();
			if (nstrs.length > 0) {
				for (String str : nstrs) {
					if (str.contains("AND")) {
						//按 AND进行拆分 判断条件是否存在 并赋值条件
						String[] strs = str.split("AND");
						if (strs.length > 0) {
							for (int i = 0; i < strs.length; i++) {
								String andStr = strs[i];
								//未设置的参数应当放弃
								if (andStr.contains("[") && andStr.contains("]")) {
									//有可能是缺省设置。
									//报表展示列表自定义条件数值替换
									ifText = andStr.substring(andStr.indexOf("["), andStr.indexOf("]") + 1);
									ParamBean paramInfo = CommonUtil.getMV(ifText_paramBeanMap, ifText);
									//替换参数
									if (paramInfo != null) {
										String param = String.valueOf(paramMap.get(paramInfo.getReverse1()));
										if (CommonUtil.isNotEmpty(param)) {
											andStr = andStr.replace(ifText, param);
										} else {
											continue;
										}
									}
								}
								//拆分的第一个前面不需要加AND,空不需要加 并且如果第一次拼sql也不需要加and
								if (stringBuilder.length() > 0 && CommonUtil.isNotEmpty(andStr) && i != 0) {
									stringBuilder.append(" AND ").append(andStr);
								} else {
									stringBuilder.append(andStr);
								}
							}
						}
					} else {
						//换行的直接拼接上
						if (stringBuilder.length() > 0) {
							stringBuilder.append('\r').append(str);
						} else {
							stringBuilder.append(str);
						}
					}
				}
			}

			resultBuffer.append(stringBuilder.toString());
			//拼凑上group 后面的语句
			if (sqlstr_groupby != null) {
				resultBuffer.append(" \r").append(sqlstr_groupby);
			}

		}

		return resultBuffer.toString();
	}

	/**
	 * 处理日期查询参数 @date{datefield} 并返回处理好的SQL串
	 *
	 * @param sqlstr     未处理前SQL
	 * @param date_begin 开始日期
	 * @param date_end   结束日期
	 * @return 处理后的SQL
	 */
	private String dealDateParam(String sqlstr, String date_begin, String date_end) {
		StringBuilder stringBuilder = new StringBuilder();
		Calendar calendar = Calendar.getInstance();//日历对象
		try {
			calendar.setTime(new Date());
			// 设置Calendar日期为下一个月一号
			calendar.set(Calendar.DATE, 1);

			if (CommonUtil.isEmpty(date_begin)) {
				//默认当前月份第一天
//						date_begin = Global.year_month_day.format(calendar.getTime()) + " 00:00:00";
				date_begin = Global.year_month_day_no_.format(new Date()) + "000000";
			} else {
				date_begin += "000000";
			}
			if (CommonUtil.isEmpty(date_end)) {
				// 设置Calendar月份数为下一个月
//						calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
				// 设置Calendar日期减一,为本月末
//						calendar.add(Calendar.DATE, -1);
				//默认当前月份最后一天
//						date_end = Global.year_month_day.format(calendar.getTime()) + " 23:59:59";
				date_end = Global.year_month_day_no_.format(new Date()) + "235959";
			} else {
				date_end += "235959";
			}


		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("处理@DATE内置参数失败：");
			Logger.getLogger(this.getClass()).error(e);
			throw new RuntimeException(e.getMessage());
		}
		date_begin = date_begin.replace("-", "");
		date_end = date_end.replace("-", "");

		String date_begin_str = null;
		String date_end_str = null;
		//含日期参数再进行解析
		while (CommonUtil.isNotEmpty(sqlstr) && sqlstr.toUpperCase().contains("@DATE")) {
			date_begin_str = date_begin;
			date_end_str = date_end;
			stringBuilder.setLength(0);
			//@date前片段
			stringBuilder.append(sqlstr.substring(0, sqlstr.toUpperCase().indexOf("@DATE")));
			//解析@date并拼凑SQL语句
			String[] dateParam = sqlstr.substring(sqlstr.indexOf("{", sqlstr.toUpperCase().indexOf("@DATE")) + 1, sqlstr.indexOf("}", sqlstr.toUpperCase().indexOf("@DATE"))).split(";");
			if (dateParam.length < 1) {
				throw new RuntimeException("@DATE 参数个数少于1，不合法！");
			} else {
				if (dateParam.length > 1) {
					// 0 -1 这类参数则无视 开始日期和结束日期
					if (dateParam.length > 2) {
						calendar.setTime(new Date());//设置当前日期
						calendar.add(Calendar.MONTH, Integer.parseInt(dateParam[2]));//月份减一
						date_begin_str = Global.year_month_day_time_no_.format(calendar.getTime());
						date_end_str = Global.year_month_day_time.format(new Date());
					}
					//格式化日期
					String dfStr = dateParam[1];
					DateFormat df = new SimpleDateFormat(dfStr);
					try {
						date_begin_str = df.format(Global.year_month_day_time_no_.parse(date_begin_str));
						date_end_str = df.format(Global.year_month_day_time_no_.parse(date_end_str));
					} catch (ParseException e) {
						throw new RuntimeException("@date日期格式化参数有误，请重新定义SQL");
					}
				}else {
					DateFormat df = new SimpleDateFormat("MM");
					try {
						date_begin_str = df.format(Global.year_month_day_time_no_.parse(date_begin_str));
						date_end_str = df.format(Global.year_month_day_time_no_.parse(date_end_str));
					} catch (ParseException e) {
						throw new RuntimeException("@date日期格式化参数有误，请重新定义SQL");
					}
				}

				//拼凑日期条件SQL语句
				String dbDateFiled = dateParam[0];
				stringBuilder.append(dbDateFiled).append(" >= '").append(date_begin_str).append("' ");
				stringBuilder.append("AND ");
				stringBuilder.append(dbDateFiled).append(" <= '").append(date_end_str).append("' ");
			}
			//@date后片段
			stringBuilder.append(sqlstr.substring(sqlstr.indexOf("}", sqlstr.toUpperCase().indexOf("@DATE")) + 1));
			sqlstr = stringBuilder.toString();
		}
		return sqlstr;
	}

	/**
	 * 二级报表砖取数据的时候通常会传递选中行的key 方便进行二级报表sql获取数据
	 * 这里把sqlstr 的  @key 替换为  '页面传递key的具体值'
	 *
	 * @param sqlstr 公式sql串
	 * @param key    页面传递key的具体值
	 * @return 处理完后的sqlstr
	 */
	private String dealSecondReportKeyParam(String sqlstr, String key, Map<String, Object> parammap) {
		if (!(sqlstr.contains("@key") || sqlstr.contains("@seckey") || sqlstr.contains("@threekey"))) {
			return sqlstr;
		} else {
			//这里要求页面写sql的时候 @key要小写，如果这里统一转换为小写字符可能会影响参数里面的值从而导致结果不对
			String[] params = {"key", "seckey", "threekey"};
			for (String param : params) {
				String paramvalue = String.valueOf(parammap.get(param));
				Logger.getLogger(this.getClass()).info("获取@" + param + "值为" + paramvalue);
				if (CommonUtil.isEmpty(paramvalue)) {
					paramvalue = "%%";
				} else {
					//传递多列值过来，就用|区分。
					if (paramvalue.contains("|")) {
						//这里判断 是否值为  张三|  如果最后一位是 | 那么会少截取一位所以加个空格就正常了。
						if (paramvalue.charAt(paramvalue.length() - 1) == '|') {
							paramvalue = paramvalue + " ";
						}
						String[] paramvalues = paramvalue.split("\\|");
						for (int index = 0; index < paramvalues.length; index++) {
							String temp = paramvalues[index];
							if (CommonUtil.isEmpty(temp)) {
								temp = "%%";
							}
							Logger.getLogger(this.getClass()).info("替换@" + param + "[" + index + "] 值：" + "'" + temp + "'");
							sqlstr = sqlstr.replace("@" + param + "[" + index + "]", "'" + temp + "'");
						}
					}
				}
				sqlstr = sqlstr.replace("@" + param, "'" + paramvalue + "'");
			}
			return sqlstr;
		}
	}
}