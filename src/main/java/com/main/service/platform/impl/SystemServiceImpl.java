package com.main.service.platform.impl;


import com.common.AccessOptUtil;
import com.common.CommonUtil;
import com.common.Global;
import com.common.PasswordHelper;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.SystemDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.UserInfo;
import com.main.service.platform.BaseDicInfoService;
import com.main.service.platform.SystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {
	@Resource
	private SystemDao sdao;
	@Resource
	private CommonDao comDao;
	@Resource
	private BaseDicInfoService basDicService;
//	@Resource
//	private RunSQLFileService rsfs;

	@Override
	public StateInfo init(UserInfo userInfo) {
		StateInfo stateInfo = new StateInfo();
		userInfo.setUserid("admin");
		userInfo.setUsername("超级管理员");
		userInfo.setSex("1");
		userInfo.setEnable("1");
		userInfo.setDeptid(Global.NULLSTRING);
		userInfo.setEntrytime(Global.year_month_day.format(new Date()));
		//加密密码
		PasswordHelper.encryptPassword(userInfo);
		//1 初始化单位信息
		if (stateInfo.getFlag()) {
			stateInfo = this.initCompany(userInfo.getCompanyid(), userInfo.getCompanyname());
		}
		//2 初始化管理员信息
		if (stateInfo.getFlag()) {
			stateInfo = this.initAdminUser(userInfo);
		}
		//3 初始化角色信息
		if (stateInfo.getFlag()) {
			stateInfo = this.initRole();
		}
		//4 初始化菜单信息
		if (stateInfo.getFlag()) {
			stateInfo = this.initResource();
		}
		//5 初始化对应关系【角色和资源】
		if (stateInfo.getFlag()) {
			stateInfo = this.initRoleResRela();
		}
		//6 初始化对应关系【用户和角色】
		if (stateInfo.getFlag()) {
			stateInfo = this.initUserRoleRela();
		}
		//7 初始化字典
		if (stateInfo.getFlag()) {
			stateInfo = basDicService.autoInitFromAccess();
		}
		return stateInfo;
	}

	/**
	 * 从Access初始化菜单资源
	 *
	 * @return 状态信息
	 */
	@Override
	public StateInfo initResource() {
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = AccessOptUtil.connectAccessFile();
			if(conn == null) throw new Exception();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select * From PM_RESOURCE");
			List<String> sqlList = new ArrayList<>();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append("Delete From PM_RESOURCE");
			sqlList.add(sqlBuffer.toString());
			sqlBuffer.setLength(0);
			while (rs.next()) {
				sqlBuffer.setLength(0);
				sqlBuffer.append("Insert Into PM_RESOURCE");
				sqlBuffer.append("(pkid,resid,name,resurl,parentid,isleaf,type,sort,fa) values (");
				sqlBuffer.append("'").append(rs.getString("pkid")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("resid")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("name")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("resurl"))).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(CommonUtil.nullToStr(rs.getString("parentid"))).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("isleaf")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("type")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("sort")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("fa")).append("'");
				sqlBuffer.append(")");
				sqlList.add(sqlBuffer.toString());
			}
			comDao.transactionUpdate(sqlList);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		} finally {
			Global.close(conn, stmt, rs);
		}
		return stateInfo;
	}

	@Override
	public StateInfo initRole() {
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = AccessOptUtil.connectAccessFile();
			if(conn == null) throw new Exception();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select * From PM_ROLE");
			List<String> sqlList = new ArrayList<>();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append("Delete From PM_ROLE");
			sqlList.add(sqlBuffer.toString());
			sqlBuffer.setLength(0);
			while (rs.next()) {
				sqlBuffer.setLength(0);
				sqlBuffer.append("Insert Into PM_ROLE");
				sqlBuffer.append("(pkid,roleid,roledesc) values (");
				sqlBuffer.append("'").append(rs.getString("pkid")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("roleid")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("roledesc")).append("'");
				sqlBuffer.append(")");
				sqlList.add(sqlBuffer.toString());
			}
			comDao.transactionUpdate(sqlList);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		} finally {
			Global.close(conn, stmt, rs);
		}
		return stateInfo;
	}

	@Override
	public StateInfo initRoleResRela() {
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = AccessOptUtil.connectAccessFile();
			if(conn == null) throw new Exception();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select * From PM_ROLE_RES");
			List<String> sqlList = new ArrayList<>();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append("Delete From PM_ROLE_RES");
			sqlList.add(sqlBuffer.toString());
			sqlBuffer.setLength(0);
			while (rs.next()) {
				sqlBuffer.setLength(0);
				sqlBuffer.append("Insert Into PM_ROLE_RES");
				sqlBuffer.append("(rolepkid,respkid) values (");
				sqlBuffer.append("'").append(rs.getString("rolepkid")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("respkid")).append("'");
				sqlBuffer.append(")");
				sqlList.add(sqlBuffer.toString());
			}
			comDao.transactionUpdate(sqlList);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		} finally {
			Global.close(conn, stmt, rs);
		}
		return stateInfo;
	}

	@Override
	public StateInfo initUserRoleRela() {
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = AccessOptUtil.connectAccessFile();
			if(conn == null) throw new Exception();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select * From PM_USER_ROLE");
			List<String> sqlList = new ArrayList<>();
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append("Delete From PM_USER_ROLE");
			sqlList.add(sqlBuffer.toString());
			sqlBuffer.setLength(0);
			while (rs.next()) {
				sqlBuffer.setLength(0);
				sqlBuffer.append("Insert Into PM_USER_ROLE");
				sqlBuffer.append("(userpkid,rolepkid) values (");
				sqlBuffer.append("'").append(rs.getString("userpkid")).append("'");
				sqlBuffer.append(",");
				sqlBuffer.append("'").append(rs.getString("rolepkid")).append("'");
				sqlBuffer.append(")");
				sqlList.add(sqlBuffer.toString());
			}
			comDao.transactionUpdate(sqlList);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		} finally {
			Global.close(conn, stmt, rs);
		}
		return stateInfo;
	}

	@Override
	public Map<String, Boolean> getDeptEmpRelaMonthBackupInfo() {
		String sql = "Select yearmonth from PM_DEPT_MB group by yearmonth";
		List<Map<String, Object>> list = comDao.getListForMap(sql);
		return CommonUtil.changetListToMap(list, "yearmonth", true);
	}

	/**
	 * 初始化管理员信息
	 *
	 * @param userInfo 用户信息
	 * @return 状态信息
	 */
	private StateInfo initAdminUser(UserInfo userInfo) {
		StateInfo stateInfo = new StateInfo();
		try {
			sdao.initAdminUser(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	/**
	 * 初始化单位信息
	 *
	 * @param companyid 单位编号
	 * @param companyname 单位名称
	 * @return 状态信息
	 */
	private StateInfo initCompany(String companyid, String companyname) {
		StateInfo stateInfo = new StateInfo();
		try {
			sdao.initCompany(companyid, companyname);
		} catch (Exception e) {
			e.printStackTrace();
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}
}
