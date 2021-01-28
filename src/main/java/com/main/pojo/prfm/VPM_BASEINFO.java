package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 基础数据字典
 */
@Table(name = "VPM_BASEINFO")
public class VPM_BASEINFO implements Serializable {

	private static final long serialVersionUID = -924866605770404495L;

	@Column
	private String hospital;//医院编号
	@Column
	private String dept;//科室编号
	@Column
	private String dept_name;//科室名称
	@Column(type="decimal(18,2)")
	private String bedcount;//科室床位数
	@Column(type="decimal(18,2)")
	private String bedused;//实际使用床位数
	@Column(type="decimal(18,2)")
	private String dispcount;//门诊人数
	@Column(type="decimal(18,2)")
	private String emscount;//急诊人数
	@Column(type="decimal(18,2)")
	private String resicount;//住院人数
	@Column(type="decimal(18,2)")
	private String outcount;//出院人数
	@Column(type="decimal(18,2)")
	private String dept_area;//科室占用面积
	@Column(type="decimal(18,2)")
	private String drc;//医生人数
	@Column(type="decimal(18,2)")
	private String nsc;//护理人数
	@Column(type="varchar(200)")
	private String remark;//备注

	//=========================GETTER && SETTER==============================//


	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
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

	public String getBedcount() {
		return bedcount;
	}

	public void setBedcount(String bedcount) {
		this.bedcount = bedcount;
	}

	public String getBedused() {
		return bedused;
	}

	public void setBedused(String bedused) {
		this.bedused = bedused;
	}

	public String getDispcount() {
		return dispcount;
	}

	public void setDispcount(String dispcount) {
		this.dispcount = dispcount;
	}

	public String getEmscount() {
		return emscount;
	}

	public void setEmscount(String emscount) {
		this.emscount = emscount;
	}

	public String getResicount() {
		return resicount;
	}

	public void setResicount(String resicount) {
		this.resicount = resicount;
	}

	public String getOutcount() {
		return outcount;
	}

	public void setOutcount(String outcount) {
		this.outcount = outcount;
	}

	public String getDept_area() {
		return dept_area;
	}

	public void setDept_area(String dept_area) {
		this.dept_area = dept_area;
	}

	public String getDrc() {
		return drc;
	}

	public void setDrc(String drc) {
		this.drc = drc;
	}

	public String getNsc() {
		return nsc;
	}

	public void setNsc(String nsc) {
		this.nsc = nsc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
