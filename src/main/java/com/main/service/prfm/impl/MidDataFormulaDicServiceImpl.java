package com.main.service.prfm.impl;

import com.common.AccessOptUtil;
import com.common.CommonUtil;
import com.common.Global;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.MidDataFormulaDicDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.MidData;
import com.main.pojo.prfm.MidDataDic;
import com.main.pojo.prfm.MidDataFormulaDic;
import com.main.service.prfm.BaseFormulaService;
import com.main.service.prfm.MidDataDicService;
import com.main.service.prfm.MidDataFormulaDicService;
import com.main.service.prfm.MidDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * 对象服务层实现类
 *
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class MidDataFormulaDicServiceImpl implements MidDataFormulaDicService {
	@Resource
	private MidDataFormulaDicDao midDataFormulaDicDao;//DAO
	@Resource
	private CommonDao comDao;//公共DAO
	@Resource
	private MidDataDicService midDataDicService;//辅助数据
	@Resource
	private MidDataService midDataService;//辅助数据
	@Resource
	private BaseFormulaService baseFormulaService; //公式解析

	@LogAnnotation(moduleName = "PD_MIDDATAFORMULADIC服务", option = "查询")
	@Override
	public MidDataFormulaDic selectByid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.midDataFormulaDicDao.selectByid(map);
	}

	@LogAnnotation(moduleName = "PD_MIDDATAFORMULADIC服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(midDataFormulaDicDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "已录入重复的id编号，请修改！", null);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public int getCount(String keyword, String node, Bridge bridge, String midBh) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("midBh", midBh);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		Map<String, Object> resultMap = this.midDataFormulaDicDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@LogAnnotation(moduleName = "PD_MIDDATAFORMULADIC服务", option = "查询列表数据")
	@Override
	public List<MidDataFormulaDic> get(String keyword, String node, Bridge bridge, String start, String limit, String midBh) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("midBh", midBh);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		return this.midDataFormulaDicDao.getPage(map);
	}

	@LogAnnotation(moduleName = "PD_MIDDATAFORMULADIC服务", option = "删除")
	@Override
	public StateInfo delete(String code, String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(code)) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id", code);
				//顺序不能乱
				this.midDataFormulaDicDao.deleteByPrimaryKey(map);
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
	private Map<String, Object> getBeanInfoMap(MidDataFormulaDic midDataFormulaDic, Bridge bridge) {
		midDataFormulaDic.setCompanyid(bridge.getCompanyid());

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid", midDataFormulaDic.getCompanyid());
		map.put("id", midDataFormulaDic.getId());
		map.put("orderid", midDataFormulaDic.getOrderid());
		map.put("name", midDataFormulaDic.getName());
		map.put("formula", midDataFormulaDic.getFormula());
		map.put("isstop", midDataFormulaDic.getIsstop());
		map.put("deptorper", midDataFormulaDic.getDeptorper());
		map.put("dec", midDataFormulaDic.getDec());
		map.put("caclfalg", midDataFormulaDic.getCaclfalg());
		map.put("failreason", midDataFormulaDic.getFailreason());
		return map;
	}

	@LogAnnotation(moduleName = "PD_MIDDATAFORMULADIC服务", option = "修改")
	@Override
	public StateInfo edit(String id, MidDataFormulaDic midDataFormulaDic, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(id) && midDataFormulaDic != null) {
			Map<String, Object> map = this.getBeanInfoMap(midDataFormulaDic, bridge);
			map.put("whereId", id);
			try {
				this.midDataFormulaDicDao.update(map);
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

	@LogAnnotation(moduleName = "PD_MIDDATAFORMULADIC服务", option = "新增")
	@Override
	public StateInfo add(String parentId, MidDataFormulaDic midDataFormulaDic, Bridge bridge) {
		//tree init
		//自动生成主键按1 101 10101规则
		Map<String, Object> mapOfID = this.midDataFormulaDicDao.getAutoGeneralID(parentId, bridge);
		midDataFormulaDic.setOrderid(Integer.parseInt(CommonUtil.dealPKRule(mapOfID, parentId)));
		midDataFormulaDic.setPkid(String.valueOf(midDataFormulaDic.getOrderid()));
		Map<String, Object> map = this.getBeanInfoMap(midDataFormulaDic, bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.midDataFormulaDicDao.insert(map);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	/**
	 * 初始化内置数据
	 */
	@LogAnnotation(moduleName = "PD_MIDDATAFORMULADIC服务", option = "初始化数据")
	@Override
	public StateInfo autoInitFromAccess(Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = AccessOptUtil.connectAccessFile();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("Select * From PD_MIDDATAFORMULA");
				List<String> sqlList = new ArrayList<>();
				StringBuilder sqlBuffer = new StringBuilder();
				while (rs.next()) {
					sqlBuffer.setLength(0);
					sqlBuffer.append("Insert Into PD_MIDDATAFORMULADIC");
					sqlBuffer.append("(pkid,companyid,id,orderid,name,formula,isstop,dec,caclfalg,failreason) values (");
					sqlBuffer.append("newid(),'").append(bridge.getCompanyid()).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("id")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append(rs.getInt("orderid"));
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("name")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("formula")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("isstop")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append(rs.getInt("dec"));
					sqlBuffer.append(",");
					sqlBuffer.append(0);
					sqlBuffer.append(",");
					sqlBuffer.append("''");

					sqlBuffer.append(")");
					sqlList.add(sqlBuffer.toString());
				}
				this.midDataFormulaDicDao.deleteAll(bridge.getYear());
				comDao.transactionUpdate(sqlList);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		} finally {
			Global.close(conn, stmt, rs);
		}
		return stateInfo;
	}

	@Override
	public StateInfo calc(String midBh, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		//成功计算的 辅助公式字典
		List<String> calcMidFormulaInfo = new ArrayList<>();
		String errorMsg = null;
		String errorcode = null;
		String infoMsg = null;
		try {
			//TODO MidDataDic 后续可能需要加入排序号orderid 目前只是根据编号的升序做计算顺序。
			//1 获取所有辅助公式数据及其对应的公式定义信息。
			Map<String, MidDataDic> infoMsgOfMidDic = new HashMap<>();
			List<MidDataDic> listOfForumulaMidData = midDataDicService.getByType(bridge.getCompanyid(), 2);
			List<MidDataFormulaDic> listOfFormula = this.midDataFormulaDicDao.getAll(bridge.getCompanyid());//根据排序号升序做计算顺序。
			Map<String, List<MidDataFormulaDic>> midBh_FormulaInfoMap = new HashMap<>();
			if (listOfFormula == null) {
				return stateInfo;
			}
			for (MidDataFormulaDic dic : listOfFormula) {
				List<MidDataFormulaDic> temp = midBh_FormulaInfoMap.get(dic.getId());
				if (temp == null) temp = new ArrayList<>();
				temp.add(dic);
				midBh_FormulaInfoMap.put(dic.getId(), temp);
			}

			//2 判断全部计算还是只计算当前
			Map<String, String> calcMidInfo = new HashMap<>();//记录已经计算过的辅助数据.
			List<String> dics = new ArrayList<>();
			for (MidDataDic dic : listOfForumulaMidData) {
				if(CommonUtil.isNotEmpty(midBh)&&!dic.getId().equals(midBh)) {
					continue;
				}
				dics.add(dic.getId());
				infoMsgOfMidDic.put(dic.getId(), dic);
				calcMidInfo.put(dic.getId(), "false");
			}

			//3 循环需要计算的辅助公式

			boolean breakFlag;//当前循环跳过标识
			Map<String, Object> paramMap = baseFormulaService.getParamMap(bridge, null, null);
			MidDataDic tempMsg = null;
			//midbh,key,{data,desc}
			Map<String, Map<String, Map<String,String>>> calcResult = new HashMap<>();//最终结果
			int sizeOld = 0;
			int sizeNow = 0;
			while (dics.size() > 0) {
				sizeNow = dics.size();
				//8 循环未计算列表 如果下一次循环 未计算列表总数没有减少 则调出循环并抛出异常。死锁了。
				if(sizeOld != 0 && sizeOld == sizeNow) {
					throw new Exception("循环死锁，公式顺序有误！");
				}
				Iterator<String> it = dics.iterator();
				while (it.hasNext()) {
					String mid = it.next();
					breakFlag = false;
					tempMsg = infoMsgOfMidDic.get(mid);
					infoMsg = tempMsg.getParentname() + "->" + tempMsg.getId() + " " + tempMsg.getName();//页面错误提示
					//4 循环每个公式定义
					List<MidDataFormulaDic> temp = midBh_FormulaInfoMap.get(mid);
					if (temp != null) {
						for (MidDataFormulaDic formulaDic : temp) {
							errorcode = formulaDic.getPkid();//当前计算的节点
							//5 判断公式是否依赖于其他辅助数据如果是则放置在未计算列表
							if (formulaDic.getFormula().contains(Global.MIDDATAFORMULATEXT)) {
								breakFlag = this.judgeCalcAfter(calcMidInfo, formulaDic.getFormula());//判断辅助数据是否已经计算过了。
							}
							if (!breakFlag) {
								//6 获取计算结果并保存。
								paramMap.put("dec", formulaDic.getDec());//小数位
								stateInfo = baseFormulaService.getData(formulaDic.getFormula(), paramMap);
								if(!stateInfo.getFlag()) {
									return stateInfo;
								}
								Map<String, Map<String,String>> result = (Map<String, Map<String, String>>) stateInfo.getData();
								calcMidFormulaInfo.add(formulaDic.getPkid());//计算成功
								//7 获取整个辅助公式数据的计算结果
								Map<String, Map<String,String>> tempOfResult = calcResult.get(mid);
								//这里采用辅助字典的小数位 即 先四舍五入再求和
								calcResult.put(mid, CommonUtil.addMap(tempOfResult, result, tempMsg.getDec()));
							} else {
								break;
							}
						}
					}
					if (!breakFlag) {
						calcMidInfo.put(mid, "true");
						it.remove();
					}
				}
				sizeOld = dics.size();
			}

			//9 批量插入计算结果。
			List<MidData> list = new ArrayList<>();
			//midbh,key,{data,desc}
			for(String mid : calcResult.keySet()) {
				midDataService.delete(mid,null,bridge);
				for(String bh : calcResult.get(mid).keySet()) {
					MidData data = new MidData();
					data.setCompanyid(bridge.getCompanyid());
					data.setId(mid);
					data.setBh(bh);
					data.setData(calcResult.get(mid).get(bh).get("data"));
					data.setProcess(calcResult.get(mid).get(bh).get("desc"));
					data.setDeptorper(infoMsgOfMidDic.get(mid).getDeptorper());
					data.setMonth(Integer.parseInt(bridge.getMonth()));
					data.setPkid(Global.createUUID());
					list.add(data);
				}
				stateInfo = midDataService.insertBatch(bridge.getYear(),list);
				list.clear();
				if(!stateInfo.getFlag()) {
					break;
				}
			}
			//10 批量更新辅助公式计算状态和错误原因
			this.midDataFormulaDicDao.updateCalcSucc(calcMidFormulaInfo);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), infoMsg + " 公式未能成功计算！ " + e.getMessage(), null);
			errorMsg = e.getMessage();
			//11 记录并更新计算失败的信息
			this.midDataFormulaDicDao.updateCalcError(errorcode,errorMsg);
		}

		return stateInfo;
	}

	private boolean judgeCalcAfter(Map<String, String> calcMidInfo, String formula) throws Exception {
		Set<String> formulas = baseFormulaService.getSingleFormulas(formula);
		for (String singleformula : formulas) {
			if (singleformula.contains(Global.MIDDATAFORMULATEXT)) {
				String paramStr = singleformula.substring(singleformula.indexOf("(") + 1, singleformula.indexOf(")"));
				String[] params = paramStr.split(",");
				//如果从列表中获取不为空，并且获取到的是未计算的。说明这条需要稍后计算。
				if (CommonUtil.isNotEmpty(calcMidInfo.get(params[0])) && "false".equals(calcMidInfo.get(params[0]))) {
					return true;
				}
			}
		}
		//全部循环完如果都符合就不需要待会儿计算，可以马上计算。
		return false;
	}

}