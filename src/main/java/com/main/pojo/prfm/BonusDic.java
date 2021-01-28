package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;
import com.main.pojo.platform.TreeNodeBean;

import java.io.Serializable;

/**
 * 定义字典
 */
@Table(name = "PD_BONUSDIC")
public class BonusDic extends TreeNodeBean implements Serializable{
	private static final long serialVersionUID = 5552216655049904579L;
	@Column(flag = "primary")
	private String companyid;
	@Column(flag = "primary")
	private String bh;
	@Column(flag = "primary")
	private int year;
	@Column(flag = "primary")
	private int month;
	@Column
	private String name;
	@Column(type="int")
	private int grade;
	@Column(type="int")
	private int isleaf;
	@Column(type="varchar(500)")
	private String formula;
	@Column
	private String parentid;
	@Column(type="int")
	private int isstop;

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(int isleaf) {
		this.isleaf = isleaf;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}
}
