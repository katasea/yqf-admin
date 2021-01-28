package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 基础数据字典
 */
@Table(name = "VPM_BASEDIC")
public class VPM_BASEDIC implements Serializable {

	private static final long serialVersionUID = -924866605770404495L;

	@Column(flag = "primary")
	private String hospital;//医院编号
	@Column(flag = "primary")
	private String code;//编号
	@Column(flag = "primary")
	private int type;//类型 1 HIS部门 2HIS人员 3HIS核算项目 4发票项目 10财务部门 11财务科目
	@Column(oth = "NOT NULL")
	private String name;//名称
	@Column
	private String dept;//类型为2时 人员所在的科室
	@Column
	private String dept_name;//科室名称

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
}
