package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;
import com.common.CommonUtil;
import com.common.Global;

/**
 * 科室信息月备份信息表
 *
 * @author CFQ
 */
@Table(name = "PM_DEPT_MB")
public class DeptInfoMonthBackup extends TreeNodeBean {
	/**
	 *
	 */
	private static final long serialVersionUID = -9011777967101276026L;
	/**
	 * 主键，自动生成
	 */
	@Column
	private String pid;
	/**
	 * 关联单位编号
	 */
	@Column(flag = "primary")
	private String companyid;
	/**
	 * 关联的单位名称
	 */
	private String companyname;
	/**
	 * 科室编号
	 */
	@Column(flag = "primary")
	private String deptid;
	/**
	 * 年月标识 存入格式 2018-09
	 */
	@Column(flag = "primary")
	private String yearmonth;
	/**
	 * 科室名称
	 */
	@Column(oth = "NOT NULL")
	private String deptname;
	/**
	 * 助记码
	 */
	@Column(oth = "NOT NULL")
	private String zjm;
	/**
	 * 科室类型，取系统字典进行选择
	 */
	@Column(oth = "NOT NULL")
	private String depttype;

	private String depttypeText;
	/**
	 * 父节点
	 */
	@Column(oth = "NOT NULL")
	private String parentid;
	/**
	 * 是否叶子节点
	 */
	@Column(type = "int")
	private int isleaf;
	/**
	 * 科室状态
	 * 1停用还是0启用
	 */
	@Column(type = "int")
	private int isstop;// 0启用,1停用
	/**
	 * 插入时间
	 * 如果为空每个月都会显示该部门
	 * 如果不为空则在此时间之后才会显示该部门
	 */
	@Column(type = "varchar(10)")
	private String inserttime;//格式：2018-03-20
	/**
	 * 停用时间
	 * 如果该部门被停用那么停用之前登录系统查看则会显示该部门
	 */
	@Column(type = "varchar(10)")
	private String stoptime;//格式：2018-03-20

	//===========================GETTERS && SETTERS=============================//


	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getDepttypeText() {
		return depttypeText;
	}

	public void setDepttypeText(String depttypeText) {
		this.depttypeText = depttypeText;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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

	public String getZjm() {
		return zjm;
	}

	public void setZjm(String zjm) {
		this.zjm = zjm;
	}

	public String getDepttype() {
		return depttype;
	}

	public void setDepttype(String depttype) {
		this.depttype = depttype;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public int getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(int isleaf) {
		this.isleaf = isleaf;
	}

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		if(CommonUtil.isEmpty(inserttime)) {
			inserttime = Global.NULLSTRING;
		}
		this.inserttime = inserttime;
	}

	public String getStoptime() {
		return stoptime;
	}

	public void setStoptime(String stoptime) {
		if(CommonUtil.isEmpty(stoptime)) {
			stoptime = Global.NULLSTRING;
		}
		this.stoptime = stoptime;
	}
}
