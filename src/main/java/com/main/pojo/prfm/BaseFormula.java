package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 基本公式类
 */

@Table(name = "PD_BASEFORMULA")
public class BaseFormula implements Serializable {

	private static final long serialVersionUID = -3916103980318562011L;
	@Column(flag = "primary")
	private String companyid;
	@Column(flag = "primary")
	private String name;
	@Column
	private int isstop;
	@Column(type = "varchar(5000)", oth = "NOT NULL")
	private String formula;
	@Column(oth = "NOT NULL")
	private String type;//1 SQL 2 JSON 3 DATE

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
