package com.main.pojo.platform;

import com.common.CommonUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 桥接信息，贯穿整个Spring MVC
 * @author CFQ
 */
public class Bridge implements Serializable {

	private static final long serialVersionUID = 6984723783615711191L;

	private String ymstr;
	private String date;
	private String month;
	private String year;
	private String userid;
	private String username;
	private String deptid;
	private String deptname;
	private String companyid;
	private String companyname;
	private boolean isAdmin;

	public Bridge(HttpServletRequest request) {
		UserInfo user = (UserInfo)request.getSession().getAttribute("userSession");
		LoginInfo loginInfo = (LoginInfo) request.getSession().getAttribute("loginSession");
		if(user!=null) {
			this.userid = user.getUserid();
			this.username = user.getUsername();
			this.deptid = user.getDeptid();
			this.deptname = user.getDeptname();
			this.setAdmin(user.isAdmin());
		}
		if(loginInfo!=null) {
			this.setYear(loginInfo.getYear());
			this.setMonth(loginInfo.getMonth());
			this.setDate(loginInfo.getDate());
			this.setYmstr(loginInfo.getYear()+"-"+loginInfo.getMonth());
			this.companyid = loginInfo.getCompanyid();
			this.companyname = loginInfo.getCompanyname();
		}

		//操作的业务日期
		String date = request.getParameter("date");
		if(!CommonUtil.isEmpty(date)) {
			Logger.getLogger(this.getClass()).warn("获取参数业务时间 ："+date);
			this.year = date.split("/")[0];
			this.month = date.split("/")[1];
			this.date = this.year +"-"+this.month+"-01";
			this.ymstr = this.year+"-"+this.month;
		}
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	/**
	 * get & set
	 */


	public String getYmstr() {
		return ymstr;
	}

	private void setYmstr(String ymstr) {
		this.ymstr = ymstr;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

}
