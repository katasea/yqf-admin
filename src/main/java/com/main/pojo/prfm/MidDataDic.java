package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 中间数据字典
 */
@Table(name = "PD_MIDDATADIC")
public class MidDataDic implements Serializable{

	private static final long serialVersionUID = -6079673538638327664L;

	@Column(flag = "primary")
	private String companyid;
	private String companyname;
	@Column(flag = "primary")
	private String id;
	@Column(type="varchar(200)")
	private String name;//名称
	@Column(type = "int")
	private int type;//1数据录入 2自动计算
	@Column(type = "int")
	private int isstop;//0 否  1 是
	@Column
	private String parentid;//大类
	private String parentname;//大类
	@Column(type="varchar(2000)")
	private String auth;//权限
	@Column(type="int")
	private int deptorper;//0全部 1部门  2个人
	@Column(type="int")
	private int dec;//小数位


	//==============================GETTER && SETTER===================//

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public int getDeptorper() {
		return deptorper;
	}

	public void setDeptorper(int deptorper) {
		this.deptorper = deptorper;
	}

	public int getDec() {
		return dec;
	}

	public void setDec(int dec) {
		this.dec = dec;
	}
}
