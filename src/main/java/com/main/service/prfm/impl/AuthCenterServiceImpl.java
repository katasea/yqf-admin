package com.main.service.prfm.impl;

import com.common.AccessOptUtil;
import com.common.CommonUtil;
import com.common.Global;
import com.main.aop.LogAnnotation;
import com.main.dao.prfm.AuthCenterDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.AuthCenter;
import com.main.service.prfm.AuthCenterService;
import com.main.service.prfm.BaseFormulaService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class AuthCenterServiceImpl implements AuthCenterService {
	@Resource
	private BaseFormulaService baseFormulaService;
	@Resource
	private AuthCenterDao authCenterDao;

	@Override
	@LogAnnotation(moduleName = "权限中心", option = "获取权限页面权限分类")
	public List<Map<String, Object>> getList4Type(String funcode) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> resultMap = new ArrayList<>();
		try {
			conn = AccessOptUtil.connectAccessFile();
			if (conn != null) {
				stmt = conn.createStatement();
				String sql = "SELECT * FROM Setting_AuthCenter";
				if(!CommonUtil.isEmpty(funcode)) {
					sql += " where code = '"+funcode+"'";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					Map<String, Object> map = new HashMap<>();
					map.put("code", rs.getString("code"));
					map.put("name", rs.getString("name"));
					map.put("sqloriginal", rs.getString("sqloriginal"));
					map.put("sqltarget", rs.getString("sqltarget"));
					map.put("add", rs.getString("add"));
					resultMap.add(map);
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error(e.getMessage(), e);
		} finally {
			Global.close(conn, stmt, rs);
		}
		return resultMap;
	}

	@Override
	@LogAnnotation(moduleName = "权限中心", option = "获取待分配列表")
	public Map<String,Object> getMorigList(String keyword, String funcode, Bridge bridge, String start, String limit) {
		List<Map<String,Object>> authTypeInfo = this.getList4Type(funcode);
		Map<String,Object> result = null;
		if(!CommonUtil.isEmpty(authTypeInfo)) {
			Map<String,Object> paramMap = baseFormulaService.getParamMap(bridge,null,null);
			paramMap.put("keyword",CommonUtil.nullToStr(keyword));
			String sqlstr = this.baseFormulaService.dealBridgeParam(String.valueOf(authTypeInfo.get(0).get("sqloriginal")),paramMap);
			result = this.getListBySQL(sqlstr, start, limit, false,null);
		}
		return result;
	}


	@Override
	@LogAnnotation(moduleName = "权限中心", option = "获取待关联列表")
	public Map<String,Object> getMtargList(String keyword, String funcode, Bridge bridge, String start, String limit, String morig) {
		List<Map<String,Object>> authTypeInfo = this.getList4Type(funcode);
		Map<String,Object> result = null;
		if(!CommonUtil.isEmpty(authTypeInfo)) {
			Map<String,Object> paramMap = baseFormulaService.getParamMap(bridge,null,null);
			paramMap.put("keyword",CommonUtil.nullToStr(keyword));
			String sqlstr = this.baseFormulaService.dealBridgeParam(String.valueOf(authTypeInfo.get(0).get("sqltarget")),paramMap);
			result = this.getListBySQL(sqlstr, start, limit, true,morig);
		}
		return result;
	}

	private Map<String,Object> getListBySQL(String sqlstr,String start, String limit, boolean filter,String morig) {
		String tempSelect = sqlstr.substring(0, sqlstr.toUpperCase().indexOf("FROM"));
		//加上别名
		if (!tempSelect.contains(",")) {
			// 如果查询中没有“,”，意味着仅仅查询一个字段。这样就不能以key/value的形式存储在map中
			Logger.getLogger(this.getClass()).error("该公式不符合正确格式！未解析到 , 无法获取到MKEY MVALUE FORMULASQL:" + sqlstr);
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
		//如果开启过滤
		if(filter) {
			sqlstr = "SELECT MKEY,MVALUE FROM (" + sqlstr + ") A WHERE MKEY NOT IN (select mtarg from pd_authcenter where morig = '"+morig+"')";
		}
		Logger.getLogger(this.getClass()).info("原始列表SQL:" + sqlstr);
		Map<String,Object> map = new HashMap<>();
		map.put("root",authCenterDao.getPageResult(sqlstr,Integer.parseInt(start),Integer.parseInt(start) + Integer.parseInt(limit)));
		map.put("total",authCenterDao.getCountResult(sqlstr));
		return map;
	}

	@Override
	@LogAnnotation(moduleName = "权限中心", option = "获取已关联权限列表")
	public List<Map<String, Object>> getAuthList(String keyword, String funcode, Bridge bridge, String morig, String start, String limit) {
		return authCenterDao.getAuthPage(keyword,funcode,bridge,morig,Integer.parseInt(start),Integer.parseInt(start) + Integer.parseInt(limit));
	}

	@Override
	@LogAnnotation(moduleName = "权限中心", option = "获取已关联权限总量")
	public int getAuthCount(String keyword, String funcode, Bridge bridge, String morig) {
		return authCenterDao.getAuthCount(keyword,funcode,bridge,morig);
	}

	@Override
	@LogAnnotation(moduleName = "权限中心", option = "新增权限")
	public StateInfo addAuth(Bridge bridge, AuthCenter authCenter) {
		StateInfo stateInfo = new StateInfo();
		try {
			authCenter.setCompanyid(bridge.getCompanyid());
			authCenter.setPkid(Global.createUUID());
			authCenterDao.addAuth(bridge,authCenter);
		}catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
	@LogAnnotation(moduleName = "权限中心", option = "删除权限对应")
	public StateInfo delAuth(Bridge bridge, String pkid) {
		StateInfo stateInfo = new StateInfo();
		try {
			authCenterDao.delAuth(bridge,pkid);
		}catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
	@LogAnnotation(moduleName = "权限中心", option = "清空权限对应")
	public StateInfo delAuthByMorig(Bridge bridge, String morig) {
		StateInfo stateInfo = new StateInfo();
		try {
			authCenterDao.delAuthByMorig(bridge,morig);
		}catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}
}
