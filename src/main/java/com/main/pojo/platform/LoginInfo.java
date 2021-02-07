package com.main.pojo.platform;

import com.common.CommonUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * 保存于session，贯穿整个Spring MVC
 * @author CFQ
 */
public class LoginInfo implements Serializable {
	private static final long serialVersionUID = 5780371099533288435L;
	private String ymstr;
	private boolean isAdmin;
	private String date;
	private String month;
	private String year;
	private String userid;
	private String username;
	private String deptid;
	private String deptname;
	private String companyid;
	private String companyname;
	private List<RoleInfo> roles;
	private List<ResourcesInfo> resources;//还未赋值

	public LoginInfo(HttpServletRequest request) {
		UserInfo user = (UserInfo)request.getSession().getAttribute("userSession");
		if(user!=null) {
			this.userid = user.getUserid();
			this.username = user.getUsername();
			this.companyid = user.getCompanyid();
			this.companyname = user.getCompanyname();
			this.deptid = user.getDeptid();
			this.deptname = user.getDeptname();
			this.roles = user.getRoleInfos();
			this.setAdmin(user.isAdmin());
		}
		/**
		 * 操作的业务日期
		 */
		String date = request.getParameter("date");
		if(!CommonUtil.isEmpty(date)) {
			Logger.getLogger(this.getClass()).warn("获取参数业务时间 ："+date);
			this.year = date.split("-")[0];
			this.month = date.split("-")[1];
			this.date = date;
		}
		this.setYmstr(this.getYear()+"-"+this.getMonth());
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

	public String getYmstr() {
		return ymstr;
	}

	public void setYmstr(String ymstr) {
		this.ymstr = ymstr;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
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

	public List<RoleInfo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleInfo> roles) {
		this.roles = roles;
	}

	public List<ResourcesInfo> getResources() {
		return resources;
	}

	public void setResources(List<ResourcesInfo> resources) {
		this.resources = resources;
	}
}
